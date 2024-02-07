package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.Notification;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.ServicesRepository;
import com.memoire.apiAhoewo.requestForm.MotifRejetServiceForm;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.NotificationService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.ServicesService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ServicesServiceImpl implements ServicesService {
    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ServicesAgenceImmobiliereService servicesAgenceImmobiliereService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private Environment env;

    @Override
    public List<Services> getAll() {
        return servicesRepository.findAll();
    }

    @Override
    public Page<Services> getServices(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return servicesRepository.findAllByEtatInOrderByCreerLeDesc(pageRequest, List.of(1, 2));
    }

    @Override
    public Page<Services> getAutresServices(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return servicesRepository.findAllByEtatInOrderByCreerLeDesc(pageRequest, List.of(0, 3));
    }

    @Override
    public List<Services> servicesActifs() {
        return servicesRepository.findByEtat(1);
    }

    @Override
    public Services findById(Long id) {
        return servicesRepository.findById(id).orElse(null);
    }

    @Override
    public Services findByNomService(String nomService) {
        return servicesRepository.findByNomService(nomService);
    }

    @Override
    public Services save(Services services, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        services.setCodeService("SERVI" + UUID.randomUUID());
        services.setCreerLe(new Date());
        services.setCreerPar(personne.getId());
        services.setStatut(true);
        Services servicesInsere = servicesRepository.save(services);
        servicesInsere.setCodeService("SERVI00" + servicesInsere.getId());
        return servicesRepository.save(servicesInsere);
    }

    @Override
    public Services update(Services services, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        services.setModifierLe(new Date());
        services.setModifierPar(personne.getId());
        return servicesRepository.save(services);
    }

    @Override
    public void activerServices(Long id) {
        Services services = servicesRepository.findById(id).orElse(null);
        services.setEtat(1);
        servicesRepository.save(services);
    }

    @Override
    public void desactiverServices(Long id) {
        Services services = servicesRepository.findById(id).orElse(null);
        services.setEtat(2);
        servicesRepository.save(services);
    }

    @Override
    public void validerServices(Long id, Principal principal) {
        Services services = servicesRepository.findById(id).orElse(null);
        services.setCodeService("SERVI00" + services.getId());
        services.setEtat(1);
        servicesRepository.save(services);

        ServicesAgenceImmobiliere servicesAgenceImmobiliere = servicesAgenceImmobiliereService.findByServices(services);
        servicesAgenceImmobiliere.setEtat(1);
        servicesAgenceImmobiliereService.update(servicesAgenceImmobiliere, principal);

        Personne personne = personneService.findByUsername(principal.getName());

        Personne personneSave = personneService.findById(services.getCreerPar());

        Notification notification = new Notification();
        notification.setTitre("Nouveau service validé");
        notification.setMessage("La demande d'ajout du nouveau service à été validé.");
        notification.setSendTo(String.valueOf(services.getCreerPar()));
        notification.setLu(false);
        notification.setDateNotification(new Date());
        notification.setUrl("/agences-immobilieres/services");
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notification.setStatut(true);
        notificationService.save(notification);

        String contenu1 = "Bonjour " + personneSave.getPrenom() + " " + personneSave.getNom() + ",\n\n" +
                "Nous sommes heureux de vous informer que la demande de nouveau service pour votre agence immobilière a été validée.\n\n" +
                "Détails du service :\n" +
                "Nom du service : " + services.getNomService() + "\n" +
                "Description : " + services.getDescription() + "\n\n" +
                "Vous pouvez désormais accéder à ce service via la plateforme.\n\n" +
                "Si vous avez des questions ou avez besoin de plus d'informations, n'hésitez pas à nous contacter.\n\n" +
                "Cordialement,\n\n" +
                "L'équipe support technique - ahoewo !";

        String contenu2 = "Bonjour " + servicesAgenceImmobiliere.getAgenceImmobiliere().getNomAgence() + ",\n\n" +
                "Nous sommes heureux de vous informer que la demande de nouveau service pour votre agence immobilière a été validée.\n\n" +
                "Détails du service :\n" +
                "Nom du service : " + services.getNomService() + "\n" +
                "Description : " + services.getDescription() + "\n\n" +
                "Vous pouvez désormais accéder à ce service via la plateforme.\n\n" +
                "Si vous avez des questions ou avez besoin de plus d'informations, n'hésitez pas à nous contacter.\n\n" +
                "Cordialement,\n\n" +
                "L'équipe support technique - ahoewo !";

        CompletableFuture.runAsync(()->{
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), personne.getEmail(), "Demande d'ajout de nouveau service validé", contenu1);
        });

        CompletableFuture.runAsync(()->{
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), servicesAgenceImmobiliere.getAgenceImmobiliere().getAdresseEmail(), "Demande d'ajout de nouveau service validé", contenu2);
        });

        servicesRepository.save(services);
    }

    @Override
    public void rejeterServices(MotifRejetServiceForm motifRejetServiceForm, Principal principal) {
        Services services = servicesRepository.findById(motifRejetServiceForm.getId()).orElse(null);
        services.setEtat(3);

        ServicesAgenceImmobiliere servicesAgenceImmobiliere = servicesAgenceImmobiliereService.findByServices(services);
        servicesAgenceImmobiliere.setEtat(3);
        servicesAgenceImmobiliereService.update(servicesAgenceImmobiliere, principal);

        Personne personne = personneService.findByUsername(principal.getName());

        Personne personneSave = personneService.findById(services.getCreerPar());

        Notification notification = new Notification();
        notification.setTitre("Nouveau service rejeté");
        notification.setMessage("La demande d'ajout du nouveau service à été rejeté.");
        notification.setSendTo(String.valueOf(services.getCreerPar()));
        notification.setLu(false);
        notification.setDateNotification(new Date());
        notification.setUrl("/agences-immobilieres/services");
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notification.setStatut(true);
        notificationService.save(notification);

        String contenu1 = "Bonjour " + personneSave.getPrenom() + " " + personneSave.getNom() + ",\n\n" +
                "Nous sommes désolés de vous informer que la demande de nouveau service pour votre agence immobilière a été rejetée.\n\n" +
                "Détails du service :\n" +
                "Nom du service : " + services.getNomService() + "\n" +
                "Description : " + services.getDescription() + "\n\n" +
                "Motif du rejet : " + motifRejetServiceForm.getMotifRejet() + "\n\n" +
                "Si vous avez des questions ou avez besoin de plus d'informations, n'hésitez pas à nous contacter.\n\n" +
                "Cordialement,\n\n" +
                "L'équipe support technique - ahoewo !";

        String contenu2 = "Bonjour " + servicesAgenceImmobiliere.getAgenceImmobiliere().getNomAgence() + ",\n\n" +
                "Nous sommes désolés de vous informer que la demande de nouveau service pour votre agence immobilière a été rejetée.\n\n" +
                "Détails du service :\n" +
                "Nom du service : " + services.getNomService() + "\n" +
                "Description : " + services.getDescription() + "\n\n" +
                "Motif du rejet : " + motifRejetServiceForm.getMotifRejet() + "\n\n" +
                "Si vous avez des questions ou avez besoin de plus d'informations, n'hésitez pas à nous contacter.\n\n" +
                "Cordialement,\n\n" +
                "L'équipe support technique - ahoewo !";

        CompletableFuture.runAsync(()->{
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), personne.getEmail(), "Demande d'ajout de nouveau service rejeté", contenu1);
        });

        CompletableFuture.runAsync(()->{
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), servicesAgenceImmobiliere.getAgenceImmobiliere().getAdresseEmail(), "Demande d'ajout de nouveau service rejeté", contenu2);
        });

        servicesRepository.save(services);
    }
}
