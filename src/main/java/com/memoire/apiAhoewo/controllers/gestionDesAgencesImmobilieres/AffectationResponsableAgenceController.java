package com.memoire.apiAhoewo.controllers.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.dto.AffectationResponsableAgenceForm;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.AffectationResponsableAgenceService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesComptes.ResponsableAgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AffectationResponsableAgenceController {
    @Autowired
    private AffectationResponsableAgenceService affectationResponsableAgenceService;
    @Autowired
    private ResponsableAgenceImmobiliereService responsableAgenceImmobiliereService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/affectations-responsable-agence/list", method = RequestMethod.GET)
    public List<AffectationResponsableAgence> getAffectationsResponsableAgenceList(Principal principal) {

        List<AffectationResponsableAgence> affectationResponsableAgenceList = new ArrayList<>();
        try {
            affectationResponsableAgenceList = this.affectationResponsableAgenceService.getAffectationsResponsableAgenceList(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return affectationResponsableAgenceList;
    }

    @RequestMapping(value = "/affectations-responsable-agence/page", method = RequestMethod.GET)
    public Page<AffectationResponsableAgence> getAffectationsReponsableAgencePage(Principal principal,
                                                                        @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                                        @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.affectationResponsableAgenceService.getAffectationsReponsableAgencePage(numeroDeLaPage, elementsParPage, principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des affectations responsables agences.", e);
        }
    }

    @RequestMapping(value = "/affectation-responsable-agence/{id}", method = RequestMethod.GET)
    public AffectationResponsableAgence detailAffectation(@PathVariable Long id) {

        AffectationResponsableAgence affectationResponsableAgence = new AffectationResponsableAgence();
        try {
            affectationResponsableAgence = this.affectationResponsableAgenceService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return affectationResponsableAgence;
    }

    @RequestMapping(value = "/affectation-responsable-agence/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterResponsableAgence(Principal principal, @RequestBody AffectationResponsableAgenceForm affectationresponsableAgenceForm) {
        AffectationResponsableAgence affectationResponsableAgence;
        try {
            if (affectationResponsableAgenceService.agenceAndResponsableExists(
                    affectationresponsableAgenceForm.getAgenceImmobiliere(),
                    affectationresponsableAgenceForm.getResponsable()))
            {
                return new ResponseEntity<>("Ce responsable à été déjà affecté à cette agence.", HttpStatus.CONFLICT);
            } else if (personneService.emailExists(affectationresponsableAgenceForm
                    .getResponsable().getEmail())) {
                return new ResponseEntity<>("Un responsable avec cette adresse e-mail existe déjà.", HttpStatus.CONFLICT);
            }
            ResponsableAgenceImmobiliere responsable = affectationresponsableAgenceForm.getResponsable();
            AgenceImmobiliere agenceImmobiliere = affectationresponsableAgenceForm.getAgenceImmobiliere();
            affectationResponsableAgence = this.affectationResponsableAgenceService.save(responsable, agenceImmobiliere, principal);
            return ResponseEntity.ok(affectationResponsableAgence);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'affectation de l'agence au responsable : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/affectation-matricule-responsable-agence/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajoutParMatriculeResponsable(Principal principal, @RequestBody AffectationResponsableAgenceForm affectationresponsableAgenceForm) {
        try {
            if (responsableAgenceImmobiliereService.matriculeExists(affectationresponsableAgenceForm.getMatricule())) {
                ResponsableAgenceImmobiliere responsable = responsableAgenceImmobiliereService.findByMatricule(affectationresponsableAgenceForm.getMatricule());

                if (affectationResponsableAgenceService.agenceAndResponsableMatriculeExists(
                        affectationresponsableAgenceForm.getAgenceImmobiliere(),
                        responsable.getMatricule())) {
                    return new ResponseEntity<>("Ce responsable a déjà été affecté à cette agence", HttpStatus.CONFLICT);
                }

                AgenceImmobiliere agenceImmobiliere = affectationresponsableAgenceForm.getAgenceImmobiliere();
                AffectationResponsableAgence affectationAgentAgence = this.affectationResponsableAgenceService.save(responsable, agenceImmobiliere, principal);
                return ResponseEntity.ok(affectationAgentAgence);
            } else {
                return new ResponseEntity<>("La matricule du responsable est introuvable", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'affectation de l'agence au responsable : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/responsable-agence/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerCompteResponsableAgence(@PathVariable Long id){
        this.affectationResponsableAgenceService.activerResponsableAgence(id);
    }

    @RequestMapping(value = "/desactiver/responsable-agence/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverCompteResponsableAgence(@PathVariable Long id){
        this.affectationResponsableAgenceService.desactiverResponsableAgence(id);
    }
}
