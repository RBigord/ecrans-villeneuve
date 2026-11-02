package com.ecrans.vente.adapter.in.planification;

import com.ecrans.vente.application.port.in.ExpirerBlocages;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Adapter primaire : réveille le cas d'usage ExpirerBlocages chaque minute (B2).
 * C'est le service de l'étape 5 qui travaille ; le planificateur ne fait que l'appeler.
 */
@Component
public class ExpirationPlanifiee {

    private final ExpirerBlocages expirerBlocages;

    public ExpirationPlanifiee(ExpirerBlocages expirerBlocages) {
        this.expirerBlocages = expirerBlocages;
    }

    @Scheduled(fixedRate = 60_000)
    public void chaqueMinute() {
        expirerBlocages.executer();
    }
}
