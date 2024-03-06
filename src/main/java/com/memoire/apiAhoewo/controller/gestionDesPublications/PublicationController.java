package com.memoire.apiAhoewo.controller.gestionDesPublications;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.*;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.requestForm.RechercheAvanceePublicationForm;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.*;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.service.gestionDesPublications.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PublicationController {
    private final PublicationService publicationService;
    private final PersonneService personneService;
    private final TypeDeBienService typeDeBienService;
    private final DelegationGestionService delegationGestionService;
    private final BienImmobilierAssocieService bienImmAssocieService;
    @Autowired
    private QuartierService quartierService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;

    public PublicationController(PublicationService publicationService, PersonneService personneService,
                                 TypeDeBienService typeDeBienService, BienImmobilierAssocieService bienImmobilierAssocieService,
                                 DelegationGestionService delegationGestionService) {
        this.publicationService = publicationService;
        this.personneService = personneService;
        this.typeDeBienService = typeDeBienService;
        this.delegationGestionService = delegationGestionService;
        this.bienImmAssocieService = bienImmobilierAssocieService;
    }

    @RequestMapping(value = "/publications/actives/agence/{nomAgence}", method = RequestMethod.GET)
    public Page<Publication> getPublicationsActivesByAgence(
            @PathVariable String nomAgence,
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findByNomAgence(nomAgence);
            return this.publicationService.getPublicationsActivesByAgence(agenceImmobiliere, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications/actives/personne/{email}", method = RequestMethod.GET)
    public Page<Publication> getPublicationsActivesByPersonne(
            @PathVariable String email,
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            Personne personne = personneService.findByEmail(email);
            return this.publicationService.getPublicationsActivesByPersonne(personne, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value ="/publications/actives/recherche-simple", method = RequestMethod.GET)
    public Page<Publication> rechercheSimpleDePublicationsActives(
            @RequestParam(required = false) String typeDeTransaction,
            @RequestParam(required = false) Long typeDeBienId,
            @RequestParam(required = false) Long quartierId,
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        TypeDeBien typeDeBien = typeDeBienService.findById(typeDeBienId);
        Quartier quartier = quartierService.findById(quartierId);

        try {
            return publicationService.rechercheSimpleDePublicationsActives(typeDeTransaction, typeDeBien, quartier, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications/actives/recherche-avancee", method = RequestMethod.POST)
    public Page<Publication> rechercheAvanceeDePublicationsActives(
            @RequestBody RechercheAvanceePublicationForm rechercheAvanceePublicationForm,
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return publicationService.rechercheAvanceeDePublicationsActives(rechercheAvanceePublicationForm, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications/actives", method = RequestMethod.GET)
    public Page<Publication> getPublicationsActives(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.publicationService.getPublicationsActives(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications/actives/location", method = RequestMethod.GET)
    public Page<Publication> getPublicationsActivesByLocation(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.publicationService.getPublicationsActivesByTypeDeTransaction("Location", numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications/actives/vente", method = RequestMethod.GET)
    public Page<Publication> getPublicationsActivesByVente(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.publicationService.getPublicationsActivesByTypeDeTransaction("Vente", numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications/actives/type-de-bien/{designation}", method = RequestMethod.GET)
    public Page<Publication> getPublicationsActivesByTypeDeBien(
            @PathVariable String designation,
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            TypeDeBien typeDeBien = typeDeBienService.findByDesignation(designation);
            return this.publicationService.getPublicationsActivesByTypeDeBien(typeDeBien, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications/actives/type-de-bien-list", method = RequestMethod.GET)
    public Page<Publication> getPublicationsActivesByTypeDeBienList(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.publicationService.getPublicationsActivesByTypeDeBienList(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications/actives/region/{libelle}", method = RequestMethod.GET)
    public Page<Publication> getPublicationsActivesByRegion(
            @PathVariable String libelle,
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            Region region = regionService.findByLibelle(libelle);
            return this.publicationService.getPublicationsActivesByRegion(region, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications/actives/region-list", method = RequestMethod.GET)
    public Page<Publication> getPublicationsActivesByRegionsList(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.publicationService.getPublicationsActivesByRegionsList(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publications", method = RequestMethod.GET)
    public Page<Publication> getPublications(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage, Principal principal) {

        try {
            return this.publicationService.getPublications(numeroDeLaPage, elementsParPage, principal);
        } catch (Exception e) {

            throw new RuntimeException("Une erreur s'est produite lors de la récupération des publications.", e);
        }
    }

    @RequestMapping(value = "/publication/{id}", method = RequestMethod.GET)
    public Publication findById(@PathVariable Long id) {

        Publication publication = new Publication();
        try {
            publication = this.publicationService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return publication;
    }

    @RequestMapping(value = "/publication/code/{code}", method = RequestMethod.GET)
    public Publication findByCodePublication(@PathVariable String code) {

        Publication publication = new Publication();
        try {
            publication = this.publicationService.findByCodePublication(code);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return publication;
    }

    @RequestMapping(value = "/publication/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterPublication(Principal principal, @RequestBody Publication publication) {
        Personne personne = personneService.findByUsername(principal.getName());
        String roleCode = personne.getRole().getCode();

        try {
            if (publicationService.existsByBienImmobilierAndEtat(publication.getBienImmobilier(), true)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une publication avec ce bien est toujours active. Veuillez désactiver la publication avant d'en ajouter une autre.");
            } else if (typeDeBienService.isTypeBienSupport(publication.getBienImmobilier().getTypeDeBien().getDesignation())) {
                List<BienImmAssocie> bienImmAssocieList = bienImmAssocieService.getBiensAssocies(publication.getBienImmobilier());
                for (BienImmAssocie bienImmAssocie : bienImmAssocieList) {
                    if (publicationService.existsByBienImmobilierAndEtat(bienImmAssocie.getBienImmobilier(), true)) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("Une publication avec un des biens associés à ce bien est toujours active. Veuillez désactiver la publication avant d'en ajouter une autre.");
                    }
                }
            } else if (typeDeBienService.isTypeBienAssocie(publication.getBienImmobilier().getTypeDeBien().getDesignation())) {
                BienImmAssocie bienImmAssocie = bienImmAssocieService.findById(publication.getBienImmobilier().getId());
                if (publicationService.existsByBienImmobilierAndEtat(bienImmAssocie.getBienImmobilier(), true)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Une publication avec le bien support auquel est associé ce bien est toujours active. Veuillez désactiver la publication avant d'en ajouter une autre.");
                }
            }

//            System.out.println("------------------------");
            if (roleCode.equals("ROLE_RESPONSABLE") || roleCode.equals("ROLE_AGENTIMMOBILIER")) {
                DelegationGestion delegationGestion = delegationGestionService.getDelegationByBienImmobilierAndEtatDelegation(publication.getBienImmobilier(), true);
                if (delegationGestion != null) {
                    publication.setAgenceImmobiliere(delegationGestion.getAgenceImmobiliere());
                } else {
                    publication.setAgenceImmobiliere(publication.getBienImmobilier().getAgenceImmobiliere());
                }
            } else {
                publication.setPersonne(personne);
            }

            publication = publicationService.save(publication, principal);
            return ResponseEntity.ok().body(publication);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de la publication : " + e.getMessage());
        }
    }


    @RequestMapping(value = "/publication/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierPublication(@PathVariable Long id, Principal principal, @RequestBody Publication publication) {
        Personne personne = personneService.findByUsername(principal.getName());

        Publication publicationExistante = publicationService.findById(id);
        String roleCode = personne.getRole().getCode();

        try {
//            if (publicationService.existsByBienImmobilierAndEtat(publicationExistante.getBienImmobilier(), true) == publicationService.existsByBienImmobilierAndEtat(publication.getBienImmobilier(), true)) {
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                        .body("Ce bien immobilier a déjà été publié");
//            } else if (typeDeBienService.isTypeBienSupport(publication.getBienImmobilier().getTypeDeBien().getDesignation())) {
//                List<BienImmAssocie> bienImmAssocieList = bienImmAssocieService.getBiensAssocies(publication.getBienImmobilier());
//                for (BienImmAssocie bienImmAssocie : bienImmAssocieList) {
//                    if (publicationService.existsByBienImmobilierAndEtat(bienImmAssocie.getBienImmobilier(), true)) {
//                        return ResponseEntity.status(HttpStatus.CONFLICT)
//                                .body("Un des biens immobiliers associés a déjà été publié");
//                    }
//                }
//            } else if (typeDeBienService.isTypeBienAssocie(publication.getBienImmobilier().getTypeDeBien().getDesignation())) {
//                BienImmAssocie bienImmAssocie = bienImmAssocieService.findById(publication.getBienImmobilier().getId());
//                if (publicationService.existsByBienImmobilierAndEtat(bienImmAssocie.getBienImmobilier(), true)) {
//                    return ResponseEntity.status(HttpStatus.CONFLICT)
//                            .body("Le bien support auquel est associé ce bien a été déjà publié");
//                }
//            }

            publicationExistante.setLibelle(publication.getLibelle());
            publicationExistante.setBienImmobilier(publication.getBienImmobilier());
            publicationExistante.setPrixDuBien(publication.getPrixDuBien());
            publicationExistante.setCommission(publication.getCommission());

            if (publication.getTypeDeTransaction().equals("Location")) {
                publicationExistante.setAvance(publication.getAvance());
                publicationExistante.setCaution(publication.getCaution());
            } else {
                publicationExistante.setAvance(null);
                publicationExistante.setCaution(null);
            }

            if (roleCode.equals("ROLE_RESPONSABLE") || roleCode.equals("ROLE_AGENTIMMOBILIER")) {
                DelegationGestion delegationGestion = delegationGestionService.getDelegationByBienImmobilierAndEtatDelegation(publication.getBienImmobilier(), true);
                publicationExistante.setFraisDeVisite(publication.getFraisDeVisite());
                publicationExistante.setPersonne(null);

                if (delegationGestion != null) {
                    publicationExistante.setAgenceImmobiliere(delegationGestion.getAgenceImmobiliere());
                } else {
                    publicationExistante.setAgenceImmobiliere(publication.getBienImmobilier().getAgenceImmobiliere());
                }
            } else {
                publicationExistante.setPersonne(personne);
            }

            publicationExistante = publicationService.save(publicationExistante, principal);
            return ResponseEntity.ok().body(publicationExistante);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de la publication : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/publication/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerPublication(@PathVariable Long id){
        this.publicationService.activerPublication(id);
    }

    @RequestMapping(value = "/desactiver/publication/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverPublication(@PathVariable Long id){
        this.publicationService.desactiverPublication(id);
    }


}
