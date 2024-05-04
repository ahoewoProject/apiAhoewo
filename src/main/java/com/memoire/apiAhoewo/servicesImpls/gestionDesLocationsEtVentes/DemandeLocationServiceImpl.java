package com.memoire.apiAhoewo.servicesImpls.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.MotifRejet;
import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeLocation;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes.DemandeLocationRepository;
import com.memoire.apiAhoewo.dto.MotifRejetForm;
import com.memoire.apiAhoewo.services.EmailSenderService;
import com.memoire.apiAhoewo.services.MotifRejetService;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeLocationService;
import com.memoire.apiAhoewo.services.gestionDesPublications.PublicationService;
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
public class DemandeLocationServiceImpl implements DemandeLocationService {
    @Autowired
    private PersonneService personneService;
    @Autowired
    private DemandeLocationRepository demandeLocationRepository;
    @Autowired
    private PublicationService publicationService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private Environment env;
    @Autowired
    private MotifRejetService motifRejetService;

    @Override
    public Page<DemandeLocation> getDemandesLocations(Principal principal, int numeroDeLaPage, int elementsParPage) {
        Personne personne = personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);

        if (personne.getRole().getCode().equals("ROLE_PROPRIETAIRE") || personne.getRole().getCode().equals("ROLE_RESPONSABLE") ||
                personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") || personne.getRole().getCode().equals("ROLE_DEMARCHEUR") ||
                personne.getRole().getCode().equals("ROLE_GERANT")) {
            List<Publication> publicationList = publicationService.getPublications(principal);
            return demandeLocationRepository.findByPublicationInOrderByIdDesc(publicationList, pageRequest);
        } else {
            return demandeLocationRepository.findByClientOrderByIdDesc((Client) personne, pageRequest);
        }
    }

    @Override
    public List<DemandeLocation> getDemandesLocations(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        if (personne.getRole().getCode().equals("ROLE_PROPRIETAIRE") || personne.getRole().getCode().equals("ROLE_RESPONSABLE") ||
                personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") || personne.getRole().getCode().equals("ROLE_DEMARCHEUR") ||
                personne.getRole().getCode().equals("ROLE_GERANT")) {
            List<Publication> publicationList = publicationService.getPublications(principal);
            return demandeLocationRepository.findByPublicationInOrderByIdDesc(publicationList);
        } else {
            return demandeLocationRepository.findByClientOrderByIdDesc((Client) personne);
        }
    }

    @Override
    public List<DemandeLocation> getDemandesLocationsEnAttente() {
        return demandeLocationRepository.findByEtatDemande(0);
    }

    @Override
    public DemandeLocation findById(Long id) {
        return demandeLocationRepository.findById(id).orElse(null);
    }

    @Override
    public DemandeLocation soumettre(DemandeLocation demandeLocation, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        demandeLocation.setCodeDemande("DELOC" + UUID.randomUUID());
        demandeLocation.setDateDemande(new Date());
        demandeLocation.setEtatDemande(0);
        demandeLocation.setCreerLe(new Date());
        demandeLocation.setCreerPar(personne.getId());
        demandeLocation.setStatut(true);

        DemandeLocation demandeLocationAdd = demandeLocationRepository.save(demandeLocation);

        Notification notification = new Notification();
        notification.setTitre("Nouvelle demande de location");
        notification.setMessage("Une demande de location a été soumise pour la publication de " + demandeLocation.getPublication().getLibelle());
        notification.setSendTo(String.valueOf(demandeLocation.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-locations/" + demandeLocationAdd.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        demandeLocationAdd.setCodeDemande("DELOC00" + demandeLocationAdd.getId());
        return demandeLocationRepository.save(demandeLocationAdd);
    }

    @Override
    public DemandeLocation modifier(DemandeLocation demandeLocation, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        demandeLocation.setEtatDemande(4);
        demandeLocation.setModifierLe(new Date());
        demandeLocation.setModifierPar(personne.getId());

        Notification notification = new Notification();
        notification.setTitre("Modification d'une demande de location");
        notification.setMessage("La demande de location " + demandeLocation.getCodeDemande() + " soumise pour la publication de " + demandeLocation.getPublication().getLibelle() + " a été modifiée.");
        notification.setSendTo(String.valueOf(demandeLocation.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-locations/" + demandeLocation.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        return demandeLocationRepository.save(demandeLocation);
    }

    @Override
    public void relancer(Long id, Principal principal) {
        DemandeLocation demandeLocation = demandeLocationRepository.findById(id).orElse(null);

        Personne personne = personneService.findByUsername(principal.getName());

        Notification notification = new Notification();
        notification.setTitre("Relance de demande de location");
        notification.setMessage("La demande de location " + demandeLocation.getCodeDemande() + " soumise pour la publication de " + demandeLocation.getPublication().getLibelle() + " a été relancée.");
        notification.setSendTo(String.valueOf(demandeLocation.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-locations/" + demandeLocation.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

    }

    @Override
    public void valider(Long id, Principal principal) {
        DemandeLocation demandeLocation = demandeLocationRepository.findById(id).orElse(null);
        demandeLocation.setEtatDemande(1);
        demandeLocationRepository.save(demandeLocation);

        Personne personne = personneService.findByUsername(principal.getName());

        String demandeLocationLink = "";

        Notification notification = new Notification();
        notification.setTitre("Demande de location " + demandeLocation.getCodeDemande() + " validée");
        notification.setMessage("La demande de location soumise pour la publication de " + demandeLocation.getPublication().getLibelle() + " a été validée.");
        notification.setSendTo(String.valueOf(demandeLocation.getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-locations/" + demandeLocation.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        demandeLocationLink = "http://localhost:4200/client/demandes-locations/" + demandeLocation.getId();

        String contenu = "Bonjour M./Mlle " + demandeLocation.getClient().getNom() + " " + demandeLocation.getClient().getPrenom() + ",\n" +
                "Votre demande de location pour la publication de " + demandeLocation.getPublication().getLibelle() + " a été validée.\n" +
                "Vous pouvez consulter les détails de la demande en cliquant sur le lien suivant : " + demandeLocationLink + "\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), demandeLocation.getClient().getEmail(), "Demande de location validée", contenu);
        });

        demandeLocationRepository.save(demandeLocation);
    }

    @Override
    public void annuler(Long id, MotifRejetForm motifRejetForm, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        DemandeLocation demandeLocation = demandeLocationRepository.findById(id).orElse(null);
        demandeLocation.setEtatDemande(2);
        demandeLocation.setAnnulerLe(new Date());
        demandeLocation.setAnnulerPar(personne.getId());

        Notification notification = new Notification();
        notification.setTitre("Demande de location " + demandeLocation.getCodeDemande() + " annulée");
        notification.setMessage("La demande de location soumise pour la publication de " + demandeLocation.getPublication().getLibelle() + " a été annulée.");
        notification.setSendTo(String.valueOf(demandeLocation.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-locations/" + demandeLocation.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (motifRejetForm != null) {
            MotifRejet motifRejet = new MotifRejet();
            motifRejet.setCode(demandeLocation.getCodeDemande());
            motifRejet.setMotif(motifRejetForm.getMotif());
            motifRejetService.save(motifRejet, principal);
        }

        demandeLocationRepository.save(demandeLocation);
    }

    @Override
    public void refuser(Long id, MotifRejetForm motifRejetForm, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        DemandeLocation demandeLocation = demandeLocationRepository.findById(id).orElse(null);
        demandeLocation.setEtatDemande(3);
        demandeLocation.setRefuserLe(new Date());
        demandeLocation.setRefuserPar(personne.getId());

        String demandeLocationLink = "";

        Notification notification = new Notification();
        notification.setTitre("Demande de location " + demandeLocation.getCodeDemande() + " refusée");
        notification.setMessage("La demande de location soumise pour la publication de " + demandeLocation.getPublication().getLibelle() + " a été refusée.");
        notification.setSendTo(String.valueOf(demandeLocation.getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-locations/" + demandeLocation.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (motifRejetForm != null) {
            MotifRejet motifRejet = new MotifRejet();
            motifRejet.setCode(demandeLocation.getCodeDemande());
            motifRejet.setMotif(motifRejetForm.getMotif());
            motifRejetService.save(motifRejet, principal);
        }

        String contenu = "Bonjour M./Mlle " + demandeLocation.getClient().getNom() + " " + demandeLocation.getClient().getPrenom() + ",\n" +
                "Votre demande de location pour la publication de " + demandeLocation.getPublication().getLibelle() + " a été refusée.\n" +
                "Vous pouvez consulter les détails de la demande en cliquant sur le lien suivant : " + demandeLocationLink + "\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), demandeLocation.getClient().getEmail(), "Demande de location refusée", contenu);
                }
        );

        demandeLocationRepository.save(demandeLocation);
    }

    @Override
    public boolean clientAndPublicationExist(Client client, Publication publication) {
        return demandeLocationRepository.existsByClientAndPublication(client, publication);
    }
}
