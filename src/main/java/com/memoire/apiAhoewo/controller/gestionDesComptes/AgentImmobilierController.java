package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
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
public class AgentImmobilierController {
    @Autowired
    private AgentImmobilierService agentImmobilierService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/agents-immobiliers", method = RequestMethod.GET)
    public List<AgentImmobilier> getAll() {

        List<AgentImmobilier> agentImmobilierList = new ArrayList<>();
        try {
            agentImmobilierList = this.agentImmobilierService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agentImmobilierList;
    }

    @RequestMapping(value = "/agents-immobiliers/responsable-agence-immobiliere", method = RequestMethod.GET)
    public List<AgentImmobilier> findAgentsImmobiliersByResponsable(Principal principal) {

        List<AgentImmobilier> agentImmobiliers = new ArrayList<>();
        try {
            agentImmobiliers = this.agentImmobilierService.findAgentsImmobiliersByResponsable(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agentImmobiliers;
    }

    @RequestMapping(value = "/agent-immobilier/{id}", method = RequestMethod.GET)
    public AgentImmobilier findById(@PathVariable Long id) {

        AgentImmobilier agentImmobilier = new AgentImmobilier();
        try {
            agentImmobilier = this.agentImmobilierService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return agentImmobilier;
    }

    @RequestMapping(value = "/agent-immobilier/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterAgentImmobilier(Principal principal, @RequestBody AgentImmobilier agentImmobilier) {
        try {
            if (personneService.usernameExists(agentImmobilier.getUsername())) {
                return new ResponseEntity<>("Un utilisateur avec ce nom d'utilisateur existe déjà", HttpStatus.CONFLICT);
            }

            agentImmobilier = this.agentImmobilierService.save(agentImmobilier, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de l'agent immobilier : " + e.getMessage());
        }
        return ResponseEntity.ok(agentImmobilier);
    }

    @RequestMapping(value = "/agent-immobilier/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerAgentImmobilier(@PathVariable Long id) {
        this.agentImmobilierService.deleteById(id);
    }

    @RequestMapping(value = "/count/agents-immobiliers", method = RequestMethod.GET)
    public int nombreAgentsImmobiliers(){
        int nombres = this.agentImmobilierService.countAgentImmobiliers();
        return nombres;
    }

    @RequestMapping(value = "/count/agents-immobiliers/responsable", method = RequestMethod.GET)
    public int nombreAgentsImmobiliersByResponsable(Principal principal){
        int nombres = this.agentImmobilierService.countAgentsImmobiliersByResponsable(principal);
        return nombres;
    }
}
