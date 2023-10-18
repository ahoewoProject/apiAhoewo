package com.memoire.apiAhoewo.service;

public interface EmailSenderService {
    void sendMail(String destinataire, String sujet, String contenu);
}
