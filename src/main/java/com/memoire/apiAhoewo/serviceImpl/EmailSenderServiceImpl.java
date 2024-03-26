package com.memoire.apiAhoewo.serviceImpl;

import com.memoire.apiAhoewo.requestForm.ContactezNousForm;
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
    public void sendMail(String expediteur, String destinataire, String sujet, String contenu) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(expediteur);
            message.setTo(destinataire);
            message.setSubject(sujet);
            message.setText(contenu);
            javaMailSender.send(message);
        } catch (MailException e){
            System.out.println("Erreur lors de l'envoi " + e.getMessage());
        }
    }

    @Override
    public void contactezNous(ContactezNousForm contactezNousForm) {
        String contenu = "Bonjour M./Mlle, Message de contact d'un utilisateur " + "\n" +
                "Nom & Prénoms : " + contactezNousForm.getNomPrenoms() + "\n" +
                "Email: " + contactezNousForm.getEmetteurEmail() + "\n" +
                "Téléphone: " + contactezNousForm.getTelephone() + "\n" +
                "Message: " + contactezNousForm.getMessage() + "\n\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";


        sendMail(env.getProperty("spring.mail.username"), contactezNousForm.getRecepteurEmail(), "IMPORTANT: Nouveau message de contact", contenu);
    }
}

