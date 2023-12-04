package com.memoire.apiAhoewo.controller.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.requestForm.AffectationAgentAgenceForm;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AffectationAgentAgenceService;
import com.memoire.apiAhoewo.service.gestionDesComptes.AgentImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/affectations-agents-agences", method = RequestMethod.GET)
    public List<AffectationAgentAgence> getAll() {

        List<AffectationAgentAgence> agentAgences = new ArrayList<>();
        try {
            agentAgences = this.affectationAgentAgenceService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agentAgences;
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
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du service : " + e.getMessage());
        }
        return ResponseEntity.ok(affectationAgentAgence);
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
                    .body("Une erreur s'est produite lors de l'ajout du service : " + e.getMessage());
        }
    }

}
