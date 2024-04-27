package com.memoire.apiAhoewo.controllers;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/notifications/non-lues/admin", method = RequestMethod.GET)
    public List<Notification> getNotificationsNonLuesByAdmin() {

        try {
            return this.notificationService.getNotificationsNonLuesByAdmin();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des notifications.", e);
        }
    }

    @RequestMapping(value = "/notifications/non-lues/notaire", method = RequestMethod.GET)
    public List<Notification> getNotificationsNonLuesByNotaire() {

        try {
            return this.notificationService.getNotificationsNonLuesByNotaire();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des notifications.", e);
        }
    }

    @RequestMapping(value = "/notifications/non-lues/owner", method = RequestMethod.GET)
    public List<Notification> getNotificationsNonLuesByOwner(Principal principal) {

        try {
            return this.notificationService.getNotificationsNonLuesByOwner(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des notifications.", e);
        }
    }

    @RequestMapping(value = "/notifications/admin", method = RequestMethod.GET)
    public Page<Notification> getNotificationsByAdmin(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.notificationService.getNotificationsByAdmin(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des notifications.", e);
        }
    }

    @RequestMapping(value = "/notifications/notaire", method = RequestMethod.GET)
    public Page<Notification> getNotificationsByNotaire(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.notificationService.getNotificationsByNotaire(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des notifications.", e);
        }
    }

    @RequestMapping(value = "/notifications/owner", method = RequestMethod.GET)
    public Page<Notification> getNotificationsByOwner(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage,
            Principal principal) {

        try {
            return this.notificationService.getNotificationsByOwner(numeroDeLaPage, elementsParPage, principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des notifications.", e);
        }
    }

    @RequestMapping(value = "/notification/{id}", method = RequestMethod.GET)
    public Notification findById(@PathVariable Long id) {

        Notification notification = new Notification();
        try {
            notification = this.notificationService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return notification;
    }

    @RequestMapping(value = "/envoyer-notification", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> envoyerNotification(@RequestBody Notification notification) {

        try {
            notification = this.notificationService.save(notification);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'envoi du notification : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/lire/notification/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void lireNotification(@PathVariable Long id){
        this.notificationService.lireNotification(id);
    }

    @RequestMapping(value = "/supprimer/notification/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void supprimerNotification(@PathVariable Long id){
        this.notificationService.deleteById(id);
    }
}
