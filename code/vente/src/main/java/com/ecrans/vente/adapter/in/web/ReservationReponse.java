package com.ecrans.vente.adapter.in.web;

import java.time.LocalDateTime;

/** Le JSON renvoyé au client : aucun objet du domaine n'en sort, seulement des types simples. */
public record ReservationReponse(String commande, long totalCentimes, String total, String urlPaiement,
                                 LocalDateTime payerAvant) {
}
