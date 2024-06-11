package com.memoire.apiAhoewo.schedulers;

import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaiementService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Configuration
public class PayPalScheduler {
    @Autowired
    private PaiementService paiementService;
    @Autowired
    private PaypalService paypalService;

    @Scheduled(fixedRate = 60000) // Vérifie toutes les 60 secondes
    public void checkPayoutStatus() {
        List<Paiement> pendingPayouts = paiementService.getPaiementsByStatutPaiementIfPayoutBatchIdExist();
        if (!pendingPayouts.isEmpty()) {
            for (Paiement paiement : pendingPayouts) {
                paypalService.updatePayoutStatus(paiement.getPayoutBatchId());
            }
        }
    }
}
