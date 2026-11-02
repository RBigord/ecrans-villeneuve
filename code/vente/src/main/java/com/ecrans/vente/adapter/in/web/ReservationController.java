package com.ecrans.vente.adapter.in.web;

import com.ecrans.vente.application.port.in.DemandeBillet;
import com.ecrans.vente.application.port.in.DemandeReservation;
import com.ecrans.vente.application.port.in.Reservation;
import com.ecrans.vente.application.port.in.ReserverDesPlaces;
import com.ecrans.vente.domaine.commande.Categorie;
import com.ecrans.vente.domaine.commun.PlaceId;
import com.ecrans.vente.domaine.commun.SeanceId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Adapter primaire REST : le site, l'appli et les bornes réservent des places par HTTP.
 * Il ne connaît que le port ReserverDesPlaces : il traduit le JSON, appelle le port, traduit la réponse.
 */
@RestController
public class ReservationController {

    private final ReserverDesPlaces reserverDesPlaces;

    public ReservationController(ReserverDesPlaces reserverDesPlaces) {
        this.reserverDesPlaces = reserverDesPlaces;
    }

    @PostMapping("/seances/{seance}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationReponse reserver(@PathVariable String seance, @RequestBody ReservationRequete requete) {
        // 1. le JSON devient le langage du domaine (une place mal écrite lève IllegalArgumentException → 400)
        List<DemandeBillet> billets = requete.billets().stream()
                .map(b -> new DemandeBillet(PlaceId.de(b.place()), Categorie.valueOf(b.categorie())))
                .toList();

        // 2. le cas d'usage décide
        Reservation reservation = reserverDesPlaces.executer(new DemandeReservation(new SeanceId(seance), billets));

        // 3. la réponse du domaine redevient du JSON
        return new ReservationReponse(
                reservation.commande().valeur(),
                reservation.total().centimes(),
                reservation.total().toString(),
                reservation.urlPaiement(),
                reservation.payerAvant());
    }
}
