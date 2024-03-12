package com.memoire.apiAhoewo.service;

import com.memoire.apiAhoewo.requestForm.ContactezNousForm;

public interface EmailSenderService {
    void sendMail(String expediteur, String destinataire, String sujet, String contenu);

    void contactezNous(ContactezNousForm contactezNousForm);
}
