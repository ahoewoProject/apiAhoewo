package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.Notification;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.*;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesComptes.Proprietaire;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.DelegationGestionRepository;
import com.memoire.apiAhoewo.requestForm.DelegationGestionForm2;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.NotificationService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AffectationResponsableAgenceService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.*;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class DelegationGestionServiceImpl implements DelegationGestionService {
    @Autowired
    private DelegationGestionRepository delegationGestionRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private BienImmobilierAssocieService bienImmoAssocieService;
    @Autowired
    private ImagesBienImmobilierService imagesBienImmobilierService;;
    @Autowired
    private CaracteristiquesService caracteristiquesService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AffectationResponsableAgenceService affectationResponsableAgenceService;
    @Autowired
    private Environment env;

    @Override
    public Page<DelegationGestion> getDelegationsByProprietairePaginees(Principal principal, int numeroDeLaPage, int elementsParPage) {
        Personne personne = personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return delegationGestionRepository.findAllByBienImmobilier_PersonneOrderByCreerLeDesc(personne, pageRequest);
    }

    @Override
    public Page<DelegationGestion> getDelegationsByGestionnairePaginees(Principal principal, int numeroDeLaPage, int elementsParPage) {
        Personne personne = personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return delegationGestionRepository.findAllByGestionnaireOrderByCreerLeDesc(personne, pageRequest);
    }

    @Override
    public Page<DelegationGestion> getDelegationsOfAgencesByResponsablePaginees(Principal principal, int numeroDeLaPage, int elementsParPage) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByResponsable(principal);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return delegationGestionRepository.findAllByAgenceImmobiliereInOrderByCreerLeDesc(agenceImmobilieres, pageRequest);
    }

    @Override
    public Page<DelegationGestion> getDelegationsOfAgencesByAgentPaginees(Principal principal, int numeroDeLaPage, int elementsParPage) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByAgent(principal);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return delegationGestionRepository.findAllByAgenceImmobiliereInOrderByCreerLeDesc(agenceImmobilieres, pageRequest);
    }

    @Override
    public List<DelegationGestion> getDelegationsByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return delegationGestionRepository.findByBienImmobilier_Personne(personne);
    }

    @Override
    public List<DelegationGestion> getDelegationsByGestionnaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return delegationGestionRepository.findByGestionnaire(personne);
    }

    @Override
    public List<DelegationGestion> getDelegationsOfAgencesByResponsable(Principal principal) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByResponsable(principal);

        return delegationGestionRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public List<DelegationGestion> getDelegationsOfAgencesByAgent(Principal principal) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByAgent(principal);

        return delegationGestionRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public DelegationGestion findById(Long id) {
        return delegationGestionRepository.findById(id).orElse(null);
    }

    @Override
    public DelegationGestion save(DelegationGestion delegationGestion, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        delegationGestion.setCodeDelegationGestion("DELGES" + UUID.randomUUID());
        delegationGestion.setDateDelegation(new Date());
        delegationGestion.setStatutDelegation(0);
        delegationGestion.setCreerLe(new Date());
        delegationGestion.setCreerPar(personne.getId());
        delegationGestion.setStatut(true);
        DelegationGestion delegationGestionAdd = delegationGestionRepository.save(delegationGestion);

        String delegationGestionLink = "";

        if (delegationGestion.getAgenceImmobiliere() == null) {

            Notification notification = new Notification();

            notification.setTitre("Délégation de gestion de bien");
            notification.setMessage("Vous avez reçu une délégation de gestion de bien");
            notification.setSendTo(String.valueOf(delegationGestion.getGestionnaire().getId()));
            notification.setLu(false);
            notification.setDateNotification(new Date());
            notification.setUrl("/delegations-gestions");
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notification.setStatut(true);
            notificationService.save(notification);

            if ("ROLE_DEMARCHEUR".equals(delegationGestion.getGestionnaire().getRole().getCode())) {
                delegationGestionLink = "http://localhost:4200/#/demarcheur/delegations-gestions";
            } else {
                delegationGestionLink = "http://localhost:4200/#/gerant/delegations-gestions";
            }

            String contenu = "Bonjour M./Mlle " + delegationGestion.getGestionnaire().getPrenom() + " " + delegationGestion.getGestionnaire().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que M./Mlle " + personne.getPrenom() + " " + personne.getNom() + " vient de vous déléguer la gestion d'un de ses biens.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";

            CompletableFuture.runAsync(() -> {
                emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getGestionnaire().getEmail(), "Délégation de la gestion de bien", contenu);
            });
        } else {
            List <AffectationResponsableAgence> affectationResponsableAgences = affectationResponsableAgenceService.findByAgenceImmobiliere(delegationGestion.getAgenceImmobiliere());

            for (AffectationResponsableAgence affectationResponsableAgence: affectationResponsableAgences) {

                Notification notification = new Notification();

                notification.setTitre("Délégation de gestion de bien");
                notification.setMessage("Votre agence a reçu une délégation de gestion de bien");
                notification.setSendTo(String.valueOf(affectationResponsableAgence.getResponsableAgenceImmobiliere().getId()));
                notification.setLu(false);
                notification.setDateNotification(new Date());
                notification.setUrl("/agences-immobilieres/delegations-gestions");
                notification.setCreerPar(personne.getId());
                notification.setCreerLe(new Date());
                notification.setStatut(true);
                notificationService.save(notification);
            }

            delegationGestionLink = "http://localhost:4200/#/responsable/agences-immobilieres/delegations-gestions";
            String contenu = "Bonjour Agence immobilière " + delegationGestion.getAgenceImmobiliere().getNomAgence() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que M./Mlle " + personne.getPrenom() + " " + personne.getNom() + " vient de vous déléguer la gestion d'un de ses biens.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";

            CompletableFuture.runAsync(() -> {
                emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getAgenceImmobiliere().getAdresseEmail(), "Délégation de la gestion de bien", contenu);
            });
        }

        delegationGestionAdd.setCodeDelegationGestion("DELGES00" + delegationGestionAdd.getId());
        return delegationGestionRepository.save(delegationGestionAdd);
    }

    @Override
    public DelegationGestion saveDelegationGestion2(DelegationGestionForm2 delegationGestionForm2, Caracteristiques caracteristiques, List<MultipartFile> files, Principal principal) {

        Personne personne = personneService.findByUsername(principal.getName());

        DelegationGestion delegationGestion;
        if (delegationGestionForm2.getTypeDeBien().getDesignation().equals("Terrain")
            || delegationGestionForm2.getTypeDeBien().getDesignation().equals("Maison")
            || delegationGestionForm2.getTypeDeBien().getDesignation().equals("Immeuble")
            || delegationGestionForm2.getTypeDeBien().getDesignation().equals("Villa")) {
            delegationGestion = this.saveDelegationGestionTypeBien(delegationGestionForm2, caracteristiques, files, principal);
        } else  {
            delegationGestion = this.saveDelegationGestionBienImmAssocie(delegationGestionForm2, caracteristiques, files, principal);
        }

        String delegationGestionLink = "http://localhost:4200/#/proprietaire/delegations-gestions";

        Notification notification = new Notification();

        if (delegationGestion.getAgenceImmobiliere() != null) {

            notification.setTitre("Délégation de gestion de bien");
            notification.setMessage("L'agence immobilière du nom de " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " vient d'enregistrer l'un de vos biens que vous lui avez délégué.");
            notification.setSendTo(String.valueOf(delegationGestion.getBienImmobilier().getPersonne().getId()));
            notification.setLu(false);
            notification.setDateNotification(new Date());
            notification.setUrl("/delegations-gestions");
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notification.setStatut(true);
            notificationService.save(notification);

            String contenu = "Bonjour M./Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + " " + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que l'agence immobilière du nom de " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " vient d'enregistrer l'un de vos biens que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";

            CompletableFuture.runAsync(() -> {
                emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getGestionnaire().getEmail(), "Délégation de la gestion de bien", contenu);
            });

        } else {

            notification.setTitre("Délégation de gestion de bien");
            notification.setMessage("M./Mlle " + delegationGestion.getGestionnaire().getPrenom() + " " + delegationGestion.getGestionnaire().getNom() + " vient d'enregistrer l'un de vos biens que vous lui avez délégué.");
            notification.setSendTo(String.valueOf(delegationGestion.getBienImmobilier().getPersonne().getId()));
            notification.setLu(false);
            notification.setDateNotification(new Date());
            notification.setUrl("/delegations-gestions");
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notification.setStatut(true);
            notificationService.save(notification);

            String contenu = "Bonjour M./Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + " " + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que M./Mlle " + delegationGestion.getGestionnaire().getPrenom() + " " + delegationGestion.getGestionnaire().getNom() + " vient d'enregistrer l'un de vos biens que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";

            CompletableFuture.runAsync(() -> {
                emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getAgenceImmobiliere().getAdresseEmail(), "Délégation de la gestion de bien", contenu);
            });

        }

        return delegationGestion;
    }

    @Override
    public DelegationGestion updateDelegationGestionTwo(DelegationGestionForm2 delegationGestionFormTwo, Caracteristiques caracteristiques, List<MultipartFile> files, Principal principal) {
        return null;
    }

    @Override
    public void accepterDelegationGestion(Long id) {
        DelegationGestion delegationGestion = delegationGestionRepository.findById(id).orElse(null);
        List<BienImmAssocie> bienImmAssocies = bienImmoAssocieService.getBiensAssocies(delegationGestion.getBienImmobilier());

        for (BienImmAssocie bienImmAssocie : bienImmAssocies) {
            List<DelegationGestion> delegationGestions = delegationGestionRepository.findByBienImmobilier(bienImmAssocie);

            for (DelegationGestion delegationGestionBienImmAssocie : delegationGestions) {
                if (!delegationGestionBienImmAssocie.getPorteeGestion()) {
                    delegationGestionBienImmAssocie.setStatutDelegation(1);
                    delegationGestionBienImmAssocie.setEtatDelegation(true);
                    delegationGestionRepository.save(delegationGestionBienImmAssocie);
                }
            }
        }
        delegationGestion.setStatutDelegation(1);
        delegationGestion.setEtatDelegation(true);
        delegationGestionRepository.save(delegationGestion);

        String delegationGestionLink = "http://localhost:4200/#/proprietaire/delegations-gestions";
        String contenu = "";

        if (delegationGestion.getAgenceImmobiliere() == null) {

            Notification notification = new Notification();
            notification.setTitre("Acceptation de la délégation de gestion de bien");
            notification.setMessage("M./Mlle " + delegationGestion.getGestionnaire().getPrenom() + " " + delegationGestion.getGestionnaire().getNom() + " vient d'accepter la gestion de votre bien que vous lui avez délégué.");
            notification.setSendTo(String.valueOf(delegationGestion.getBienImmobilier().getPersonne().getId()));
            notification.setLu(false);
            notification.setDateNotification(new Date());
            notification.setUrl("/delegations-gestions");
            notification.setCreerPar(delegationGestion.getGestionnaire().getId());
            notification.setCreerLe(new Date());
            notification.setStatut(true);
            notificationService.save(notification);

             contenu = "Bonjour M./Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + " " + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que M./Mlle " + delegationGestion.getGestionnaire().getPrenom() + " " + delegationGestion.getGestionnaire().getNom() + " vient d'accepter la gestion de votre bien que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";
        } else {

            Notification notification = new Notification();
            notification.setTitre("Acceptation de la délégation de gestion de bien");
            notification.setMessage("L'agence immobilière du nom de " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " vient d'accepter la gestion de votre bien que vous lui avez délégué.");
            notification.setSendTo(String.valueOf(delegationGestion.getBienImmobilier().getPersonne().getId()));
            notification.setLu(false);
            notification.setDateNotification(new Date());
            notification.setUrl("/delegations-gestions");
            notification.setCreerPar(delegationGestion.getAgenceImmobiliere().getId());
            notification.setCreerLe(new Date());
            notification.setStatut(true);
            notificationService.save(notification);

             contenu = "Bonjour M./Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + " " + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que l'agence immobilière du nom de " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " vient d'accepter la gestion de votre bien que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";
        }


        String finalContenu = contenu;
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getGestionnaire().getEmail(), "Acceptation de la délégation de gestion de bien", finalContenu);
        });
    }

    @Override
    public void refuserDelegationGestion(Long id) {
        DelegationGestion delegationGestion = delegationGestionRepository.findById(id).orElse(null);
        List<BienImmAssocie> bienImmAssocies = bienImmoAssocieService.getBiensAssocies(delegationGestion.getBienImmobilier());

        for (BienImmAssocie bienImmAssocie : bienImmAssocies) {
            List<DelegationGestion> delegationGestions = delegationGestionRepository.findByBienImmobilier(bienImmAssocie);

            for (DelegationGestion delegationGestionBienImmAssocie : delegationGestions) {
                if (!delegationGestionBienImmAssocie.getPorteeGestion()) {
                    delegationGestionBienImmAssocie.setStatutDelegation(2);
                    delegationGestionRepository.save(delegationGestionBienImmAssocie);
                }
            }
        }
        delegationGestion.setStatutDelegation(2);
        delegationGestionRepository.save(delegationGestion);

        String delegationGestionLink = "http://localhost:4200/#/proprietaire/delegations-gestions";
        String contenu = "";

        if (delegationGestion.getAgenceImmobiliere() == null) {

            Notification notification = new Notification();
            notification.setTitre("Refus de la délégation de gestion de bien");
            notification.setMessage("M./Mlle " + delegationGestion.getGestionnaire().getPrenom() + " " + delegationGestion.getGestionnaire().getNom() + " vient de rejeter la gestion de votre bien que vous lui avez délégué.");
            notification.setSendTo(String.valueOf(delegationGestion.getBienImmobilier().getPersonne().getId()));
            notification.setLu(false);
            notification.setDateNotification(new Date());
            notification.setUrl("/delegations-gestions");
            notification.setCreerPar(delegationGestion.getGestionnaire().getId());
            notification.setCreerLe(new Date());
            notification.setStatut(true);
            notificationService.save(notification);

            contenu = "Bonjour M./Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + " " + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que M./Mlle " + delegationGestion.getGestionnaire().getPrenom() + " " + delegationGestion.getGestionnaire().getNom() + " vient de rejeter la gestion de votre bien que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";
        } else {

            Notification notification = new Notification();
            notification.setTitre("Refus de la délégation de gestion de bien");
            notification.setMessage("L'agence immobilière du nom de " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " vient de rejeter la gestion de votre bien que vous lui avez délégué.");
            notification.setSendTo(String.valueOf(delegationGestion.getBienImmobilier().getPersonne().getId()));
            notification.setLu(false);
            notification.setDateNotification(new Date());
            notification.setUrl("/delegations-gestions");
            notification.setCreerPar(delegationGestion.getAgenceImmobiliere().getId());
            notification.setCreerLe(new Date());
            notification.setStatut(true);
            notificationService.save(notification);

            contenu = "Bonjour M./Mlle " + delegationGestion.getBienImmobilier().getPersonne().getPrenom() + " " + delegationGestion.getBienImmobilier().getPersonne().getNom() + ",\n\n" +
                    "Nous avons le plaisir de vous informer que l'agence immobilière du nom de " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " vient de rejeter la gestion de votre bien que vous lui avez délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";
        }


        String finalContenu = contenu;
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getGestionnaire().getEmail(), "Refus de la délégation de gestion de bien", finalContenu);
        });
    }

    @Override
    public boolean bienImmobilierAndStatutDelegationAndEtatDelegationExists(BienImmobilier bienImmobilier, Integer statutDelegation, Boolean etatDelegation) {
        return delegationGestionRepository.existsByBienImmobilierAndStatutDelegationAndEtatDelegation(bienImmobilier, statutDelegation, etatDelegation);
    }

    @Override
    public boolean bienImmobilierAndAgenceImmobiliereExists(BienImmobilier bienImmobilier, AgenceImmobiliere agenceImmobiliere) {
        return delegationGestionRepository.existsByBienImmobilierAndAgenceImmobiliere(bienImmobilier, agenceImmobiliere);
    }

    @Override
    public boolean bienImmobilierAndGestionnaireExists(BienImmobilier bienImmobilier, Personne personne) {
        return delegationGestionRepository.existsByBienImmobilierAndGestionnaire(bienImmobilier, personne);
    }

    @Override
    public void activerDelegationGestion(Long id, Principal principal) {
        DelegationGestion delegationGestion = delegationGestionRepository.findById(id).orElse(null);
        delegationGestion.setEtatDelegation(true);
        Personne personne = personneService.findByUsername(principal.getName());

        String delegationGestionLink = "";

        if (delegationGestion.getAgenceImmobiliere() == null) {

            Notification notification = new Notification();

            notification.setTitre("Activation d'une délégation de gestion de bien");
            notification.setMessage("Une délégation de gestion de bien vous a été réactivée");
            notification.setSendTo(String.valueOf(delegationGestion.getGestionnaire().getId()));
            notification.setLu(false);
            notification.setDateNotification(new Date());
            notification.setUrl("/delegations-gestions");
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notification.setStatut(true);
            notificationService.save(notification);

            if ("ROLE_DEMARCHEUR".equals(delegationGestion.getGestionnaire().getRole().getCode())) {
                delegationGestionLink = "http://localhost:4200/#/demarcheur/delegations-gestions";
            } else {
                delegationGestionLink = "http://localhost:4200/#/gerant/delegations-gestions";
            }

            String contenu = "Bonjour M./Mlle " + delegationGestion.getGestionnaire().getPrenom() + " " + delegationGestion.getGestionnaire().getNom() + ",\n\n" +
                    "Nous sommes ravis de vous annoncer que M./Mlle " + personne.getPrenom() + " " + personne.getNom() + " a récemment activé la gestion d'un bien qu'il/elle vous avait délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";

            CompletableFuture.runAsync(() -> {
                emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getGestionnaire().getEmail(), "Activation d'une délégation de la gestion de bien", contenu);
            });
        } else {
            List <AffectationResponsableAgence> affectationResponsableAgences = affectationResponsableAgenceService.findByAgenceImmobiliere(delegationGestion.getAgenceImmobiliere());

            for (AffectationResponsableAgence affectationResponsableAgence: affectationResponsableAgences) {

                Notification notification = new Notification();

                notification.setTitre("Activation d'une délégation de gestion de bien");
                notification.setMessage("Une délégation de gestion de bien a été réactivée à votre agence");
                notification.setSendTo(String.valueOf(affectationResponsableAgence.getResponsableAgenceImmobiliere().getId()));
                notification.setLu(false);
                notification.setDateNotification(new Date());
                notification.setUrl("/agences-immobilieres/delegations-gestions");
                notification.setCreerPar(personne.getId());
                notification.setCreerLe(new Date());
                notification.setStatut(true);
                notificationService.save(notification);
            }

            delegationGestionLink = "http://localhost:4200/#/responsable/agences-immobilieres/delegations-gestions";
            String contenu = "Bonjour Chere Agence " + delegationGestion.getAgenceImmobiliere().getNomAgence() + ",\n\n" +
                    "Nous sommes ravis de vous annoncer que M./Mlle " + personne.getPrenom() + " " + personne.getNom() + " a récemment activé la gestion d'un bien qu'il/elle vous avait délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";

            CompletableFuture.runAsync(() -> {
                emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getAgenceImmobiliere().getAdresseEmail(), "Activation d'une délégation de la gestion de bien", contenu);
            });
        }
        delegationGestionRepository.save(delegationGestion);
    }

    @Override
    public void desactiverDelegationGestion(Long id, Principal principal) {
        DelegationGestion delegationGestion = delegationGestionRepository.findById(id).orElse(null);
        delegationGestion.setEtatDelegation(false);
        Personne personne = personneService.findByUsername(principal.getName());

        String delegationGestionLink = "";

        if (delegationGestion.getAgenceImmobiliere() == null) {

            Notification notification = new Notification();

            notification.setTitre("Désactivation d'une délégation de gestion de bien");
            notification.setMessage("Une délégation de gestion de bien vous a été retirée");
            notification.setSendTo(String.valueOf(delegationGestion.getGestionnaire().getId()));
            notification.setLu(false);
            notification.setDateNotification(new Date());
            notification.setUrl("/delegations-gestions");
            notification.setCreerPar(personne.getId());
            notification.setCreerLe(new Date());
            notification.setStatut(true);
            notificationService.save(notification);

            if ("ROLE_DEMARCHEUR".equals(delegationGestion.getGestionnaire().getRole().getCode())) {
                delegationGestionLink = "http://localhost:4200/#/demarcheur/delegations-gestions";
            } else {
                delegationGestionLink = "http://localhost:4200/#/gerant/delegations-gestions";
            }

            String contenu = "Bonjour M./Mlle " + delegationGestion.getGestionnaire().getPrenom() + " " + delegationGestion.getGestionnaire().getNom() + ",\n\n" +
                    "Nous sommes désolés de vous informer que M./Mlle " + personne.getPrenom() + " " + personne.getNom() + " a récemment désactivé la gestion d'un bien qu'il/elle vous avait délégué.\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";

            CompletableFuture.runAsync(() -> {
                emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getGestionnaire().getEmail(), "Désactivation d'une délégation de la gestion de bien", contenu);
            });
        } else {
            List <AffectationResponsableAgence> affectationResponsableAgences = affectationResponsableAgenceService.findByAgenceImmobiliere(delegationGestion.getAgenceImmobiliere());

            for (AffectationResponsableAgence affectationResponsableAgence: affectationResponsableAgences) {

                Notification notification = new Notification();

                notification.setTitre("Désactivation d'une délégation de gestion de bien");
                notification.setMessage("Une délégation de gestion de bien a été retirée à votre agence");
                notification.setSendTo(String.valueOf(affectationResponsableAgence.getResponsableAgenceImmobiliere().getId()));
                notification.setLu(false);
                notification.setDateNotification(new Date());
                notification.setUrl("/agences-immobilieres/delegations-gestions");
                notification.setCreerPar(personne.getId());
                notification.setCreerLe(new Date());
                notification.setStatut(true);
                notificationService.save(notification);
            }

            delegationGestionLink = "http://localhost:4200/#/responsable/agences-immobilieres/delegations-gestions";
            String contenu = "Bonjour Chere " + delegationGestion.getAgenceImmobiliere().getNomAgence() + ",\n\n" +
                    "Nous sommes désolés de vous informer que M./Mlle " + personne.getPrenom() + " " + personne.getNom() + " a récemment désactivé la gestion d'un bien qu'il/elle vous avait délégué..\n" +
                    "\n\n" +
                    "Cliquez sur le lien suivant pour accéder à la délégation de la gestion de bien : " + delegationGestionLink + "\n" +
                    "\n\n" +
                    "Cordialement,\n" +
                    "\nL'équipe de support technique - ahoewo !";

            CompletableFuture.runAsync(() -> {
                emailSenderService.sendMail(env.getProperty("spring.mail.username"), delegationGestion.getAgenceImmobiliere().getAdresseEmail(), "Désactivation d'un délégation de la gestion de bien", contenu);
            });
        }
        delegationGestionRepository.save(delegationGestion);
    }

    private DelegationGestion saveDelegationGestionTypeBien(DelegationGestionForm2 delegationGestionForm2, Caracteristiques caracteristiques, List<MultipartFile> files, Principal principal) {
        BienImmobilier bienImmobilier = new BienImmobilier();
        Personne personne = personneService.findByUsername(principal.getName());
        Proprietaire proprietaire = (Proprietaire) personneService.findByMatricule(delegationGestionForm2.getMatriculeProprietaire());
        DelegationGestion delegationGestion = new DelegationGestion();
        bienImmobilier.setTypeDeBien(delegationGestionForm2.getTypeDeBien());
        bienImmobilier.setPersonne(proprietaire);
        if (!isTypeBienBoutiqueOrMagasinOrTerrain(delegationGestionForm2.getTypeDeBien().getDesignation())) {
            bienImmobilier.setCategorie(delegationGestionForm2.getCategorie());
        }
        bienImmobilier.setPays(delegationGestionForm2.getPays());
        bienImmobilier.setRegion(delegationGestionForm2.getRegion());
        bienImmobilier.setVille(delegationGestionForm2.getVille());
        bienImmobilier.setQuartier(delegationGestionForm2.getQuartier());
        bienImmobilier.setAdresse(delegationGestionForm2.getAdresse());
        bienImmobilier.setSurface(delegationGestionForm2.getSurface());
        bienImmobilier.setDescription(delegationGestionForm2.getDescription());

        BienImmobilier bienImmobilierAdd = bienImmobilierService.save(bienImmobilier, principal);

        if (files != null) {
            for(MultipartFile images: files){
                ImagesBienImmobilier imagesBienImmobilier = new ImagesBienImmobilier();
                String nomImageDuBien = imagesBienImmobilierService.enregistrerImageDuBien(images);
                imagesBienImmobilier.setNomImage(nomImageDuBien);
                imagesBienImmobilier.setBienImmobilier(bienImmobilierAdd);
                imagesBienImmobilierService.save(imagesBienImmobilier, principal);
            }
        }

        if (caracteristiques != null) {
            caracteristiquesService.save(bienImmobilierAdd, caracteristiques, principal);
        }

        delegationGestion.setCodeDelegationGestion("DELGES" + UUID.randomUUID());
        delegationGestion.setBienImmobilier(bienImmobilierAdd);
        if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER")) {
            delegationGestion.setAgenceImmobiliere(delegationGestionForm2.getAgenceImmobiliere());
        } else {
            delegationGestion.setGestionnaire(personne);
        }
        delegationGestion.setDateDelegation(new Date());
        delegationGestion.setStatutDelegation(1);
        delegationGestion.setEtatDelegation(true);
        delegationGestion.setCreerLe(new Date());
        delegationGestion.setCreerPar(personne.getId());
        delegationGestion.setStatut(true);
        DelegationGestion delegationGestionAdd = delegationGestionRepository.save(delegationGestion);
        delegationGestionAdd.setCodeDelegationGestion("DELGES00" + delegationGestionAdd.getId());
        return delegationGestionRepository.save(delegationGestionAdd);
    }

    private DelegationGestion saveDelegationGestionBienImmAssocie(DelegationGestionForm2 delegationGestionForm2, Caracteristiques caracteristiques, List<MultipartFile> files, Principal principal) {
        BienImmAssocie bienImmAssocie = new BienImmAssocie();
        Personne personne = personneService.findByUsername(principal.getName());
        BienImmobilier bienImmobilier = bienImmobilierService.findByCodeBien(delegationGestionForm2.getMatriculeBienImmo());
        DelegationGestion delegationGestion = new DelegationGestion();
        bienImmAssocie.setTypeDeBien(delegationGestionForm2.getTypeDeBien());
        bienImmAssocie.setPersonne(bienImmobilier.getPersonne());
        if (!isTypeBienBoutiqueOrMagasinOrTerrain(delegationGestionForm2.getTypeDeBien().getDesignation())) {
            bienImmAssocie.setCategorie(delegationGestionForm2.getCategorie());
        }
        bienImmAssocie.setBienImmobilier(bienImmobilier);
        bienImmAssocie.setPays(bienImmobilier.getPays());
        bienImmAssocie.setRegion(bienImmobilier.getRegion());
        bienImmAssocie.setVille(bienImmobilier.getVille());
        bienImmAssocie.setQuartier(bienImmobilier.getQuartier());
        bienImmAssocie.setAdresse(bienImmobilier.getAdresse());
        bienImmAssocie.setSurface(delegationGestionForm2.getSurface());
        bienImmAssocie.setDescription(delegationGestionForm2.getDescription());

        BienImmAssocie bienImmAssocieAdd = bienImmoAssocieService.save(bienImmAssocie, principal);

        if (files != null) {
            for(MultipartFile images: files){
                ImagesBienImmobilier imagesBienImmobilier = new ImagesBienImmobilier();
                String nomImageDuBien = imagesBienImmobilierService.enregistrerImageDuBien(images);
                imagesBienImmobilier.setNomImage(nomImageDuBien);
                imagesBienImmobilier.setBienImmobilier(bienImmAssocieAdd);
                imagesBienImmobilierService.save(imagesBienImmobilier, principal);
            }
        }

        if (caracteristiques != null) {
            caracteristiquesService.save(bienImmAssocieAdd, caracteristiques, principal);
        }

        delegationGestion.setCodeDelegationGestion("DELGES" + UUID.randomUUID());
        delegationGestion.setBienImmobilier(bienImmAssocieAdd);
        if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER")) {
            delegationGestion.setAgenceImmobiliere(bienImmAssocieAdd.getAgenceImmobiliere());
        } else {
            delegationGestion.setGestionnaire(personne);
        }
        delegationGestion.setDateDelegation(new Date());
        delegationGestion.setStatutDelegation(1);
        delegationGestion.setEtatDelegation(true);
        delegationGestion.setCreerLe(new Date());
        delegationGestion.setCreerPar(personne.getId());
        delegationGestion.setStatut(true);
        DelegationGestion delegationGestionAdd = delegationGestionRepository.save(delegationGestion);
        delegationGestionAdd.setCodeDelegationGestion("DELGES00" + delegationGestionAdd.getId());
        return delegationGestionRepository.save(delegationGestionAdd);
    }

    private boolean isTypeBienBoutiqueOrMagasinOrTerrain(String designation) {
        return designation.equals("Boutique") ||
                designation.equals("Magasin");
    }

}
