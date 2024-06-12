package com.memoire.apiAhoewo.servicesImpls.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.dto.MotifForm;
import com.memoire.apiAhoewo.models.Motif;
import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeVisite;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes.DemandeVisiteRepository;
import com.memoire.apiAhoewo.services.EmailSenderService;
import com.memoire.apiAhoewo.services.MotifService;
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
    private MotifService motifService;

    @Override
    public Page<DemandeVisite> getDemandesVisites(Principal principal, int numeroDeLaPage, int elementsParPage) {
        Personne personne = personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);

        String roleCode = personne.getRole().getCode();

        if (personneService.estProprietaire(roleCode) || personneService.estResponsable(roleCode) ||
                personneService.estAgentImmobilier(roleCode) || personneService.estDemarcheur(roleCode) ||
                personneService.estGerant(roleCode)) {

            List<Publication> publicationList = publicationService.getPublications(principal);
            return demandeVisiteRepository.findByPublicationInOrderByIdDesc(publicationList, pageRequest);
        } else {
            return demandeVisiteRepository.findByClientOrderByIdDesc((Client) personne, pageRequest);
        }
    }

    @Override
    public List<DemandeVisite> getDemandesVisites(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        String roleCode = personne.getRole().getCode();

        if (personneService.estProprietaire(roleCode) || personneService.estResponsable(roleCode) ||
                personneService.estAgentImmobilier(roleCode) || personneService.estDemarcheur(roleCode) ||
                personneService.estGerant(roleCode)) {

            List<Publication> publicationList = publicationService.getPublications(principal);
            return demandeVisiteRepository.findByPublicationInOrderByIdDesc(publicationList);
        } else {
            return demandeVisiteRepository.findByClientOrderByIdDesc((Client) personne);
        }
    }

    @Override
    public List<DemandeVisite> getDemandesVisitesEnAttente() {
        return demandeVisiteRepository.findByEtatDemande(0);
    }

    @Override
    public List<DemandeVisite> getDemandesVisitesValidees() {
        return demandeVisiteRepository.findByEtatDemande(1);
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
        notification.setUrl("/demande-visite/" + demandeVisiteAdd.getId());
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

        demandeVisite.setModifierLe(new Date());
        demandeVisite.setModifierPar(personne.getId());

        Notification notification = new Notification();
        if (personneService.estClient(personne.getRole().getCode())) {
            demandeVisite.setEtatDemande(4);
            notification.setTitre("Modification d'une demande de visite");
            notification.setMessage("La demande de visite " + demandeVisite.getCodeDemande() + " pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été modifiée.");
            notification.setSendTo(String.valueOf(demandeVisite.getPublication().getCreerPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/demande-visite/" + demandeVisite.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);
        } else {
            demandeVisite.setEtatDemande(5);
            notification.setTitre("Modification d'une demande de visite");
            notification.setMessage("Votre demande de visite " + demandeVisite.getCodeDemande() + " pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été modifiée.");
            notification.setSendTo(String.valueOf(demandeVisite.getCreerPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/demande-visite/" + demandeVisite.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);
        }

        return demandeVisiteRepository.save(demandeVisite);
    }

    @Override
    public void relancer(Long id, Principal principal) {
        DemandeVisite demandeVisite = demandeVisiteRepository.findById(id).orElse(null);

        Personne personne = personneService.findByUsername(principal.getName());

        Notification notification = new Notification();
        notification.setTitre("Relance d'une demande de visite");
        assert demandeVisite != null;
        notification.setMessage("La demande de visite " + demandeVisite.getCodeDemande() + " a été relancée pour la publication de " + demandeVisite.getPublication().getLibelle());
        notification.setSendTo(String.valueOf(demandeVisite.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demande-visite/" + demandeVisite.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);
    }

    @Override
    public void valider(Long id, Principal principal) {
        DemandeVisite demandeVisite = demandeVisiteRepository.findById(id).orElse(null);

        assert demandeVisite != null;
        demandeVisite.setEtatDemande(1);

        Personne personne = personneService.findByUsername(principal.getName());

        String demandeVisiteLink = "";

        Notification notification = new Notification();
        if (personneService.estClient(personne.getRole().getCode())) {
            notification.setTitre("Demande de visite " + demandeVisite.getCodeDemande() + " validée");
            notification.setMessage("La demande de visite soumise pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été validée suite à la modification que vous avez faite.");
            notification.setSendTo(String.valueOf(demandeVisite.getModifierPar()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/demande-visite/" + demandeVisite.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);
        } else {
            notification.setTitre("Demande de visite " + demandeVisite.getCodeDemande() + " validée");
            notification.setMessage("La demande de visite soumise pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été validée.");
            notification.setSendTo(String.valueOf(demandeVisite.getClient().getId()));
            notification.setDateNotification(new Date());
            notification.setLu(false);
            notification.setUrl("/demande-visite/" + demandeVisite.getId());
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notificationService.save(notification);

//            demandeVisiteLink = "http://localhost:4200/client/demande-visite/" + demandeVisite.getId();
//
//            String contenu = "Bonjour M./Mlle " + demandeVisite.getClient().getNom() + " " + demandeVisite.getClient().getPrenom() + ",\n" +
//                    "Votre demande de visite pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été validée.\n" +
//                    "Vous pouvez consulter les détails de la demande en cliquant sur le lien suivant : " + demandeVisiteLink + "\n" +
//                    "Cordialement,\n" +
//                    "L'équipe Ahoewo";
//
//            CompletableFuture.runAsync(() -> {
//                emailSenderService.sendMail(env.getProperty("spring.mail.username"), demandeVisite.getClient().getEmail(), "Demande de visite validée", contenu);
//            });
        }

        demandeVisiteRepository.save(demandeVisite);
    }

    @Override
    public void annuler(Long id, MotifForm motifForm, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        DemandeVisite demandeVisite = demandeVisiteRepository.findById(id).orElse(null);
        assert demandeVisite != null;
        demandeVisite.setEtatDemande(2);
        demandeVisite.setAnnulerLe(new Date());
        demandeVisite.setAnnulerPar(personne.getId());

        Notification notification = new Notification();
        notification.setTitre("Demande de visite " + demandeVisite.getCodeDemande() + " annulée");
        notification.setMessage("La demande de visite soumise pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été annulée.");
        notification.setSendTo(String.valueOf(demandeVisite.getPublication().getCreerPar()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demande-visite/" + demandeVisite.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (motifForm != null) {
            Motif motif = new Motif();
            motif.setCode(demandeVisite.getCodeDemande());
            motif.setLibelle("Motif d'annulation d'une demande de visite");
            motif.setMotif(motifForm.getMotif());
            motifService.save(motif, principal);
        }

        demandeVisiteRepository.save(demandeVisite);
    }

    @Override
    public void refuser(Long id, MotifForm motifForm, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        DemandeVisite demandeVisite = demandeVisiteRepository.findById(id).orElse(null);
        assert demandeVisite != null;
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
        notification.setUrl("/demande-visite/" + demandeVisite.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (motifForm != null) {
            Motif motif = new Motif();
            motif.setCode(demandeVisite.getCodeDemande());
            motif.setLibelle("Motif de refus d'une demande de visite");
            motif.setMotif(motifForm.getMotif());
             motifService.save(motif, principal);
        }

//        String contenu = "Bonjour M./Mlle " + demandeVisite.getClient().getNom() + " " + demandeVisite.getClient().getPrenom() + ",\n" +
//                "Votre demande de visite pour la publication de " + demandeVisite.getPublication().getLibelle() + " a été validée.\n" +
//                "Vous pouvez consulter les détails de la demande en cliquant sur le lien suivant : " + demandeVisiteLink + "\n" +
//                "Cordialement,\n" +
//                "L'équipe Ahoewo";
//
//        CompletableFuture.runAsync(() -> {
//            emailSenderService.sendMail(env.getProperty("spring.mail.username"), demandeVisite.getClient().getEmail(), "Demande de visite refusée", contenu);
//                }
//        );

        demandeVisiteRepository.save(demandeVisite);
    }

    @Override
    public boolean clientAndPublicationExist(Client client, Publication publication) {
        return demandeVisiteRepository.existsByClientAndPublication(client, publication);
    }
}
