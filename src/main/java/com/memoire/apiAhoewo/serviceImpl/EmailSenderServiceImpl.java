package com.memoire.apiAhoewo.serviceImpl;

import com.memoire.apiAhoewo.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Override
    public void sendMail(String destinataire, String sujet, String contenu) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(env.getProperty("spring.mail.username"));
            message.setTo(destinataire);
            message.setSubject(sujet);
            message.setText(contenu);
            javaMailSender.send(message);
        } catch (MailException e){
            System.out.println("Erreur lors de l'envoi " + e.getMessage());
        }
    }
}

