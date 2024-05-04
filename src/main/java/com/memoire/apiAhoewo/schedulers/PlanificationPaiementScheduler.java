package com.memoire.apiAhoewo.schedulers;

import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PlanificationPaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanificationPaiementScheduler {
    @Autowired
    private PlanificationPaiementService planificationPaiementService;
    @Autowired
    private NotificationService notificationService;

//    @Scheduled(cron = "0 0 8 * * ?")
//    public void envoyerRappelsPlanificationsPaiementsEnAttente() {
//        List<PlanificationPaiement> planificationPaiements = planificationPaiementService.getPlanificationsPaiementEnAttente();
//
//        for (PlanificationPaiement planificationPaiement : planificationPaiements) {
//            Notification notification = new Notification();
//            notification.setTitre("Nouvelle planification de paiement");
//            notification.setMessage("Vous avez une reçu une nouvelle planification de paiement de loyer pour le contrat de location " + planificationPaiement.getContrat().getCodeContrat());
//            notification.setSendTo(String.valueOf(planificationPaiement.getContrat().getClient().getId()));
//            notification.setDateNotification(new Date());
//            notification.setLu(false);
//            notification.setUrl("/planifications-paiements/" + planificationPaiement.getId());
//            notification.setCreerPar(personne.getId());
//            notification.setCreerLe(new Date());
//            notificationService.save(notification);
//        }
//    }
}
