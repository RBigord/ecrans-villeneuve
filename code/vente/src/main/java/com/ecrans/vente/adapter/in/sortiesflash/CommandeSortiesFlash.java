package com.ecrans.vente.adapter.in.sortiesflash;

import java.util.List;

/**
 * Le JSON de SortiesFlash (annexe B), tel quel, avec leurs noms de champs.
 * C'est leur langage : il ne sort pas de ce package, l'ACL le traduit.
 */
public record CommandeSortiesFlash(String show_ref, List<String> seats, List<String> ticket_types,
                                   String partner_order_id, long paid_amount_cents) {
}
