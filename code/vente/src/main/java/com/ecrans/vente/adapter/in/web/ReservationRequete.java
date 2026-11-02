package com.ecrans.vente.adapter.in.web;

import java.util.List;

/** Le JSON envoyé par le site, l'appli et les bornes pour réserver. Il ne sort pas de l'adapter web. */
public record ReservationRequete(List<BilletRequete> billets) {

    /** Une place choisie, telle que le client l'écrit : « A1 », « PLEIN ». */
    public record BilletRequete(String place, String categorie) {
    }
}
