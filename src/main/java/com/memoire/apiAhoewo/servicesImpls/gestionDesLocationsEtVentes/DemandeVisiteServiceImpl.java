package com.memoire.apiAhoewo.servicesImpls.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.MotifRejet;
import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeVisite;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes.DemandeVisiteRepository;
import com.memoire.apiAhoewo.dto.MotifRejetForm;
import com.memoire.apiAhoewo.services.EmailSenderService;
import com.memoire.apiAhoewo.services.MotifRejetService;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeVisiteService;
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
public class DemandeVisiteServiceImpl implements DemandeVisiteService {
    @Autowired
    private PersonneService personneService;
    @Autowired
    private DemandeVisiteRepository demandeVisiteRepository;
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
    public Page<DemandeVisite> getDemandesVisites(Principal principal, int numeroDeLaPage, int elementsParPage) {
        Personne personne = personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);



        if (personne.getRole().getCode().equals("ROLE_PROPRIETAIRE") || personne.getRole().getCode().equals("ROLE_RESPONSABLE") ||
                personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") || personne.getRole().getCode().equals("ROLE_DEMARCHEUR") ||
                personne.getRole().getCode().equals("ROLE_GERANT")) {
            List<Publication> publicationList = publicationService.getPublications(principal);
            return demandeVisiteRepository.findByPublicationInOrderByIdDesc(publicationList, pageRequest);
        } else {
            return demandeVisiteRepository.findByClientOrderByIdDesc((Client) personne, pageRequest);
        }
    }

    @Override
    public DemandeVisite findById(Long id) {
        return demandeVisiteRepository.findById(id).orElse(null);
    }

    @Override
    public DemandeVisite soumettre(DemandeVisite demandeVisite, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        demandeVisite.setCodeDemande("DEVIS" + UUID.randomUUID());
        demandeVisite.setDateDemande(new Date());
        demandeVisite.setEtatDemande(0);
        demandeVisite.setCreerLe(new Date());
        demandeVisite.setCreerPar(personne.getId());
        demandeVisite.setStatut(true);

        DemandeVisite demandeVisiteAdd = demandeVisiteRepository.save(demandeVisite);

        Notification notification = new Notification();
        notification.setTitre("Nouvelle demande de visite");
        notification.setMessage("Une demande de visite a été soumise pour la publication de " + demandeVisite.getPublication().getLibelle());
        notification.setSendTo(String.valueOf(demandeVisite.getPublication().getCreerPar()));
        notification.setLu(false);
        notification.setUrl("/demandes-visites/" + demandeVisiteAdd.getId());
        notification.setDateNotification(new Date());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        demandeVisiteAdd.setCodeDemande("DEVIS00" + demandeVisiteAdd.getId());
        return demandeVisiteRepository.save(demandeVisiteAdd);
    }

    @Override
    public DemandeVisite modifier(DemandeVisite demandeVisite, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        demandeVisite.setEtatDemande(4);
        demandeVisite.setModifierLe(new Date());
        demandeVisite.setModifierPar(personne.getId());

        Notification notification = new Notification();
        notification.setTitre("Modification d'une demande de visite");
        notification.setMessage("La demande de visite " + demandeVisite.getCodeDemande() + " a été modifiée pour la publication de " + demandeVisite.getPublication().getLibelle());
        notification.setSendTo(String.valueOf(demandeVisite.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-visites/" + demandeVisite.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        return demandeVisiteRepository.save(demandeVisite);
    }

    @Override
    public void relancer(Long id, Principal principal) {
        DemandeVisite demandeVisite = demandeVisiteRepository.findById(id).orElse(null);

        Personne personne = personneService.findByUsername(principal.getName());

        Notification notification = new Notification();
        notification.setTitre("Relance d'une demande de visite");
        notification.setMessage("La demande de visite " + demandeVisite.getCodeDemande() + " a été relancée pour la publication de " + demandeVisite.getPublication().getLibelle());
        notification.setSendTo(String.valueOf(demandeVisite.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-visites/" + demandeVisite.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);
    }

    @Override
    public void valider(Long id, Principal principal) {
        DemandeVisite demandeVisite = demandeVisiteRepository.findById(id).orElse(null);
        demandeVisite.setEtatDemande(1);

        Personne personne = personneService.findByUsername(principal.getName());

        String demandeVisiteLink = "";

        Notification notification = new Notification();
        notification.setTitre("Demande de visite " + demandeVisite.getCodeDemande() + " validée");
        notification.setMessage("La demande de visite soumise pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été validée.");
        notification.setSendTo(String.valueOf(demandeVisite.getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-visites/" + demandeVisite.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        demandeVisiteLink = "http://localhost:4200/client/demandes-visites/" + demandeVisite.getId();

        String contenu = "Bonjour M./Mlle " + demandeVisite.getClient().getNom() + " " + demandeVisite.getClient().getPrenom() + ",\n" +
                "Votre demande de visite pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été validée.\n" +
                "Vous pouvez consulter les détails de la demande en cliquant sur le lien suivant : " + demandeVisiteLink + "\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), demandeVisite.getClient().getEmail(), "Demande de visite validée", contenu);
        });

        demandeVisiteRepository.save(demandeVisite);
    }

    @Override
    public void annuler(Long id, MotifRejetForm motifRejetForm, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        DemandeVisite demandeVisite = demandeVisiteRepository.findById(id).orElse(null);
        demandeVisite.setEtatDemande(2);
        demandeVisite.setAnnulerLe(new Date());
        demandeVisite.setAnnulerPar(personne.getId());

        Notification notification = new Notification();
        notification.setTitre("Demande de visite " + demandeVisite.getCodeDemande() + " annulée");
        notification.setMessage("La demande de visite soumise pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été annulée.");
        notification.setSendTo(String.valueOf(demandeVisite.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-visites/" + demandeVisite.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (motifRejetForm != null) {
            MotifRejet motifRejet = new MotifRejet();
            motifRejet.setCode(demandeVisite.getCodeDemande());
            motifRejet.setMotif(motifRejetForm.getMotif());
            motifRejetService.save(motifRejet, principal);
        }

        demandeVisiteRepository.save(demandeVisite);
    }

    @Override
    public void refuser(Long id, MotifRejetForm motifRejetForm, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        DemandeVisite demandeVisite = demandeVisiteRepository.findById(id).orElse(null);
        demandeVisite.setEtatDemande(3);
        demandeVisite.setRefuserLe(new Date());
        demandeVisite.setRefuserPar(personne.getId());

        String demandeVisiteLink = "";

        Notification notification = new Notification();
        notification.setTitre("Demande de visite " + demandeVisite.getCodeDemande() + " refusée");
        notification.setMessage("La demande de visite soumise pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été refusée.");
        notification.setSendTo(String.valueOf(demandeVisite.getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-visites/" + demandeVisite.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (motifRejetForm != null) {
            MotifRejet motifRejet = new MotifRejet();
            motifRejet.setCode(demandeVisite.getCodeDemande());
            motifRejet.setMotif(motifRejetForm.getMotif());
            motifRejetService.save(motifRejet, principal);
        }

        String contenu = "Bonjour M./Mlle " + demandeVisite.getClient().getNom() + " " + demandeVisite.getClient().getPrenom() + ",\n" +
                "Votre demande de visite pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été validée.\n" +
                "Vous pouvez consulter les détails de la demande en cliquant sur le lien suivant : " + demandeVisiteLink + "\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), demandeVisite.getClient().getEmail(), "Demande de visite refusée", contenu);
                }
        );

        demandeVisiteRepository.save(demandeVisite);
    }

    @Override
    public boolean clientAndPublicationExist(Client client, Publication publication) {
        return demandeVisiteRepository.existsByClientAndPublication(client, publication);
    }
}
