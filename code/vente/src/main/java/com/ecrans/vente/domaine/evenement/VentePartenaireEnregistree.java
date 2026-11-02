package com.ecrans.vente.domaine.evenement;

import com.ecrans.vente.domaine.commun.CommandeId;
import com.ecrans.vente.domaine.commun.Montant;

import java.time.LocalDateTime;

/**
 * Une vente partenaire a été enregistrée. Elle porte le montant payé chez le partenaire,
 * différent de notre grille : la déclaration CNC (3.4) et le reversement (B11) le liront ici.
 */
public record VentePartenaireEnregistree(CommandeId commande, String partenaire, String referencePartenaire,
                                         Montant montantPaye, LocalDateTime survenuLe) implements EvenementVente {
}
