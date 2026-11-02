package com.ecrans.vente.application.service;

import com.ecrans.vente.application.exception.SeanceInconnue;
import com.ecrans.vente.application.port.in.EnregistrerVentePartenaire;
import com.ecrans.vente.application.port.in.VentePartenaire;
import com.ecrans.vente.application.port.out.Commandes;
import com.ecrans.vente.application.port.out.GrilleTarifaire;
import com.ecrans.vente.application.port.out.PublicateurEvenements;
import com.ecrans.vente.application.port.out.Seances;
import com.ecrans.vente.domaine.commande.Billet;
import com.ecrans.vente.domaine.commande.Commande;
import com.ecrans.vente.domaine.commande.Tarif;
import com.ecrans.vente.domaine.commun.CommandeId;
import com.ecrans.vente.domaine.evenement.VentePartenaireEnregistree;
import com.ecrans.vente.domaine.seance.Seance;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Une vente déjà payée chez un partenaire suit les mêmes règles que les nôtres (B1 à B4), sans passer par PayVia
 * et sans blocage de 10 minutes : les places sont bloquées puis vendues aussitôt, la commande est payée tout de suite.
 */
public class EnregistrerVentePartenaireService implements EnregistrerVentePartenaire {

    private final Seances seances;
    private final Commandes commandes;
    private final GrilleTarifaire grille;
    private final PublicateurEvenements publicateur;
    private final Clock horloge;

    public EnregistrerVentePartenaireService(Seances seances, Commandes commandes, GrilleTarifaire grille,
                                             PublicateurEvenements publicateur, Clock horloge) {
        this.seances = seances;
        this.commandes = commandes;
        this.grille = grille;
        this.publicateur = publicateur;
        this.horloge = horloge;
    }

    @Override
    public CommandeId executer(VentePartenaire vente) {
        LocalDateTime maintenant = LocalDateTime.now(horloge);
        CommandeId id = CommandeId.nouvelle();

        // les billets au prix de notre grille (B6) ; le prix payé chez le partenaire reste dans la vente
        List<Billet> billets = vente.billets().stream()
                .map(b -> new Billet(b.place(), new Tarif(b.categorie(), grille.prix(vente.seance(), b.categorie()))))
                .toList();

        // 1. le domaine décide : commande valide (B3), places libres (B1, tout ou rien), vente ouverte (B4)
        Commande commande = Commande.creer(id, vente.seance(), billets, maintenant); // déjà payée : pas d'attente
        Seance seance = seances.parId(vente.seance())
                .orElseThrow(() -> new SeanceInconnue(vente.seance()));
        seance.bloquer(commande.places(), id, maintenant);
        seance.vendre(id, commande.places(), maintenant);
        commande.payer(maintenant);

        // 2. on enregistre APRÈS
        seances.enregistrer(seance);
        commandes.enregistrer(commande);

        // 3. on publie, avec le montant réellement payé chez le partenaire
        commande.retirerEvenements().forEach(publicateur::publier);
        publicateur.publier(new VentePartenaireEnregistree(id, vente.partenaire(), vente.referencePartenaire(),
                vente.montantPaye(), maintenant));
        return id;
    }
}
