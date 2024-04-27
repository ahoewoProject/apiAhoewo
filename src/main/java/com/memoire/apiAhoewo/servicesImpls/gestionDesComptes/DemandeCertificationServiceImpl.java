package com.memoire.apiAhoewo.servicesImpls.gestionDesComptes;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.DemandeCertification;
import com.memoire.apiAhoewo.models.gestionDesComptes.Notaire;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres.AffectationAgentAgenceRepository;
import com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres.AgenceImmobiliereRepository;
import com.memoire.apiAhoewo.repositories.gestionDesComptes.AgentImmobilierRepository;
import com.memoire.apiAhoewo.repositories.gestionDesComptes.DemandeCertificationRepository;
import com.memoire.apiAhoewo.repositories.gestionDesComptes.PersonneRepository;
import com.memoire.apiAhoewo.services.EmailSenderService;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesComptes.DemandeCertificationService;
import com.memoire.apiAhoewo.services.gestionDesComptes.NotaireService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class DemandeCertificationServiceImpl implements DemandeCertificationService {
    @Autowired
    private DemandeCertificationRepository demandeCertificationRepository;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private AgenceImmobiliereRepository agenceImmobiliereRepository;
    @Autowired
    private AgentImmobilierRepository agentImmobilierRepository;
    @Autowired
    private AffectationAgentAgenceRepository affectationAgentAgenceRepository;
    @Autowired
    private Environment env;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotaireService notaireService;
    @Autowired
    private PersonneService personneService;

    @Override
    public Page<DemandeCertification> getDemandesCertifications(int numeroDeLaPage, int elementsParPage, Principal principal) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        Personne personne = personneService.findByUsername(principal.getName());
        if (personne.getRole().getCode().equals("ROLE_NOTAIRE")) {
            return demandeCertificationRepository.findAllByOrderByCreerLeDesc(pageRequest);
        } else {
            return demandeCertificationRepository.findByPersonneOrderByCreerLeDesc(personne, pageRequest);
        }
    }

    @Override
    public DemandeCertification findById(Long id) {
        return demandeCertificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<DemandeCertification> getByUser(Principal principal) {
        Personne personne = personneRepository.findByUsername(principal.getName());
        return demandeCertificationRepository.findByPersonne(personne);
    }

    @Override
    public DemandeCertification saveDemandeCertificationCompte(DemandeCertification demandeCertification, Principal principal) {
        Personne personne = personneRepository.findByUsername(principal.getName());

        demandeCertification.setCodeCertification("DEMCER" + UUID.randomUUID());
        demandeCertification.setDateDemande(new Date());
        demandeCertification.setStatutDemande(0);
        demandeCertification.setCreerLe(new Date());
        demandeCertification.setCreerPar(personne.getId());
        demandeCertification.setStatut(true);
        demandeCertification = demandeCertificationRepository.save(demandeCertification);


        List<Notaire> notaireList = notaireService.getNotaireActifs();

        if (!notaireList.isEmpty()) {
            for (Notaire notaire : notaireList) {

                String contenuMail = "Bonjour M/Mlle. " + notaire.getPrenom() + " " + notaire.getNom() + ",\n\n" +
                        "Nous avons le plaisir de vous informer qu'une nouvelle demande de certification de compte a été soumise.\n\n" +
                        "Détails de la demande :\n" +
                        "- Demandeur : M/Mlle. " + personne.getPrenom() + " " + personne.getNom() + "\n" +
                        "- Code de demande : DEMCER00" + demandeCertification.getId() + "\n" +
                        "- Lien de la demande : " + env.getProperty("ahoewo.client") + "notaire/demandes-certifications/" + demandeCertification.getId() + "\n\n" +
                        "Nous vous prions de bien vouloir prendre les mesures nécessaires pour traiter cette demande dans les meilleurs délais.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Ahoewo";

                CompletableFuture.runAsync(() -> {
                    emailSenderService.sendMail(env.getProperty("spring.mail.username"), notaire.getEmail(), "Demande de certification de compte", contenuMail);
                });
            }
        }

        Notification notification = new Notification();
        notification.setTitre("Nouvelle demande de certification.");
        notification.setMessage("M/Mlle. " + personne.getPrenom() + " " + personne.getNom() + " vient de soumettre une demande de certification pour l'agence du nom de : " + demandeCertification.getAgenceImmobiliere().getNomAgence());
        notification.setSendTo("NOTAIRE");
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-certifications/" + demandeCertification.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        // Mise à jour du code de certification
        demandeCertification.setCodeCertification("DEMCER00" + demandeCertification.getId());
        return demandeCertificationRepository.save(demandeCertification);
    }

    @Override
    public DemandeCertification saveDemandeCertificationAgence(DemandeCertification demandeCertification,
                                                               Principal principal) {
        Personne personne = personneRepository.findByUsername(principal.getName());

        demandeCertification.setCodeCertification("DEMCER" + UUID.randomUUID());
        demandeCertification.setDateDemande(new Date());
        demandeCertification.setStatutDemande(0);
        demandeCertification.setCreerLe(new Date());
        demandeCertification.setCreerPar(personne.getId());
        demandeCertification.setStatut(true);
        demandeCertification = demandeCertificationRepository.save(demandeCertification);

        List<Notaire> notaireList = notaireService.getNotaireActifs();

        if (!notaireList.isEmpty()) {
            for (Notaire notaire : notaireList) {

                String contenuMail = "Bonjour M/Mlle. " + notaire.getPrenom() + " " + notaire.getNom() + ",\n\n" +
                        "Nous avons le plaisir de vous informer qu'une nouvelle demande de certification d'agence a été soumise.\n\n" +
                        "Détails de la demande :\n" +
                        "- Nom de l'agence : " + demandeCertification.getAgenceImmobiliere().getNomAgence() + "\n" +
                        "- Demandeur : M/Mlle. " + personne.getPrenom() + " " + personne.getNom() + "\n" +
                        "- Code de demande : DEMCER00" + demandeCertification.getId() + "\n" +
                        "- Lien de la demande : " + env.getProperty("ahoewo.client") + "notaire/demandes-certifications/" + demandeCertification.getId() + "\n\n" +
                        "Nous vous prions de bien vouloir prendre les mesures nécessaires pour traiter cette demande dans les meilleurs délais.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Ahoewo";

                CompletableFuture.runAsync(() -> {
                    emailSenderService.sendMail(env.getProperty("spring.mail.username"), notaire.getEmail(), "Demande de certification d'agence", contenuMail);
                });
            }
        }

        Notification notification = new Notification();
        notification.setTitre("Nouvelle demande de certification.");
        notification.setMessage("M/Mlle. " + personne.getPrenom() + " " + personne.getNom() + " vient de soumettre une demande de certification pour l'agence du nom de : " + demandeCertification.getAgenceImmobiliere().getNomAgence());
        notification.setSendTo("NOTAIRE");
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-certifications/" + demandeCertification.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        demandeCertification.setCodeCertification("DEMCER00" + demandeCertification.getId());
        return demandeCertificationRepository.save(demandeCertification);
    }


    @Override
    public void certifierCompte(Long idPersonne, Long idDemandeCertif) {
        DemandeCertification demandeCertification = demandeCertificationRepository.findById(idDemandeCertif).orElse(null);
        demandeCertification.setStatutDemande(1);
        Personne personne = personneRepository.findById(idPersonne).orElse(null);
        personne.setEstCertifie(true);
        demandeCertificationRepository.save(demandeCertification);
        personneRepository.save(personne);

        Notification notification = new Notification();
        notification.setTitre("Certification de compte.");
        notification.setMessage("M/Mlle. " + personne.getPrenom() + " " + personne.getNom() + ", votre compte vient d'être certifié conformément à votre demande de certification.");
        notification.setSendTo(String.valueOf(personne.getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-certifications/" + demandeCertification.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        String contenu = "Bonjour M/Mlle " + personne.getPrenom() + " " + personne.getNom() + ",\n\n" +
                "Nous avons le plaisir de vous informer que votre compte vient d'être certifié conformément à votre demande de certification.\n" +
                "- Lien de la demande : " + env.getProperty("ahoewo.client") + "notaire/demandes-certifications/" + demandeCertification.getId() + "\n\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), personne.getEmail(), "Certification de compte", contenu);
        });

    }

    @Override
    public void certifierAgence(Long idAgence, Long idDemandeCertif) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findById(idAgence).orElse(null);
        agenceImmobiliere.setEstCertifie(true);
        DemandeCertification demandeCertification = demandeCertificationRepository.findById(idDemandeCertif).orElse(null);
        demandeCertification.setStatutDemande(1);
        Personne personne = personneRepository.findById(demandeCertification.getPersonne().getId()).orElse(null);
        personne.setEstCertifie(true);

        agenceImmobiliereRepository.save(agenceImmobiliere);
        demandeCertificationRepository.save(demandeCertification);
        personneRepository.save(personne);

        String contenu1 = "Bonjour " + agenceImmobiliere.getNomAgence() + ",\n\n" +
                "Nous avons le plaisir de vous informer que votre agence immobilière vient d'être certifiée conformément à votre demande de certification.\n" +
                "- Lien de la demande : " + env.getProperty("ahoewo.client") + "responsable/agences-immobilieres/demandes-certifications/" + demandeCertification.getId() + "\n\n" +
                "N'hésitez pas à explorer toutes les fonctionnalités offertes par notre plateforme pour optimiser votre travail et offrir le meilleur service à vos clients.\n\n" +
                "Si vous avez des questions ou avez besoin d'assistance, n'hésitez pas à nous contacter. Nous sommes là pour vous aider.\n\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), agenceImmobiliere.getAdresseEmail(), "Certification d'une agence", contenu1);
        });

        String contenu2 = "Bonjour M/Mlle " + personne.getPrenom() + " " + personne.getNom() + ",\n\n" +
                "Nous avons le plaisir de vous informer que votre compte vient d'être certifié conformément à la demande de certification de votre agence.\n" +
                "- Lien de la demande : " + env.getProperty("ahoewo.client") + "responsable/agences-immobilieres/demandes-certifications/" + demandeCertification.getId() + "\n\n" +
                "N'hésitez pas à explorer toutes les fonctionnalités offertes par notre plateforme pour optimiser votre travail et offrir le meilleur service à vos clients.\n\n" +
                "Si vous avez des questions ou avez besoin d'assistance, n'hésitez pas à nous contacter. Nous sommes là pour vous aider.\n\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), personne.getEmail(), "Certification de compte", contenu2);
        });

        Notification notification = new Notification();
        notification.setTitre("Certification d'agence/Certification de compte.");
        notification.setMessage("M/Mlle. " + personne.getPrenom() + " " + personne.getNom() + ", votre agence ainsi que votre compte vient d'être certifié conformément à la demande de certification de votre agence.");
        notification.setSendTo(String.valueOf(personne.getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/demandes-certifications/" + demandeCertification.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        List<AffectationAgentAgence> affectationAgentAgenceList = affectationAgentAgenceRepository.findByAgenceImmobiliere(agenceImmobiliere);
        List<AgentImmobilier> agentImmobiliers = affectationAgentAgenceList.stream()
                .map(AffectationAgentAgence::getAgentImmobilier)
                .collect(Collectors.toList());

        if (!agentImmobiliers.isEmpty()) {
            for (AgentImmobilier agent : agentImmobiliers) {
                agent.setEstCertifie(true);
                agentImmobilierRepository.save(agent);

                String contenu3 = "Bonjour M/Mlle " + agent.getPrenom() + " " + agent.getNom() + ",\n\n" +
                        "Nous avons le plaisir de vous informer que votre compte vient d'être certifié conformément à la demande de certification soumise par l'agence immobilière chez laquelle vous travaillez.\n" +
                        "\n\n" +
                        "N'hésitez pas à explorer toutes les fonctionnalités offertes par notre plateforme pour optimiser votre travail et offrir le meilleur service à vos clients.\n\n" +
                        "Si vous avez des questions ou avez besoin d'assistance, n'hésitez pas à nous contacter. Nous sommes là pour vous aider.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Ahoewo";
                CompletableFuture.runAsync(() -> {
                    emailSenderService.sendMail(env.getProperty("spring.mail.username"), agent.getEmail(), "Certification de compte", contenu3);
                });
            }
        }
    }

    @Override
    public int countDemandeCertifications() {
        return (int) demandeCertificationRepository.count();
    }

    @Override
    public int countDemandeCertifValidees() {
        List<DemandeCertification> demandeCertificationList = demandeCertificationRepository.findByStatutDemande(1);
        int count = demandeCertificationList.size();
        return count;
    }

    @Override
    public int countDemandeCertifEnAttente() {
        List<DemandeCertification> demandeCertificationList = demandeCertificationRepository.findByStatutDemande(0);
        int count = demandeCertificationList.size();
        return count;
    }

    /* Fonction pour l'enregistrement du document justificatif de la demande de certification */
    @Override
    public String enregistrerDocumentJustificatif(MultipartFile file) {
        String nomDocument = null;

        try {
            String repertoireImage = "src/main/resources/documentsJustificatifs";
            File repertoire = creerRepertoire(repertoireImage);

            String document = file.getOriginalFilename();
            nomDocument = FilenameUtils.getBaseName(document) + "." + FilenameUtils.getExtension(document);
            File ressourceDocument = new File(repertoire, nomDocument);

            FileUtils.writeByteArrayToFile(ressourceDocument, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nomDocument;
    }

    /* Fonction pour la création du repertoire des documents justificatifs de la demande de certification */
    private File creerRepertoire(String repertoireDocument) {
        File repertoire = new File(repertoireDocument);
        if (!repertoire.exists()) {
            boolean repertoireCree = repertoire.mkdirs();
            if (!repertoireCree) {
                throw new RuntimeException("Impossible de créer ce répertoire.");
            }
        }
        return repertoire;
    }

    /* Fonction pour construire le chemin vers le document justificatif */
    @Override
    public String construireCheminFichier(String nomFichier) {
        String repertoireFichier = "src/main/resources/documentsJustificatifs";
        return repertoireFichier + "/" + nomFichier;
    }
}
