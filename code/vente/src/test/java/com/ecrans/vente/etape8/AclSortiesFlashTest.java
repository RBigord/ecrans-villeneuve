package com.ecrans.vente.etape8;

import com.ecrans.vente.adapter.in.sortiesflash.AclSortiesFlash;
import com.ecrans.vente.adapter.in.sortiesflash.CommandeSortiesFlash;
import com.ecrans.vente.adapter.in.sortiesflash.CorrespondanceSeances;
import com.ecrans.vente.application.exception.SeanceInconnue;
import com.ecrans.vente.application.port.in.DemandeBillet;
import com.ecrans.vente.application.port.in.VentePartenaire;
import com.ecrans.vente.domaine.commande.Categorie;
import com.ecrans.vente.domaine.commun.Montant;
import com.ecrans.vente.domaine.commun.PlaceId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ecrans.vente.support.Donnees.S42;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Étape 8 · l'ACL SortiesFlash, sans Spring : leur format entre, notre langage sort. */
class AclSortiesFlashTest {

    private static final String SHOW_REF = "VIL-20261112-2030-S3";

    private AclSortiesFlash acl;

    @BeforeEach
    void preparer() {
        CorrespondanceSeances correspondance = new CorrespondanceSeances();
        correspondance.associer(SHOW_REF, S42);
        acl = new AclSortiesFlash(correspondance);
    }

    private static CommandeSortiesFlash commande(String showRef, List<String> seats, List<String> types) {
        return new CommandeSortiesFlash(showRef, seats, types, "SF-77812", 1840);
    }

    @Test
    void traduit_leur_seance_et_leurs_types_de_billets_dans_notre_langage() {
        VentePartenaire vente = acl.traduire(commande(SHOW_REF, List.of("G7", "G8", "G9"), List.of("ADULT", "STUDENT", "CHILD")));

        assertThat(vente.seance()).isEqualTo(S42);
        assertThat(vente.billets()).containsExactly(
                new DemandeBillet(PlaceId.de("G7"), Categorie.PLEIN),
                new DemandeBillet(PlaceId.de("G8"), Categorie.REDUIT),
                new DemandeBillet(PlaceId.de("G9"), Categorie.ENFANT));
    }

    @Test
    void conserve_le_montant_paye_chez_le_partenaire_et_sa_reference_B11() {
        VentePartenaire vente = acl.traduire(commande(SHOW_REF, List.of("G7"), List.of("ADULT")));

        assertThat(vente.montantPaye()).isEqualTo(Montant.centimes(1840));
        assertThat(vente.partenaire()).isEqualTo("SortiesFlash");
        assertThat(vente.referencePartenaire()).isEqualTo("SF-77812");
    }

    @Test
    void une_seance_inconnue_chez_nous_est_refusee() {
        assertThatThrownBy(() -> acl.traduire(commande("VIL-19990101-2030-S3", List.of("G7"), List.of("ADULT"))))
                .isInstanceOf(SeanceInconnue.class);
    }

    @Test
    void un_type_de_billet_inconnu_est_un_format_invalide() {
        assertThatThrownBy(() -> acl.traduire(commande(SHOW_REF, List.of("G7"), List.of("VIP"))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void il_faut_autant_de_types_que_de_places() {
        assertThatThrownBy(() -> acl.traduire(commande(SHOW_REF, List.of("G7", "G8"), List.of("ADULT"))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
