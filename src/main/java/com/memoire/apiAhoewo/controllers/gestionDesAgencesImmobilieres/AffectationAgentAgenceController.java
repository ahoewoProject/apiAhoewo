package com.memoire.apiAhoewo.controllers.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.dto.AffectationAgentAgenceForm;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.AffectationAgentAgenceService;
import com.memoire.apiAhoewo.services.gestionDesComptes.AgentImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
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
public class AffectationAgentAgenceController {
    @Autowired
    private AffectationAgentAgenceService affectationAgentAgenceService;
    @Autowired
    private AgentImmobilierService agentImmobilierService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/affectations-agent-agence/page", method = RequestMethod.GET)
    public Page<AffectationAgentAgence> getAffectationsAgentAgencePage(Principal principal, @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                     @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.affectationAgentAgenceService.getAffectationsAgentAgencePage(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des affectations agents agences.", e);
        }
    }

    @RequestMapping(value = "/affectations-agent-agence/list", method = RequestMethod.GET)
    public List<AffectationAgentAgence> getAffectationsAgentAgenceList(Principal principal) {

        List<AffectationAgentAgence> affectationAgentAgenceArrayList = new ArrayList<>();
        try {
            affectationAgentAgenceArrayList = this.affectationAgentAgenceService.getAffectationsAgentAgenceList(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return affectationAgentAgenceArrayList;
    }

    @RequestMapping(value = "/affectations-agents-agences/responsable", method = RequestMethod.GET)
    public List<AffectationAgentAgence> getAgentsOfAgence(Principal principal) {

        List<AffectationAgentAgence> agentsList = new ArrayList<>();
        try {
            agentsList = this.affectationAgentAgenceService.getAgentsByAgences(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agentsList;
    }

    @RequestMapping(value = "/affectations-agents-agences/agent", method = RequestMethod.GET)
    public List<AffectationResponsableAgence> getAgencesOfAgent(Principal principal) {

        List<AffectationResponsableAgence> agencesList = new ArrayList<>();
        try {
            agencesList = this.affectationAgentAgenceService.getAgencesByAgent(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agencesList;
    }

    @RequestMapping(value = "/affectation-agent-agence/{id}", method = RequestMethod.GET)
    public AffectationAgentAgence findById(@PathVariable Long id) {

        AffectationAgentAgence affectationAgentAgence = new AffectationAgentAgence();
        try {
            affectationAgentAgence = this.affectationAgentAgenceService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return affectationAgentAgence;
    }

    @RequestMapping(value = "/affectation-agent-agence/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterAgentAgence(Principal principal, @RequestBody AffectationAgentAgenceForm affectationAgentAgenceForm) {
        AffectationAgentAgence affectationAgentAgence;
        try {
            if (affectationAgentAgenceService.agenceAndAgentExists(
                    affectationAgentAgenceForm.getAgenceImmobiliere(),
                    affectationAgentAgenceForm.getAgentImmobilier()))
            {
                return new ResponseEntity<>("Cet agent immobilier à été déjà ajouté dans cette agence.", HttpStatus.CONFLICT);
            } else if (personneService.emailExists(affectationAgentAgenceForm
                    .getAgentImmobilier().getEmail())) {
                return new ResponseEntity<>("Un agent immobilier avec cette adresse e-mail existe déjà.", HttpStatus.CONFLICT);
            }
            AgentImmobilier agentImmobilier = affectationAgentAgenceForm.getAgentImmobilier();
            AgenceImmobiliere agenceImmobiliere = affectationAgentAgenceForm.getAgenceImmobiliere();
            affectationAgentAgence = this.affectationAgentAgenceService.save(agentImmobilier, agenceImmobiliere, principal);
            return ResponseEntity.ok(affectationAgentAgence);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'affection de l'agence à l'agent : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/affectation-matricule-agent-agence/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajoutParMatriculeAgent(Principal principal, @RequestBody AffectationAgentAgenceForm affectationAgentAgenceForm) {
        try {
            if (agentImmobilierService.matriculeExists(affectationAgentAgenceForm.getMatricule())) {
                AgentImmobilier agentImmobilier = agentImmobilierService.findByMatricule(affectationAgentAgenceForm.getMatricule());

                if (affectationAgentAgenceService.agenceAndMatriculeAgentExists(
                        affectationAgentAgenceForm.getAgenceImmobiliere(),
                        agentImmobilier.getMatricule())) {
                    return new ResponseEntity<>("Cet agent immobilier a déjà été ajouté dans cette agence", HttpStatus.CONFLICT);
                }

                AgenceImmobiliere agenceImmobiliere = affectationAgentAgenceForm.getAgenceImmobiliere();
                AffectationAgentAgence affectationAgentAgence = this.affectationAgentAgenceService.save(agentImmobilier, agenceImmobiliere, principal);
                return ResponseEntity.ok(affectationAgentAgence);
            } else {
                return new ResponseEntity<>("La matricule de l'agent immobilier est introuvable", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de l'affectation de l'agence à l'agent : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/agent-agence/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerCompteAgentAgence(@PathVariable Long id){
        this.affectationAgentAgenceService.activerCompteAgentAgence(id);
    }

    @RequestMapping(value = "/desactiver/agent-agence/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverCompteAgentAgence(@PathVariable Long id){
        this.affectationAgentAgenceService.desactiverCompteAgentAgence(id);
    }
}
