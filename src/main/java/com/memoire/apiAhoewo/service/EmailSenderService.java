package com.memoire.apiAhoewo.service;

public interface EmailSenderService {
    void sendMail(String expediteur, String destinataire, String sujet, String contenu);
}
