package com.memoire.apiAhoewo.schedulers;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeAchat;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeLocation;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeVisite;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeAchatService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeLocationService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeVisiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@Configuration
public class DemandeScheduler {
    @Autowired
    private DemandeVisiteService demandeVisiteService;
    @Autowired
    private DemandeLocationService demandeLocationService;
    @Autowired
    private DemandeAchatService demandeAchatService;
    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedDelay = 60000)
    public void envoyerRappelsDemandesVisitesValidees() {
        List<DemandeVisite> demandesValidees = demandeVisiteService.getDemandesVisitesValidees();

        for (DemandeVisite demande : demandesValidees) {
            if (estDansDeuxProchainesHeures(demande.getDateHeureVisite())) {
                Notification notification = new Notification();
                notification.setTitre("Rappel : Visite prévue");
                notification.setMessage("La visite pour " + demande.getPublication().getLibelle() + " est prévue pour " + demande.getDateHeureVisite() + ". N'oubliez pas!");
                notification.setSendTo(String.valueOf(demande.getPublication().getCreerPar())); // Envoyer à l'utilisateur qui a créé la publication
                notification.setLu(false);
                notification.setUrl("/demande-visite/" + demande.getId()); // Lien vers la demande de visite
                notification.setDateNotification(new Date());
                notification.setCreerPar(demande.getCreerPar()); // Utilisateur système qui envoie le rappel
                notification.setCreerLe(new Date());
                notificationService.save(notification); // Enregistrer la notification
            }
        }
    }

    // Méthode utilitaire pour vérifier si la date de visite est arrivée
    private boolean estDateDeVisiteArrivee(LocalDateTime dateVisite) {
        LocalDateTime maintenant = LocalDateTime.now();
        return dateVisite.isBefore(maintenant);
    }

    // Méthode utilitaire pour vérifier si la date de visite est dans les deux prochaines heures
    private boolean estDansDeuxProchainesHeures(LocalDateTime dateVisite) {
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime deuxHeuresAvant = maintenant.plusHours(2);
        return dateVisite.isAfter(maintenant) && dateVisite.isBefore(deuxHeuresAvant);
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void envoyerRappelsDemandesVisitesEnAttente() {
        List<DemandeVisite> demandesEnAttente = demandeVisiteService.getDemandesVisitesEnAttente();

        for (DemandeVisite demande : demandesEnAttente) {
            Notification notification =  new Notification();
            notification.setTitre("Demande de visite en attente.");
            notification.setMessage("La demande de visite soumise pour la publication de " + demande.getPublication().getLibelle() + " est toujours en attente");
            notification.setSendTo(String.valueOf(demande.getPublication().getCreerPar()));
            notification.setLu(false);
            notification.setUrl("/demande-visite/" + demande.getId());
            notification.setDateNotification(new Date());
            notification.setCreerPar(demande.getCreerPar());
            notification.setCreerLe(new Date());
            notificationService.save(notification);
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void envoyerAlertesDemandesLocationsEnAttente() {
        List<DemandeLocation> demandesEnAttente = demandeLocationService.getDemandesLocationsEnAttente();

        for (DemandeLocation demande : demandesEnAttente) {
            Notification notification = new Notification();
            notification.setTitre("Demande de location en attente.");
            notification.setMessage("La demande de location soumise pour la publication de " + demande.getPublication().getLibelle() + " est toujours en attente");
            notification.setSendTo(String.valueOf(demande.getPublication().getCreerPar()));
            notification.setLu(false);
            notification.setUrl("/demande-location/" + demande.getId());
            notification.setDateNotification(new Date());
            notification.setCreerPar(demande.getCreerPar());
            notification.setCreerLe(new Date());
            notificationService.save(notification);
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void envoyerRappelsDemandesAchatsEnAttente() {
        List<DemandeAchat> demandesEnAttente = demandeAchatService.getDemandesAchatsEnAttente();

        for (DemandeAchat demande : demandesEnAttente) {
            Notification notification = new Notification();
            notification.setTitre("Demande d'achat en attente.");
            notification.setMessage("La demande d'achat soumise pour la publication de " + demande.getPublication().getLibelle() + " est toujours en attente");
            notification.setSendTo(String.valueOf(demande.getPublication().getCreerPar()));
            notification.setLu(false);
            notification.setUrl("/demande-achat/" + demande.getId());
            notification.setDateNotification(new Date());
            notification.setCreerPar(demande.getCreerPar());
            notification.setCreerLe(new Date());
            notificationService.save(notification);
        }
    }
}
