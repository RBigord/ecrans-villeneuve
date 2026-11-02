package com.ecrans.vente.application.port.in;

import com.ecrans.vente.domaine.commun.Montant;
import com.ecrans.vente.domaine.commun.SeanceId;

import java.util.List;

/**
 * Une vente déjà payée chez un partenaire (B11), traduite dans notre langage par son ACL.
 * Le montant payé chez le partenaire est conservé : la déclaration CNC et le reversement en ont besoin.
 */
public record VentePartenaire(String partenaire, String referencePartenaire, SeanceId seance,
                              List<DemandeBillet> billets, Montant montantPaye) {
    public VentePartenaire {
        billets = List.copyOf(billets);
    }
}
