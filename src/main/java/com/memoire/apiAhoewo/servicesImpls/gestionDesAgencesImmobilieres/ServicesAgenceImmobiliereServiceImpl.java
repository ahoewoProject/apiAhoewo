package com.memoire.apiAhoewo.servicesImpls.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliereRepository;
import com.memoire.apiAhoewo.dto.ServiceNonTrouveForm;
import com.memoire.apiAhoewo.services.EmailSenderService;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliereService;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.ServicesService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ServicesAgenceImmobiliereServiceImpl implements ServicesAgenceImmobiliereService {
    @Autowired
    private ServicesAgenceImmobiliereRepository servicesAgenceImmobiliereRepository;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;
    @Autowired
    private ServicesService servicesService;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private Environment env;

    @Override
    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(Principal principal, int numeroDeLaPage, int elementsParPage) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByResponsable(principal);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return servicesAgenceImmobiliereRepository.findAllByAgenceImmobiliereInAndEtatInOrderByCreerLeDesc(agenceImmobilieres, pageRequest, List.of(0,1,2));
    }

    @Override
    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(Long id, int numeroDeLaPage, int elementsParPage) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findById(id);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return servicesAgenceImmobiliereRepository.findAllByAgenceImmobiliereAndEtatInOrderByCreerLeDesc(agenceImmobiliere, pageRequest, List.of(0,1,2));
    }

    @Override
    public Page<ServicesAgenceImmobiliere> getServicesByNomAgence(String nomAgence, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return servicesAgenceImmobiliereRepository.findByAgenceImmobiliere_NomAgenceAndEtatOrderByIdDesc(nomAgence, 1, pageRequest);
    }

    @Override
    public ServicesAgenceImmobiliere findById(Long id) {
        return servicesAgenceImmobiliereRepository.findById(id).orElse(null);
    }

    @Override
    public ServicesAgenceImmobiliere findByServices(Services services) {
        return servicesAgenceImmobiliereRepository.findByServices(services);
    }


    @Override
    public ServicesAgenceImmobiliere save(ServicesAgenceImmobiliere servicesAgenceImmobiliere, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        servicesAgenceImmobiliere.setEtat(1);
        servicesAgenceImmobiliere.setCreerLe(new Date());
        servicesAgenceImmobiliere.setCreerPar(personne.getId());
        servicesAgenceImmobiliere.setStatut(true);
        return servicesAgenceImmobiliereRepository.save(servicesAgenceImmobiliere);
    }

    @Override
    public void demandeAjoutServiceNonTrouve(Principal principal, ServiceNonTrouveForm serviceNonTrouveForm) {
        Personne personne = personneService.findByUsername(principal.getName());
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findByNomAgence(serviceNonTrouveForm.getNomAgence());

        Services services = new Services();
        services.setNomService(serviceNonTrouveForm.getNomDuService());
        services.setDescription(serviceNonTrouveForm.getDescriptionDuService());
        services.setEtat(0);
        services = servicesService.save(services, principal);

        ServicesAgenceImmobiliere servicesAgenceImmobiliere = new ServicesAgenceImmobiliere();
        servicesAgenceImmobiliere.setAgenceImmobiliere(agenceImmobiliere);
        servicesAgenceImmobiliere.setServices(services);
        servicesAgenceImmobiliere.setEtat(0);
        servicesAgenceImmobiliere.setCreerLe(new Date());
        servicesAgenceImmobiliere.setCreerPar(personne.getId());
        servicesAgenceImmobiliere.setStatut(true);
        servicesAgenceImmobiliereRepository.save(servicesAgenceImmobiliere);

        Notification notification = new Notification();

        notification.setTitre("Demande d'ajout de nouveau service");
        notification.setMessage("Demande d'ajout de service pour " + agenceImmobiliere.getNomAgence());
        notification.setSendTo("ADMIN");
        notification.setLu(false);
        notification.setDateNotification(new Date());
        notification.setUrl("/autres-services/" + services.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        String contenu = "Bonjour Le Support Technique ahoewo,\n\n" +
                "Nous souhaitons vous informer qu'un service que nous désirons ajouter à notre agence " + agenceImmobiliere.getNomAgence() + " n'est pas actuellement disponible dans la liste des services de la plateforme.\n\n" +
                "Détails du service non disponible :\n" +
                "Nom du service : " + serviceNonTrouveForm.getNomDuService() + "\n" +
                "Description : " + serviceNonTrouveForm.getDescriptionDuService() + "\n\n" +
                "Nous vous prions de bien vouloir examiner cette demande et prendre les mesures nécessaires pour évaluer l'ajout de ce service à la plateforme.\n\n" +
                "Nous vous remercions par avance pour votre attention à cette requête. Si vous avez besoin de plus amples informations ou si vous avez des questions, n'hésitez pas à nous contacter.\n\n" +
                "Cordialement,\n\n" +
                "Responsable de l'agence : " + personne.getNom() + " " + personne.getPrenom() + "\n" +
                "Nom de l'agence : " + agenceImmobiliere.getNomAgence() + "\n" +
                "Email de l'agence : " + agenceImmobiliere.getAdresseEmail() + ".";

        // Envoyer l'email au support technique
        CompletableFuture.runAsync(()->{
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.username"), "Demande d'ajout de nouveau service - Action requise", contenu);
        });
    }


    @Override
    public ServicesAgenceImmobiliere update(ServicesAgenceImmobiliere servicesAgenceImmobiliere, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        servicesAgenceImmobiliere.setModifierLe(new Date());
        servicesAgenceImmobiliere.setModifierPar(personne.getId());
        return servicesAgenceImmobiliereRepository.save(servicesAgenceImmobiliere);
    }

    @Override
    public boolean servicesAndAgenceImmobiliereExists(Services services, AgenceImmobiliere agenceImmobiliere) {
        return servicesAgenceImmobiliereRepository.existsByServicesAndAgenceImmobiliere(services, agenceImmobiliere);
    }

    @Override
    public void activerServiceAgence(Long id) {
        ServicesAgenceImmobiliere servicesAgenceImmobiliere = servicesAgenceImmobiliereRepository.findById(id).orElse(null);
        servicesAgenceImmobiliere.setEtat(1);
        servicesAgenceImmobiliereRepository.save(servicesAgenceImmobiliere);
    }

    @Override
    public void desactiverServiceAgence(Long id) {
        ServicesAgenceImmobiliere servicesAgenceImmobiliere = servicesAgenceImmobiliereRepository.findById(id).orElse(null);
        servicesAgenceImmobiliere.setEtat(2);
        servicesAgenceImmobiliereRepository.save(servicesAgenceImmobiliere);
    }
}
