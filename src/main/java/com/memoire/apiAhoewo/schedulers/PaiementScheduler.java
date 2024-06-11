package com.memoire.apiAhoewo.schedulers;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Configuration
public class PaiementScheduler {
    @Autowired
    private PaiementService paiementService;
    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void envoyerRappelsPaiementsEnAttenteParModeDePaiement() {
        List<Paiement> paiementList = paiementService.getPaiementByModePaiementEnAttente();

        for (Paiement paiement : paiementList) {
            Notification notification = new Notification();
            notification.setTitre(paiement.getPlanificationPaiement().getTypePlanification() + " en attente !");
            notification.setMessage("Un paiement " + paiement.getCodePaiement() + " effectué pour la planification de paiement " + paiement.getPlanificationPaiement().getCodePlanification() + " est en attente.");
            notification.setSendTo(String.valueOf(paiement.getPlanificationPaiement().getCreerPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/paiement/" + paiement.getId());
            notification.setCreerPar(paiement.getCreerPar());
            notification.setCreerLe(new Date());
            notificationService.save(notification);
        }
    }
}
