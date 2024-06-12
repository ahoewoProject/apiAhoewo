package com.memoire.apiAhoewo.schedulers;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.models.gestionDesPaiements.PlanificationPaiement;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaiementService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PlanificationPaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PlanificationPaiementScheduler {
    @Autowired
    private PlanificationPaiementService planificationPaiementService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PaiementService paiementService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void envoyerRappelsPlanificationsPaiementsEnAttente() {
        List<PlanificationPaiement> planificationPaiements = planificationPaiementService.getPlanificationsPaiementEnAttente();

        for (PlanificationPaiement planificationPaiement : planificationPaiements) {
            Date datePlanifie = planificationPaiement.getDatePlanifiee();
            Date currentDate = new Date();
            long diff = currentDate.getTime() - datePlanifie.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            String client;
            if (planificationPaiement.getTypePlanification().equals("Paiement de location")) {
                client = "Le Locataire";
            } else {
                client = "L'acquéreur";
            }

            Paiement paiement = paiementService.findByCodePlanification(planificationPaiement.getCodePlanification());
            if (paiement != null) {
                notifierPlanification(diffDays, planificationPaiement, client);
            }
        }
    }

    private void notifierPlanification(long diffDays, PlanificationPaiement planificationPaiement, String client) {
        String when;
        String messageClient;
        String messageProprietaire;
        String clientNomPrenom = planificationPaiement.getContrat().getClient().getNom() + " " + planificationPaiement.getContrat().getClient().getPrenom();
        String planificationLibelle = planificationPaiement.getLibelle();

        switch ((int) diffDays) {
            case -1:
                when = "Demain";
                messageClient = "Vous avez une planification de paiement : " + planificationLibelle + " pour demain.";
                messageProprietaire = client + " " + clientNomPrenom + " a une planification de paiement : " + planificationLibelle + " pour demain.";
                break;
            case 0:
                when = "Aujourd'hui";
                messageClient = "Vous avez une planification de paiement : " + planificationLibelle + " pour aujourd'hui.";
                messageProprietaire = client + " " + clientNomPrenom + " a une planification de paiement : " + planificationLibelle + " pour aujourd'hui.";
                break;
            case 1:
                when = "Un jour de retard";
                messageClient = "Votre planification de paiement : " + planificationLibelle + " est en retard d'un jour.";
                messageProprietaire = client + " " + clientNomPrenom + " a une planification de paiement : " + planificationLibelle + " en retard d'un jour.";
                break;
            case 2:
                when = "Deux jours de retard";
                messageClient = "Votre planification de paiement : " + planificationLibelle + " est en retard de deux jours.";
                messageProprietaire = client + " " + clientNomPrenom + " a une planification de paiement : " + planificationLibelle + " en retard de deux jours.";
                break;
            case 3:
                when = "Trois jours de retard";
                messageClient = "Votre planification de paiement : " + planificationLibelle + " est en retard de trois jours.";
                messageProprietaire = client + " " + clientNomPrenom + " a une planification de paiement : " + planificationLibelle + " en retard de trois jours.";
                break;
            case 5:
                when = "Cinq jours de retard";
                messageClient = "Votre planification de paiement : " + planificationLibelle + " est en retard de cinq jours.";
                messageProprietaire = client + " " + clientNomPrenom + " a une planification de paiement : " + planificationLibelle + " en retard de cinq jours.";
                break;
            default:
                return; // Aucun message pour les autres cas
        }

        envoyerNotificationClient(planificationPaiement, when, messageClient);
        envoyerNotificationProprietaire(planificationPaiement, when, messageProprietaire);
    }

    private void envoyerNotificationClient(PlanificationPaiement planificationPaiement, String titreSuffixe, String message) {
        Notification notification = new Notification();
        notification.setTitre("Planification de paiement - " + titreSuffixe);
        notification.setMessage(message + " pour le contrat de location " + planificationPaiement.getContrat().getCodeContrat());
        notification.setSendTo(String.valueOf(planificationPaiement.getContrat().getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/planification-paiement/" + planificationPaiement.getId());
        notification.setCreerPar(planificationPaiement.getCreerPar());
        notification.setCreerLe(new Date());
        notificationService.save(notification);
    }

    private void envoyerNotificationProprietaire(PlanificationPaiement planificationPaiement, String titreSuffixe, String message) {
        Notification notification = new Notification();
        notification.setTitre("Planification de paiement - " + titreSuffixe);
        notification.setMessage(message + " pour le contrat de location " + planificationPaiement.getContrat().getCodeContrat());
        if (planificationPaiement.getLibelle().equals("Avance/Caution")) {
            notification.setSendTo(String.valueOf(planificationPaiement.getContrat().getCreerPar()));
        } else {
            notification.setSendTo(String.valueOf(planificationPaiement.getCreerPar()));
        }
         // Assuming 'getProprietaire()' method exists
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/planification-paiement/" + planificationPaiement.getId());
        notification.setCreerPar(planificationPaiement.getCreerPar());
        notification.setCreerLe(new Date());
        notificationService.save(notification);
    }
}
