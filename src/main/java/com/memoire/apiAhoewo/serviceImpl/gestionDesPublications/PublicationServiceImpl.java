package com.memoire.apiAhoewo.serviceImpl.gestionDesPublications;

import com.memoire.apiAhoewo.model.Notification;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.*;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.BienImmobilierRepository;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.DelegationGestionRepository;
import com.memoire.apiAhoewo.repository.gestionDesPublications.PublicationRepository;
import com.memoire.apiAhoewo.requestForm.RechercheAvanceePublicationForm;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.NotificationService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.CaracteristiquesService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.DelegationGestionService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.RegionService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.TypeDeBienService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.service.gestionDesPublications.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class PublicationServiceImpl implements PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private BienImmobilierRepository bienImmobilierRepository;
    @Autowired
    private DelegationGestionRepository delegationGestionRepository;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private TypeDeBienService typeDeBienService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private DelegationGestionService delegationGestionService;
    @Autowired
    private CaracteristiquesService caracteristiquesService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private Environment env;

    @Override
    public Page<Publication> rechercheSimpleDePublicationsActives(String typeDeTransaction, TypeDeBien typeDeBien, Quartier quartier, int numeroDeLaPage, int elementsParPage) {

        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        Page<Publication> pagePublications = publicationRepository.findByEtatOrderByIdDesc(true, pageRequest);

        List<Publication> publicationList = pagePublications.getContent().stream()
                .filter(p -> (typeDeTransaction == null || p.getTypeDeTransaction().equals(typeDeTransaction)))
                .filter(p -> (typeDeBien == null || p.getBienImmobilier().getTypeDeBien().equals(typeDeBien)))
                .filter(p -> (quartier == null || p.getBienImmobilier().getQuartier().equals(quartier)))
                .collect(Collectors.toList());

        long totalElements = publicationList.isEmpty() ? 0 : publicationList.size();

        return new PageImpl<>(publicationList, pageRequest, totalElements);
    }

    @Override
    public Page<Publication> rechercheAvanceeDePublicationsActives(RechercheAvanceePublicationForm rechercheAvancePublicationForm, int numeroDeLaPage, int elementsParPage) {

        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        Page<Publication> pagePublications = publicationRepository.findByEtatOrderByIdDesc(true, pageRequest);

        List<Publication> publicationList = pagePublications.getContent().stream()
                .filter(p -> (rechercheAvancePublicationForm.getTypeDeTransaction() == null || p.getTypeDeTransaction().equals(rechercheAvancePublicationForm.getTypeDeTransaction())))
                .filter(p -> (rechercheAvancePublicationForm.getTypeDeBien() == null || rechercheAvancePublicationForm.getTypeDeBien().estNull() || p.getBienImmobilier().getTypeDeBien().equals(rechercheAvancePublicationForm.getTypeDeBien())))
                .filter(p -> (rechercheAvancePublicationForm.getQuartier() == null || rechercheAvancePublicationForm.getQuartier().estNull() || p.getBienImmobilier().getQuartier().equals(rechercheAvancePublicationForm.getQuartier())))
                .filter(publication ->
                        (rechercheAvancePublicationForm.getPrixMin() == null || publication.getPrixDuBien() >= rechercheAvancePublicationForm.getPrixMin())
                        && (rechercheAvancePublicationForm.getPrixMax() == null || publication.getPrixDuBien() <= rechercheAvancePublicationForm.getPrixMax()))
                .filter(publication -> (rechercheAvancePublicationForm.getFraisDeVisite() == null ||
                        (publication.getFraisDeVisite() != null &&
                                publication.getFraisDeVisite() <= rechercheAvancePublicationForm.getFraisDeVisite())))
                .filter(publication -> (rechercheAvancePublicationForm.getAvance() == null || publication.getAvance() <= rechercheAvancePublicationForm.getAvance()))
                .filter(publication -> (rechercheAvancePublicationForm.getCaution() == null || publication.getCaution() <= rechercheAvancePublicationForm.getCaution()))
                .filter(publication -> (rechercheAvancePublicationForm.getCommission() == null || publication.getCommission() <= rechercheAvancePublicationForm.getCommission()))
                .filter(publication -> matchCaracteristiques(publication.getBienImmobilier(), rechercheAvancePublicationForm))
                .collect(Collectors.toList());

        long totalElements = publicationList.isEmpty() ? 0 : publicationList.size();

        return new PageImpl<>(publicationList, pageRequest, totalElements);
    }

    @Override
    public Page<Publication> getPublicationsActives(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return publicationRepository.findByEtatOrderByIdDesc(true, pageRequest);
    }

    @Override
    public Page<Publication> getPublicationsActivesByTypeDeTransaction(String typeDeTransaction, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return publicationRepository.findByTypeDeTransactionAndEtatOrderByIdDesc(typeDeTransaction, true, pageRequest);
    }

    @Override
    public Page<Publication> getPublicationsActivesByTypeDeBienList(int numeroDeLaPage, int elementsParPage) {
        List<TypeDeBien> typeDeBienList = typeDeBienService.findTypeDeBienActifs();

        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return publicationRepository.findByBienImmobilier_TypeDeBienInAndEtatOrderByIdDesc(typeDeBienList, true, pageRequest);
    }

    @Override
    public Page<Publication> getPublicationsActivesByTypeDeBien(TypeDeBien typeDeBien, int numeroDeLaPage, int elementsParPage) {
        return publicationRepository.findByBienImmobilier_TypeDeBienAndEtatOrderByIdDesc(typeDeBien, true, PageRequest.of(numeroDeLaPage, elementsParPage));
    }

    @Override
    public Page<Publication> getPublicationsActivesByRegionsList(int numeroDeLaPage, int elementsParPage) {
        List<Region> regionList = regionService.getRegionsActifs(true);

        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return publicationRepository.findByBienImmobilier_Quartier_Ville_RegionInAndEtatOrderByIdDesc(regionList, true, pageRequest);
    }

    @Override
    public Page<Publication> getPublicationsActivesByRegion(Region region, int numeroDeLaPage, int elementsParPage) {
        return publicationRepository.findByBienImmobilier_Quartier_Ville_RegionAndEtatOrderByIdDesc(region, true, PageRequest.of(numeroDeLaPage, elementsParPage));
    }

    @Override
    public Page<Publication> getPublicationsActivesByAgence(AgenceImmobiliere agenceImmobiliere, int numeroDeLaPage, int elementsParPage) {
        return publicationRepository.findByAgenceImmobiliereAndEtatOrderByIdDesc(agenceImmobiliere, true, PageRequest.of(numeroDeLaPage, elementsParPage));
    }

    @Override
    public Page<Publication> getPublicationsActivesByPersonne(Personne personne, int numeroDeLaPage, int elementsParPage) {
        return publicationRepository.findByPersonneAndEtatOrderByIdDesc(personne, true, PageRequest.of(numeroDeLaPage, elementsParPage));
    }

    @Override
    public Page<Publication> getPublications(int numeroDeLaPage, int elementsParPage, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<BienImmobilier> bienImmobilierList = new ArrayList<>();

        if (personne.getRole().getCode().equals("ROLE_PROPRIETAIRE")) {
            bienImmobilierList = bienImmobilierRepository.findByPersonne(personne);
        } else if (personne.getRole().getLibelle().equals("ROLE_GERANT")) {
            List<DelegationGestion> delegationGestionList = delegationGestionRepository.findByGestionnaireAndStatutDelegation(personne, 1);

            bienImmobilierList = delegationGestionList.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList());
        } else if (personne.getRole().getCode().equals("ROLE_DEMARCHEUR")) {
            List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByPersonne(personne);
            List<DelegationGestion> delegationGestions = delegationGestionRepository.findByGestionnaireAndStatutDelegation(personne, 1);

            bienImmobilierList = new ArrayList<>(bienImmobiliers);
            bienImmobilierList.addAll(delegationGestions.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList()));
        } else if (personne.getRole().getCode().equals("ROLE_RESPONSABLE")) {
            List<AgenceImmobiliere> agenceImmobiliereList = agenceImmobiliereService.getAgencesByResponsable(principal);
            List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobiliereList);
            List<DelegationGestion> delegationGestionList = delegationGestionRepository.findByAgenceImmobiliereInAndStatutDelegation(agenceImmobiliereList, 1);

            bienImmobilierList = new ArrayList<>(bienImmobiliers);
            bienImmobilierList.addAll(delegationGestionList.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList()));
        } else if (personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER")) {
            List<AgenceImmobiliere> agenceImmobiliereList = agenceImmobiliereService.getAgencesByAgent(principal);
            List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobiliereList);
            List<DelegationGestion> delegationGestionList = delegationGestionRepository.findByAgenceImmobiliereInAndStatutDelegation(agenceImmobiliereList, 1);

            bienImmobilierList = new ArrayList<>(bienImmobiliers);
            bienImmobilierList.addAll(delegationGestionList.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList()));
        }

        return publicationRepository.findByBienImmobilierInOrderByIdDesc(bienImmobilierList, PageRequest.of(numeroDeLaPage, elementsParPage));
    }

    @Override
    public Publication save(Publication publication, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        String roleCode = personne.getRole().getCode();

        publication.setCodePublication("PUB" + UUID.randomUUID());
        publication.setDatePublication(new Date());
        publication.setCreerLe(new Date());
        publication.setCreerPar(personne.getId());
        publication.setEtat(true);
        publication.setStatut(true);

        Publication publicationAdd = publicationRepository.save(publication);

        String publicationLink = "";

        if (roleCode.equals("ROLE_RESPONSABLE") || roleCode.equals("ROLE_AGENTIMMOBILIER")) {
            DelegationGestion delegationGestion = delegationGestionService.getDelegationByBienImmobilierAndEtatDelegation(publication.getBienImmobilier(), true);

            if (delegationGestion != null) {
                Notification notification = new Notification();
                notification.setTitre("Nouvelle publication de bien");
                notification.setMessage("Le bien " + publication.getBienImmobilier().getCodeBien() + " délégué a été publié par l'agence " + delegationGestion.getAgenceImmobiliere().getNomAgence());
                notification.setSendTo(String.valueOf(publication.getBienImmobilier().getPersonne().getId()));
                notification.setDateNotification(new Date());
                notification.setLu(false);
                notification.setUrl("/publications");
                notification.setCreerPar(personne.getId());
                notification.setCreerLe(new Date());
                notificationService.save(notification);

                publicationLink = "http://localhost:4200/proprietaire/publications";

                String contenu = "Bonjour M./Mlle " + publication.getBienImmobilier().getPersonne().getNom() + " " + publication.getBienImmobilier().getPersonne().getPrenom() + ",\n" +
                        "Le bien immobilier " + publication.getBienImmobilier().getCodeBien() + " que vous avez délégué à l'agence " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " a été publié avec succès.\n" +
                        "Vous pouvez consulter la publication en cliquant sur le lien suivant: " + publicationLink + "\n" +
                        "Cordialement,\n" +
                        "L'équipe Ahoewo";

                CompletableFuture.runAsync(() -> {
                    emailSenderService.sendMail(env.getProperty("spring.mail.username"), publication.getBienImmobilier().getPersonne().getEmail(), "Publication de bien", contenu);
                });
            } else {

            }
        } else if (roleCode.equals("ROLE_GERANT") || roleCode.equals("ROLE_DEMARCHEUR")) {
            DelegationGestion delegationGestion = delegationGestionService.getDelegationByBienImmobilierAndEtatDelegation(publication.getBienImmobilier(), true);

            if (delegationGestion != null) {
                Notification notification = new Notification();
                notification.setTitre("Nouvelle publication de bien");
                notification.setMessage("Le bien " + publication.getBienImmobilier().getCodeBien() + " délégué à M./Mlle " + delegationGestion.getGestionnaire().getNom() +
                        " " + delegationGestion.getGestionnaire().getPrenom());
                notification.setSendTo(String.valueOf(publication.getBienImmobilier().getPersonne().getId()));
                notification.setDateNotification(new Date());
                notification.setLu(false);
                notification.setUrl("/publications");
                notification.setCreerPar(personne.getId());
                notification.setCreerLe(new Date());
                notificationService.save(notification);

                publicationLink = "http://localhost:4200/proprietaire/publications";

                String contenu = "Bonjour M./Mlle " + publication.getBienImmobilier().getPersonne().getNom() + " " + publication.getBienImmobilier().getPersonne().getPrenom() +
                        "Le bien immobilier " + publication.getBienImmobilier().getCodeBien() + " délégué à M./Mlle " + delegationGestion.getGestionnaire().getNom() +
                        " " + delegationGestion.getGestionnaire().getPrenom() + " a été publié avec succès.\n" +
                        "Vous pouvez consulter la publication en cliquant sur le lien suivant: " + publicationLink + "\n" +
                        "Cordialement,\n" +
                        "L'équipe Ahoewo";

                CompletableFuture.runAsync(() -> {
                    emailSenderService.sendMail(env.getProperty("spring.mail.username"), publication.getBienImmobilier().getPersonne().getEmail(), "Publication de bien", contenu);
                });
            }
        }
        publicationAdd.setCodePublication("PUB-" + new SimpleDateFormat("yyyy-MM-dd").format(publicationAdd.getDatePublication()) + "-00" + publicationAdd.getId());
        return publicationRepository.save(publicationAdd);
    }

    @Override
    public Publication update(Publication publication, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        String roleCode = personne.getRole().getCode();

        publication.setModifierLe(new Date());
        publication.setModifierPar(personne.getId());

        String publicationLink = "";
        if (roleCode.equals("ROLE_RESPONSABLE") || roleCode.equals("ROLE_AGENTIMMOBILIER")) {
            DelegationGestion delegationGestion = delegationGestionService.getDelegationByBienImmobilierAndEtatDelegation(publication.getBienImmobilier(), true);

            if (delegationGestion != null) {
                Notification notification = new Notification();
                notification.setTitre("Modification d'une publication de bien");
                notification.setMessage("Le bien " + publication.getBienImmobilier().getCodeBien() + " délégué a été modifié par l'agence " + delegationGestion.getAgenceImmobiliere().getNomAgence());
                notification.setSendTo(String.valueOf(publication.getBienImmobilier().getPersonne().getId()));
                notification.setDateNotification(new Date());
                notification.setLu(false);
                notification.setUrl("/publications");
                notification.setCreerPar(personne.getId());
                notification.setCreerLe(new Date());
                notificationService.save(notification);

                publicationLink = "http://localhost:4200/proprietaire/publications";

                String contenu = "Bonjour M./Mlle " + publication.getBienImmobilier().getPersonne().getNom() + " " + publication.getBienImmobilier().getPersonne().getPrenom() + ",\n" +
                        "Le bien immobilier " + publication.getBienImmobilier().getCodeBien() + " que vous avez délégué à l'agence " + delegationGestion.getAgenceImmobiliere().getNomAgence() + " a été modifié avec succès.\n" +
                        "Vous pouvez consulter la publication en cliquant sur le lien suivant: " + publicationLink + "\n" +
                        "Cordialement,\n" +
                        "L'équipe Ahoewo";

                CompletableFuture.runAsync(() -> {
                    emailSenderService.sendMail(env.getProperty("spring.mail.username"), publication.getBienImmobilier().getPersonne().getEmail(), "Modification de publication de bien", contenu);
                });
            }
        } else if (roleCode.equals("ROLE_GERANT") || roleCode.equals("ROLE_DEMARCHEUR")) {
            DelegationGestion delegationGestion = delegationGestionService.getDelegationByBienImmobilierAndEtatDelegation(publication.getBienImmobilier(), true);

            if (delegationGestion != null) {
                Notification notification = new Notification();
                notification.setTitre("Modification d'une publication de bien");
                notification.setMessage("Le bien " + publication.getBienImmobilier().getCodeBien() + " délégué à M./Mlle " + delegationGestion.getGestionnaire().getNom() +
                        " " + delegationGestion.getGestionnaire().getPrenom());
                notification.setSendTo(String.valueOf(publication.getBienImmobilier().getPersonne().getId()));
                notification.setDateNotification(new Date());
                notification.setLu(false);
                notification.setUrl("/publications");
                notification.setCreerPar(personne.getId());
                notification.setCreerLe(new Date());
                notificationService.save(notification);

                publicationLink = "http://localhost:4200/proprietaire/publications";

                String contenu = "Bonjour M./Mlle " + publication.getBienImmobilier().getPersonne().getNom() + " " + publication.getBienImmobilier().getPersonne().getPrenom() +
                        "Le bien immobilier " + publication.getBienImmobilier().getCodeBien() + " délégué à M./Mlle " + delegationGestion.getGestionnaire().getNom() +
                        " " + delegationGestion.getGestionnaire().getPrenom() + " a été modifié avec succès.\n" +
                        "Vous pouvez consulter la publication en cliquant sur le lien suivant: " + publicationLink + "\n" +
                        "Cordialement,\n" +
                        "L'équipe Ahoewo";

                CompletableFuture.runAsync(() -> {
                    emailSenderService.sendMail(env.getProperty("spring.mail.username"), publication.getBienImmobilier().getPersonne().getEmail(), "Modification de publication de bien", contenu);
                });
            }
        }
        return publicationRepository.save(publication);
    }

    @Override
    public Publication findById(Long id) {
        return publicationRepository.findById(id).orElse(null);
    }

    @Override
    public Publication findByCodePublication(String codePublication) {
        return publicationRepository.findByCodePublication(codePublication);
    }

    @Override
    public void activerPublication(Long id) {
        Publication publication = publicationRepository.findById(id).orElse(null);
        if (publication != null) {
            publication.setEtat(true);
            publicationRepository.save(publication);
        }
    }

    @Override
    public void desactiverPublication(Long id) {
        Publication publication = publicationRepository.findById(id).orElse(null);
        if (publication != null) {
            publication.setEtat(false);
            publicationRepository.save(publication);
        }
    }

    @Override
    public boolean existsByBienImmobilierAndEtat(BienImmobilier bienImmobilier, Boolean etat) {
        return publicationRepository.existsByBienImmobilierAndEtat(bienImmobilier, etat);
    }

    private boolean matchCaracteristiques(final BienImmobilier bienImmobilier, final RechercheAvanceePublicationForm rechercheAvancePublicationForm) {
        Caracteristiques caracteristiques = caracteristiquesService.findByBienImmobilier(bienImmobilier.getId());
        if (caracteristiques != null) {
            if (rechercheAvancePublicationForm.getNombreChambres() == null || rechercheAvancePublicationForm.getNombreChambres() <= caracteristiques.getNombreChambres()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreChambresSalon() == null || rechercheAvancePublicationForm.getNombreChambresSalon() <= caracteristiques.getNombreChambresSalon()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreBureaux() == null || rechercheAvancePublicationForm.getNombreBureaux() <= caracteristiques.getNombreBureaux()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreBoutiques() == null || rechercheAvancePublicationForm.getNombreBoutiques() <= caracteristiques.getNombreBoutiques()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreMagasins() == null || rechercheAvancePublicationForm.getNombreMagasins() <= caracteristiques.getNombreMagasins()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreAppartements() == null || rechercheAvancePublicationForm.getNombreAppartements() <= caracteristiques.getNombreAppartements()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreEtages() == null || rechercheAvancePublicationForm.getNombreEtages() <= caracteristiques.getNombreEtages()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreGarages() == null || rechercheAvancePublicationForm.getNombreGarages() <= caracteristiques.getNombreGarages()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreSalons() == null || rechercheAvancePublicationForm.getNombreSalons() <= caracteristiques.getNombreSalons()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombrePlacards() == null || rechercheAvancePublicationForm.getNombrePlacards() <= caracteristiques.getNombrePlacards()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreCuisineInterne() == null || rechercheAvancePublicationForm.getNombreCuisineInterne() <= caracteristiques.getNombreCuisineInterne()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreCuisineExterne() == null || rechercheAvancePublicationForm.getNombreCuisineExterne() <= caracteristiques.getNombreCuisineExterne()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreWCDoucheInterne() == null || rechercheAvancePublicationForm.getNombreWCDoucheInterne() <= caracteristiques.getNombreWCDoucheInterne()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getNombreWCDoucheExterne() == null || rechercheAvancePublicationForm.getNombreWCDoucheExterne() <= caracteristiques.getNombreWCDoucheExterne()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getEauTde() == null || rechercheAvancePublicationForm.getEauTde() == caracteristiques.getEauTde()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getEauForage() == null || rechercheAvancePublicationForm.getEauForage() == caracteristiques.getEauForage()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getElectriciteCeet() == null || rechercheAvancePublicationForm.getElectriciteCeet() == caracteristiques.getElectriciteCeet()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getWifi() == null || rechercheAvancePublicationForm.getWifi() == caracteristiques.getWifi()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getCuisineInterne() == null || rechercheAvancePublicationForm.getCuisineInterne() == caracteristiques.getCuisineInterne()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getCuisineExterne() == null || rechercheAvancePublicationForm.getCuisineExterne() == caracteristiques.getCuisineExterne()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getWcDoucheInterne() == null || rechercheAvancePublicationForm.getWcDoucheInterne() == caracteristiques.getWcDoucheInterne()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getWcDoucheExterne() == null || rechercheAvancePublicationForm.getWcDoucheExterne() == caracteristiques.getWcDoucheExterne()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getBalcon() == null || rechercheAvancePublicationForm.getBalcon() == caracteristiques.getBalcon()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getClimatisation() == null || rechercheAvancePublicationForm.getClimatisation() == caracteristiques.getClimatisation()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getPiscine() == null || rechercheAvancePublicationForm.getPiscine() == caracteristiques.getPiscine()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getParking() == null || rechercheAvancePublicationForm.getParking() == caracteristiques.getParking()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getJardin() == null || rechercheAvancePublicationForm.getJardin() == caracteristiques.getJardin()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getTerrasse() == null || rechercheAvancePublicationForm.getTerrasse() == caracteristiques.getTerrasse()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getAscenseur() == null || rechercheAvancePublicationForm.getAscenseur() == caracteristiques.getAscenseur()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getGarage() == null || rechercheAvancePublicationForm.getGarage() == caracteristiques.getGarage()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getBaieVitree() == null || rechercheAvancePublicationForm.getBaieVitree() == caracteristiques.getBaieVitree()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getSolCarelle() == null || rechercheAvancePublicationForm.getSolCarelle() == caracteristiques.getSolCarelle()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getCashPowerPersonnel() == null || rechercheAvancePublicationForm.getCashPowerPersonnel() == caracteristiques.getCashPowerPersonnel()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getCompteurAdditionnel() == null || rechercheAvancePublicationForm.getCompteurAdditionnel() == caracteristiques.getCompteurAdditionnel()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getCompteurEau() == null || rechercheAvancePublicationForm.getCompteurEau() == caracteristiques.getCompteurEau()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getPlafonne() == null || rechercheAvancePublicationForm.getPlafonne() == caracteristiques.getPlafonne()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getDalle() == null || rechercheAvancePublicationForm.getDalle() == caracteristiques.getDalle()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getPlacard() == null || rechercheAvancePublicationForm.getPlacard() == caracteristiques.getPlacard()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getALetage() == null || rechercheAvancePublicationForm.getALetage() == caracteristiques.getALetage()) {
                return true;
            }
            if (rechercheAvancePublicationForm.getToiletteVisiteur() == null || rechercheAvancePublicationForm.getToiletteVisiteur() == caracteristiques.getToiletteVisiteur()) {
                return true;
            }
        }
        return false;
    }


}
