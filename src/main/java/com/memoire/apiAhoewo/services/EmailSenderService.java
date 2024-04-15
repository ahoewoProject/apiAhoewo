package com.memoire.apiAhoewo.services;

import com.memoire.apiAhoewo.dto.ContactezNousForm;

public interface EmailSenderService {
    void sendMail(String expediteur, String destinataire, String sujet, String contenu);

    void contactezNous(ContactezNousForm contactezNousForm);
}
