package com.memoire.apiAhoewo.controllers.gestionDesBiensImmobiliers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.dto.DelegationGestionForm1;
import com.memoire.apiAhoewo.dto.DelegationGestionForm2;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmAssocie;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Caracteristiques;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierAssocieService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.DelegationGestionService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DelegationGestionController {
    @Autowired
    private DelegationGestionService delegationGestionService;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;
    @Autowired
    private BienImmobilierAssocieService bienImmAssocieService;

    @RequestMapping(value = "/delegations-gestions-paginees", method = RequestMethod.GET)
    public Page<DelegationGestion> getDelegationsGestionsPaginees(Principal principal,
                                                                @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                                @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.delegationGestionService.getDelegationsGestions(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des délégations de gestions.", e);
        }
    }

    @RequestMapping(value = "/delegations-gestions-list", method = RequestMethod.GET)
    public List<DelegationGestion> getDelegationsGestionsList(Principal principal) {

        List<DelegationGestion> delegationGestions = new ArrayList<>();
        try {
            delegationGestions = this.delegationGestionService.getDelegationsGestions(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return delegationGestions;
    }

    @RequestMapping(value = "/delegation-gestion/{id}", method = RequestMethod.GET)
    public DelegationGestion findById(@PathVariable Long id) {

        DelegationGestion delegationGestion = new DelegationGestion();
        try {
            delegationGestion = this.delegationGestionService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return delegationGestion;
    }

    @RequestMapping(value = "/delegation-gestion/bien-immobilier/{idBienImmobilier}", method = RequestMethod.GET)
    public DelegationGestion findByBienImmobilier(@PathVariable Long idBienImmobilier) {

        BienImmobilier bienImmobilier = bienImmobilierService.findById(idBienImmobilier);
        DelegationGestion delegationGestion = new DelegationGestion();
        try {
            delegationGestion = this.delegationGestionService.findByBienImmobilier(bienImmobilier);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return delegationGestion;
    }

    @RequestMapping(value = "/delegation-gestion/ajouter/matricule", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterDelegationMatricule(Principal principal, @RequestBody DelegationGestionForm1 delegationGestionForm) {
        try {
            if (!personneService.matriculeExists(delegationGestionForm.getMatricule())) {
                return new ResponseEntity<>("La matricule du gestionnaire est introuvable !", HttpStatus.NOT_FOUND);
            }

            Personne personne = personneService.findByMatricule(delegationGestionForm.getMatricule());
            if (delegationGestionService.bienImmobilierAndGestionnaireExists(
                    delegationGestionForm.getBienImmobilier(), personne)) {
                return new ResponseEntity<>("Ce bien immobilier a été déjà délégué à ce gestionnaire !", HttpStatus.CONFLICT);
            } else if (delegationGestionService.bienImmobilierAndStatutDelegationAndEtatDelegationExists(
                    delegationGestionForm.getBienImmobilier(), 1, true)) {
                return new ResponseEntity<>("Ce bien immobilier a été déjà délégué à un gestionnaire !", HttpStatus.CONFLICT);
            }

            if (bienImmAssocieService.bienImmobilierExists(delegationGestionForm.getBienImmobilier())) {
                List<BienImmAssocie> bienImmAssocies = bienImmAssocieService.getBiensAssocies(delegationGestionForm.getBienImmobilier());

                for (BienImmAssocie bienImmAssocie : bienImmAssocies) {
                    if (delegationGestionService.bienImmobilierAndStatutDelegationAndEtatDelegationExists(
                            bienImmAssocie, 1, true)) {
                        return new ResponseEntity<>("Un bien immobilier se trouvant dans cette propriété a été déjà délégué à un gestionnaire !", HttpStatus.CONFLICT);
                    }
                    bienImmAssocie.setEstDelegue(true);
                    bienImmobilierService.update(bienImmAssocie, principal);
                    DelegationGestion delegationGestion = new DelegationGestion();
                    delegationGestion.setGestionnaire(personne);
                    delegationGestion.setBienImmobilier(bienImmAssocie);
                    delegationGestion.setPorteeGestion(false);
                    delegationGestionService.save(delegationGestion, principal);
                }
            }

            delegationGestionForm.getBienImmobilier().setEstDelegue(true);
            bienImmobilierService.update(delegationGestionForm.getBienImmobilier(), principal);
            DelegationGestion delegationGestion = new DelegationGestion();
            delegationGestion.setGestionnaire(personne);
            delegationGestion.setBienImmobilier(delegationGestionForm.getBienImmobilier());
            delegationGestion.setPorteeGestion(true);
            delegationGestion = delegationGestionService.save(delegationGestion, principal);

            return ResponseEntity.ok(delegationGestion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du type de bien : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/delegation-gestion/ajouter/code-agence", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterDelegationCodeAgence(Principal principal, @RequestBody DelegationGestionForm1 delegationGestionForm) {

        try {

            if (agenceImmobiliereService.codeAgenceExists(delegationGestionForm.getCodeAgence())) {
                AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findByCodeAgence(
                        delegationGestionForm.getCodeAgence());
                if (delegationGestionService.bienImmobilierAndAgenceImmobiliereExists(
                        delegationGestionForm.getBienImmobilier(), agenceImmobiliere
                )) {
                    return new ResponseEntity<>("Ce bien immobilier a été déjà délégué à cette agence immobilière !", HttpStatus.CONFLICT);
                } else {
                    if (delegationGestionService.bienImmobilierAndStatutDelegationAndEtatDelegationExists(
                            delegationGestionForm.getBienImmobilier(), 1, true)
                    ) {
                        return new ResponseEntity<>("Ce bien immobilier a été déjà délégué à une agence immobilière !", HttpStatus.CONFLICT);
                    }
                }

                if (bienImmAssocieService.bienImmobilierExists(delegationGestionForm.getBienImmobilier())) {
                    List<BienImmAssocie> bienImmAssocies = bienImmAssocieService.getBiensAssocies(delegationGestionForm.getBienImmobilier());

                    for (BienImmAssocie bienImmAssocie : bienImmAssocies) {
                        if (delegationGestionService.bienImmobilierAndStatutDelegationAndEtatDelegationExists(
                                bienImmAssocie, 1, true)) {
                            return new ResponseEntity<>("Un bien immobilier se trouvant dans cette propriété a été déjà délégué à une agence immobilière !", HttpStatus.CONFLICT);
                        } else {
                            bienImmAssocie.setEstDelegue(true);
                            bienImmobilierService.update(bienImmAssocie, principal);

                            DelegationGestion delegationGestion = new DelegationGestion();
                            delegationGestion.setAgenceImmobiliere(agenceImmobiliere);
                            delegationGestion.setBienImmobilier(bienImmAssocie);
                            delegationGestion.setPorteeGestion(false);
                            delegationGestionService.save(delegationGestion, principal);
                        }
                    }
                }

                delegationGestionForm.getBienImmobilier().setEstDelegue(true);
                bienImmobilierService.update(delegationGestionForm.getBienImmobilier(), principal);

                DelegationGestion delegationGestion = new DelegationGestion();
                delegationGestion.setAgenceImmobiliere(agenceImmobiliere);
                delegationGestion.setBienImmobilier(delegationGestionForm.getBienImmobilier());
                delegationGestion.setPorteeGestion(true);
                delegationGestion = this.delegationGestionService.save(delegationGestion, principal);
                return ResponseEntity.ok(delegationGestion);
            } else {
                return new ResponseEntity<>("Le code de l'agence immobilière est introuvable !", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du type de bien : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/delegation-gestion/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterDelegationGestion(Principal principal,
                                                      @RequestParam(value = "images", required = false) List<MultipartFile> files,
                                                      String delegationGestionJson,
                                                      @RequestParam(value = "caracteristiquesJson", required = false) String caracteristiquesJson)
    {
        DelegationGestion delegationGestion;
        try {
            DelegationGestionForm2 delegationGestionForm2 = new DelegationGestionForm2();
            Caracteristiques caracteristique =  new Caracteristiques();
            if (delegationGestionJson != null && !delegationGestionJson.isEmpty()) {
                delegationGestionForm2 = new ObjectMapper().readValue(delegationGestionJson, DelegationGestionForm2.class);
            }

            if (caracteristiquesJson != null && !caracteristiquesJson.trim().isEmpty() && !caracteristiquesJson.equals("{}")) {
                caracteristique = new ObjectMapper().readValue(caracteristiquesJson, Caracteristiques.class);
            }

            if (isTypeDeBien(delegationGestionForm2.getTypeDeBien().getDesignation())) {
                if (bienImmobilierService.existsByCodeBien(delegationGestionForm2.getMatriculeBienImmo())) {
                    delegationGestion = delegationGestionService.saveDelegationGestion2(delegationGestionForm2, caracteristique, files, principal);
                    return ResponseEntity.ok(delegationGestion);
                } else {
                    return new ResponseEntity<>("Le code du bien auquel est associé ce bien est introuvable !", HttpStatus.NOT_FOUND);
                }
            } else {
                if (personneService.matriculeExists(delegationGestionForm2.getMatriculeProprietaire())) {
                    delegationGestion = delegationGestionService.saveDelegationGestion2(delegationGestionForm2, caracteristique, files, principal);
                    return ResponseEntity.ok(delegationGestion);
                } else {
                    return new ResponseEntity<>("La matricule du propriétaire est introuvable !", HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'enregistrement de la délégation : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/accepter/delegation-gestion/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public ResponseEntity<?> accepterDelegationGestion(@PathVariable Long id) {
        DelegationGestion delegationGestion = delegationGestionService.findById(id);
//        if (delegationGestionService.bienImmobilierAndStatutDelegationExists(delegationGestion.getBienImmobilier(), 1)) {
//            return new ResponseEntity<>("Délégation de gestion déjà acceptée", HttpStatus.BAD_REQUEST);
//        }
//        else {
//            this.delegationGestionService.accepterDelegationGestion(delegationGestion.getId());
//            return ResponseEntity.ok(delegationGestion);
//        }

        this.delegationGestionService.accepterDelegationGestion(delegationGestion.getId());
        return ResponseEntity.ok(delegationGestion);
    }

    @RequestMapping(value = "/refuser/delegation-gestion/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void refuserDelegationGestion(@PathVariable Long id) {
        this.delegationGestionService.refuserDelegationGestion(id);
    }

    @RequestMapping(value = "/delegation-gestion/activer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void activerDelegationGestion(@PathVariable Long id, Principal principal) {
        this.delegationGestionService.activerDelegationGestion(id, principal);
    }

    @RequestMapping(value = "/delegation-gestion/desactiver/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void desactiverDelegationGestion(@PathVariable Long id, Principal principal) {
        this.delegationGestionService.desactiverDelegationGestion(id, principal);
    }


    private boolean isTypeDeBien(String designation) {
        return designation.equals("Appartement") ||
                designation.equals("Chambre salon") ||
                designation.equals("Chambre") ||
                designation.equals("Bureau") ||
                designation.equals("Boutique") ||
                designation.equals("Magasin");
    }
}
