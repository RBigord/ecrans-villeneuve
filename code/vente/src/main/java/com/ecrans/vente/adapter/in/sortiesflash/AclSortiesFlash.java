package com.ecrans.vente.adapter.in.sortiesflash;

import com.ecrans.vente.application.exception.SeanceInconnue;
import com.ecrans.vente.application.port.in.DemandeBillet;
import com.ecrans.vente.application.port.in.VentePartenaire;
import com.ecrans.vente.domaine.commande.Categorie;
import com.ecrans.vente.domaine.commun.Montant;
import com.ecrans.vente.domaine.commun.PlaceId;
import com.ecrans.vente.domaine.commun.SeanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Anti-corruption layer : traduit une commande SortiesFlash dans notre langage (B11).
 * Si SortiesFlash change un code, seule cette classe bouge ; CinéMalin aura sa propre ACL.
 * Java pur : aucune dépendance à Spring, testable seule.
 */
public class AclSortiesFlash {

    static final String PARTENAIRE = "SortiesFlash";

    private static final Map<String, Categorie> CATEGORIES = Map.of(
            "ADULT", Categorie.PLEIN,
            "STUDENT", Categorie.REDUIT,
            "CHILD", Categorie.ENFANT);

    private final CorrespondanceSeances correspondance;

    public AclSortiesFlash(CorrespondanceSeances correspondance) {
        this.correspondance = correspondance;
    }

    /**
     * @throws SeanceInconnue           si leur show_ref ne correspond à aucune de nos séances (→ 404)
     * @throws IllegalArgumentException si le format est invalide : type inconnu, nombre de types ≠ nombre de places (→ 400)
     */
    public VentePartenaire traduire(CommandeSortiesFlash commande) {
        SeanceId seance = correspondance.seance(commande.show_ref())
                .orElseThrow(() -> new SeanceInconnue(new SeanceId(String.valueOf(commande.show_ref()))));

        if (commande.seats() == null || commande.ticket_types() == null
                || commande.seats().size() != commande.ticket_types().size()) {
            throw new IllegalArgumentException("Il faut autant de ticket_types que de seats");
        }

        List<DemandeBillet> billets = new ArrayList<>();
        for (int i = 0; i < commande.seats().size(); i++) {
            billets.add(new DemandeBillet(PlaceId.de(commande.seats().get(i)), categorie(commande.ticket_types().get(i))));
        }

        return new VentePartenaire(PARTENAIRE, commande.partner_order_id(), seance, billets,
                Montant.centimes(commande.paid_amount_cents()));
    }

    private static Categorie categorie(String ticketType) {
        Categorie categorie = CATEGORIES.get(ticketType);
        if (categorie == null) {
            throw new IllegalArgumentException("Type de billet SortiesFlash inconnu : " + ticketType);
        }
        return categorie;
    }
}
