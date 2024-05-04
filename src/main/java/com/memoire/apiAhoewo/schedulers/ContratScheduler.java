package com.memoire.apiAhoewo.schedulers;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratVente;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.ContratLocationService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.ContratVenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ContratScheduler {
    @Autowired
    private ContratVenteService contratVenteService;
    @Autowired
    private ContratLocationService contratLocationService;
    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void envoyerRappelsContratsVentesEnAttente() {
        List<ContratVente> contratVenteList = contratVenteService.getContratVentesEnAttente();

        for (ContratVente contratVente : contratVenteList) {
            Notification notification1 = new Notification();
            notification1.setTitre("Proposition d'un contrat de vente en attente.");
            notification1.setMessage("Vous avez une proposition de contrat de vente " + contratVente.getCodeContrat() + " en attente.");
            notification1.setSendTo(String.valueOf(contratVente.getDemandeAchat().getClient().getId()));
            notification1.setDateNotification(new Date());
            notification1.setLu(false);
            notification1.setUrl("/contrats/ventes/" + contratVente.getId());
            notification1.setCreerPar(contratVente.getCreerPar());
            notification1.setCreerLe(new Date());
            notificationService.save(notification1);
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void envoyerRappelsContratsLocationsEnAttente() {
        List<ContratLocation> contratLocationList = contratLocationService.getContratLocationsEnAttente();

        for (ContratLocation contratLocation : contratLocationList) {
            Notification notification = new Notification();
            notification.setTitre("Proposition d'un contrat de location en attente.");
            notification.setMessage("Vous avez une proposition de contrat de location " + contratLocation.getCodeContrat() + " en attente.");
            notification.setSendTo(String.valueOf(contratLocation.getDemandeLocation().getClient().getId()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/contrats/locations/" + contratLocation.getId());
            notification.setCreerPar(contratLocation.getCreerPar());
            notification.setCreerLe(new Date());
            notificationService.save(notification);
        }
    }
}
