package com.memoire.apiAhoewo.controllers;

import com.memoire.apiAhoewo.dto.ContactezNousForm;
import com.memoire.apiAhoewo.services.EmailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContactezNousController {
    private final EmailSenderService emailSenderService;

    public ContactezNousController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @RequestMapping(value = "/contactez-nous", method = RequestMethod.POST)
    public ResponseEntity<?> contactezNous(@RequestBody ContactezNousForm contactezNousForm) {
         try {
             this.emailSenderService.contactezNous(contactezNousForm);
         } catch (Exception e) {
             // TODO: handle exception
             System.out.println("Erreur " + e.getMessage());
             throw new RuntimeException("Une erreur s'est produite lors de la récupération des notifications.", e);
         }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Votre message a été envoyé avec succès.");

        return ResponseEntity.ok(response);
     }

}
