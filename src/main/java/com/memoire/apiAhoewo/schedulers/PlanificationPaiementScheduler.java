package com.memoire.apiAhoewo.schedulers;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesPaiements.PlanificationPaiement;
import com.memoire.apiAhoewo.services.NotificationService;
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

            if (diffDays == -1) {
                envoyerNotificationClient(planificationPaiement, "Demain", "Vous avez une planification de paiement : " + planificationPaiement.getLibelle() + " pour demain.");
                envoyerNotificationProprietaire(planificationPaiement, "Demain", client + " " + planificationPaiement.getContrat().getClient().getNom() + " " + planificationPaiement.getContrat().getClient().getPrenom() + " a une planification de paiement : " + planificationPaiement.getLibelle() + " pour demain.");
            } else if (diffDays == 0) {
                envoyerNotificationClient(planificationPaiement, "Aujourd'hui", "Vous avez une planification de paiement : " + planificationPaiement.getLibelle() + " pour aujourd'hui.");
                envoyerNotificationProprietaire(planificationPaiement, "Aujourd'hui",  client + " " + planificationPaiement.getContrat().getClient().getNom() + " " + planificationPaiement.getContrat().getClient().getPrenom() + " a une planification de paiement : " + planificationPaiement.getLibelle() + " pour aujourd'hui.");
            } else if (diffDays == 1) {
                envoyerNotificationClient(planificationPaiement, "Un jour de retard", "Votre planification de paiement : " + planificationPaiement.getLibelle() + " est en retard d'un jour.");
                envoyerNotificationProprietaire(planificationPaiement, "Un jour de retard", client + " " + planificationPaiement.getContrat().getClient().getNom() + " " + planificationPaiement.getContrat().getClient().getPrenom() + " a une planification de paiement : " + planificationPaiement.getLibelle() + " en retard d'un jour.");
            } else if (diffDays == 2) {
                envoyerNotificationClient(planificationPaiement, "Deux jours de retard", "Votre planification de paiement : " + planificationPaiement.getLibelle() + " est en retard de deux jours.");
                envoyerNotificationProprietaire(planificationPaiement, "Deux jours de retard", client + " " + planificationPaiement.getContrat().getClient().getNom() + " " + planificationPaiement.getContrat().getClient().getPrenom() + " a une planification de paiement : " + planificationPaiement.getLibelle() + " en retard de deux jours.");
            } else if (diffDays == 3) {
                envoyerNotificationClient(planificationPaiement, "Trois jours de retard", "Votre planification de paiement : " + planificationPaiement.getLibelle() + " est en retard de trois jours.");
                envoyerNotificationProprietaire(planificationPaiement, "Trois jours de retard", client + " " + planificationPaiement.getContrat().getClient().getNom() + " " + planificationPaiement.getContrat().getClient().getPrenom() + " a une planification de paiement : " + planificationPaiement.getLibelle() + " en retard de trois jours.");
            } else if (diffDays == 5) {
                envoyerNotificationClient(planificationPaiement, "Cinq jours de retard", "Votre planification de paiement : " + planificationPaiement.getLibelle() + " est en retard de cinq jours.");
                envoyerNotificationProprietaire(planificationPaiement, "Cinq jours de retard", client + " " + planificationPaiement.getContrat().getClient().getNom() + " " + planificationPaiement.getContrat().getClient().getPrenom() + " a une planification de paiement : " + planificationPaiement.getLibelle() + " en retard de cinq jours.");
            }
        }
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
        notification.setSendTo(String.valueOf(planificationPaiement.getCreerPar())); // Assuming 'getProprietaire()' method exists
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/planification-paiement/" + planificationPaiement.getId());
        notification.setCreerPar(planificationPaiement.getCreerPar());
        notification.setCreerLe(new Date());
        notificationService.save(notification);
    }
}
