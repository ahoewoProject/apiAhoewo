package com.memoire.apiAhoewo.servicesImpls.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.MotifRejet;
import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeAchat;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes.DemandeAchatRepository;
import com.memoire.apiAhoewo.dto.MotifRejetForm;
import com.memoire.apiAhoewo.services.EmailSenderService;
import com.memoire.apiAhoewo.services.MotifRejetService;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeAchatService;
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
public class DemandeAchatServiceImpl implements DemandeAchatService {
    @Autowired
    private PersonneService personneService;
    @Autowired
    private DemandeAchatRepository demandeAchatRepository;
    @Autowired
    private PublicationService publicationService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MotifRejetService motifRejetService;
    @Autowired
    private Environment env;

    @Override
    public Page<DemandeAchat> getDemandesAchats(Principal principal, int numeroDeLaPage, int elementsParPage) {
        Personne personne = personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);

        if (personne.getRole().getCode().equals("ROLE_PROPRIETAIRE") || personne.getRole().getCode().equals("ROLE_RESPONSABLE") ||
                personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") || personne.getRole().getCode().equals("ROLE_DEMARCHEUR") ||
                personne.getRole().getCode().equals("ROLE_GERANT")) {
            List<Publication> publicationList = publicationService.getPublications(principal);
            return demandeAchatRepository.findByPublicationInOrderByIdDesc(publicationList, pageRequest);
        } else {
            return demandeAchatRepository.findByClientOrderByIdDesc((Client) personne, pageRequest);
        }
    }

    @Override
    public List<DemandeAchat> getDemandesAchats(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        if (personne.getRole().getCode().equals("ROLE_PROPRIETAIRE") || personne.getRole().getCode().equals("ROLE_RESPONSABLE") ||
                personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") || personne.getRole().getCode().equals("ROLE_DEMARCHEUR") ||
                personne.getRole().getCode().equals("ROLE_GERANT")) {
            List<Publication> publicationList = publicationService.getPublications(principal);
            return demandeAchatRepository.findByPublicationInOrderByIdDesc(publicationList);
        } else {
            return demandeAchatRepository.findByClientOrderByIdDesc((Client) personne);
        }
    }

    @Override
    public List<DemandeAchat> getDemandesAchatsEnAttente() {
        return demandeAchatRepository.findByEtatDemande(0);
    }

    @Override
    public DemandeAchat findById(Long id) {
        return demandeAchatRepository.findById(id).orElse(null);
    }

    @Override
    public DemandeAchat soumettre(DemandeAchat demandeAchat, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        demandeAchat.setCodeDemande("DEACH" + UUID.randomUUID());
        demandeAchat.setDateDemande(new Date());
        demandeAchat.setEtatDemande(0);
        demandeAchat.setCreerLe(new Date());
        demandeAchat.setCreerPar(personne.getCreerPar());
        demandeAchat.setStatut(true);

        DemandeAchat demandeAchatAdd = demandeAchatRepository.save(demandeAchat);

        Notification notification = new Notification();
        notification.setTitre("Nouvelle demande d'achat");
        notification.setMessage("Une demande d'achat a été soumise pour la publication de " + demandeAchatAdd.getPublication().getLibelle());
        notification.setSendTo(String.valueOf(demandeAchatAdd.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-achats/" + demandeAchatAdd.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        demandeAchatAdd.setCodeDemande("DEACH" + demandeAchatAdd.getId());
        return demandeAchatRepository.save(demandeAchatAdd);
    }

    @Override
    public DemandeAchat modifier(DemandeAchat demandeAchat, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        demandeAchat.setEtatDemande(4);
        demandeAchat.setModifierLe(new Date());
        demandeAchat.setModifierPar(personne.getId());

        Notification notification = new Notification();
        notification.setTitre("Modification d'une demande d'achat");
        notification.setMessage("La demande d'achat " + demandeAchat.getCodeDemande() + " a été modifiée pour la publication de " + demandeAchat.getPublication().getLibelle());
        notification.setSendTo(String.valueOf(demandeAchat.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-achats/" + demandeAchat.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        return demandeAchatRepository.save(demandeAchat);
    }

    @Override
    public void valider(Long id, Principal principal) {
        DemandeAchat demandeAchat = demandeAchatRepository.findById(id).orElse(null);
        demandeAchat.setEtatDemande(1);

        Personne personne = personneService.findByUsername(principal.getName());

        String demandeAchatLink = "";

        Notification notification = new Notification();
        notification.setTitre("Demande d'achat " + demandeAchat.getCodeDemande() + " validée");
        notification.setMessage("La demande d'achat soumise pour la publication de " + demandeAchat.getPublication().getLibelle() + " a été validée.");
        notification.setSendTo(String.valueOf(demandeAchat.getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-achats/" + demandeAchat.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        demandeAchatLink = "http://localhost:4200/client/demandes-achats/" + demandeAchat.getId();

        String contenu = "Bonjour M./Mlle " + demandeAchat.getClient().getNom() + " " + demandeAchat.getClient().getPrenom() + ",\n" +
                "Votre demande d'achat pour la publication de " + demandeAchat.getPublication().getLibelle() + " a été validée.\n" +
                "Vous pouvez consulter les détails de la demande en cliquant sur le lien suivant : " + demandeAchatLink + "\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), demandeAchat.getClient().getEmail(), "Demande d'achat validée", contenu);
        });

        demandeAchatRepository.save(demandeAchat);
    }

    @Override
    public void relancer(Long id, Principal principal) {
        DemandeAchat demandeAchat = demandeAchatRepository.findById(id).orElse(null);

        Personne personne = personneService.findByUsername(principal.getName());

        Notification notification = new Notification();
        notification.setTitre("Relance de demande d'achat");
        notification.setMessage("La demande d'achat " + demandeAchat.getCodeDemande() + " soumise pour la publication de " + demandeAchat.getPublication().getLibelle() + " a été relancée.");
        notification.setSendTo(String.valueOf(demandeAchat.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-achats/" + demandeAchat.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);
    }

    @Override
    public void annuler(Long id, MotifRejetForm motifRejetForm, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        DemandeAchat demandeAchat = demandeAchatRepository.findById(id).orElse(null);
        demandeAchat.setEtatDemande(2);
        demandeAchat.setAnnulerLe(new Date());
        demandeAchat.setAnnulerPar(personne.getId());

        Notification notification = new Notification();
        notification.setTitre("Demande d'achat " + demandeAchat.getCodeDemande() + " annulée");
        notification.setMessage("La demande d'achat soumise pour la publication de " + demandeAchat.getPublication().getLibelle() + " a été annulée.");
        notification.setSendTo(String.valueOf(demandeAchat.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-achats/" + demandeAchat.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (motifRejetForm != null) {
            MotifRejet motifRejet = new MotifRejet();
            motifRejet.setCode(demandeAchat.getCodeDemande());
            motifRejet.setMotif(motifRejetForm.getMotif());
            motifRejetService.save(motifRejet, principal);
        }

        demandeAchatRepository.save(demandeAchat);
    }

    @Override
    public void refuser(Long id, MotifRejetForm motifRejetForm, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        DemandeAchat demandeAchat = demandeAchatRepository.findById(id).orElse(null);
        demandeAchat.setEtatDemande(3);
        demandeAchat.setRefuserLe(new Date());
        demandeAchat.setRefuserPar(personne.getId());

        String demandeAchatLink = "";

        Notification notification = new Notification();
        notification.setTitre("Demande d'achat " + demandeAchat.getCodeDemande() + " refusée");
        notification.setMessage("La demande d'achat soumise pour la publication de " + demandeAchat.getPublication().getLibelle() + " a été refusée.");
        notification.setSendTo(String.valueOf(demandeAchat.getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-achats/" + demandeAchat.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (motifRejetForm != null) {
            MotifRejet motifRejet = new MotifRejet();
            motifRejet.setCode(demandeAchat.getCodeDemande());
            motifRejet.setMotif(motifRejetForm.getMotif());
            motifRejetService.save(motifRejet, principal);
        }

        String contenu = "Bonjour M./Mlle " + demandeAchat.getClient().getNom() + " " + demandeAchat.getClient().getPrenom() + ",\n" +
                "Votre demande d'achat pour la publication de " + demandeAchat.getPublication().getLibelle() + " a été refusée.\n" +
                "Vous pouvez consulter les détails de la demande en cliquant sur le lien suivant : " + demandeAchatLink + "\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), demandeAchat.getClient().getEmail(), "Demande d'achat refusée", contenu);
                }
        );

        demandeAchatRepository.save(demandeAchat);
    }

    @Override
    public boolean clientAndPublicationExist(Client client, Publication publication) {
        return demandeAchatRepository.existsByClientAndPublication(client, publication);
    }
}
