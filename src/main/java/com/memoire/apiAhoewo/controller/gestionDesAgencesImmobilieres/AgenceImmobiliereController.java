package com.memoire.apiAhoewo.controller.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AgenceImmobiliereController {

    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;

    @RequestMapping(value = "/agences-immobilieres", method = RequestMethod.GET)
    public List<AgenceImmobiliere> getAll() {

        List<AgenceImmobiliere> agenceImmobilieres = new ArrayList<>();
        try {
            agenceImmobilieres = this.agenceImmobiliereService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agenceImmobilieres;
    }

    @RequestMapping(value = "/agence-immobiliere/agent-immobilier", method = RequestMethod.GET)
    public List<AgenceImmobiliere> getAllByAgentImmobilier(Principal principal) {

        List<AgenceImmobiliere> agenceImmobiliere = new ArrayList<>();
        try {
            agenceImmobiliere = this.agenceImmobiliereService.getAllByAgentImmobilier(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agenceImmobiliere;
    }

    @RequestMapping(value = "/agence-immobiliere/{id}", method = RequestMethod.GET)
    public AgenceImmobiliere findById(@PathVariable Long id) {

        AgenceImmobiliere agenceImmobiliere = new AgenceImmobiliere();
        try {
            agenceImmobiliere = this.agenceImmobiliereService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return agenceImmobiliere;
    }

    @RequestMapping(value = "/agence-immobiliere/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterAgenceImmobiliere(Principal principal, @RequestBody AgenceImmobiliere agenceImmobiliere) {
        try {
            AgenceImmobiliere existingAgenceImmobiliere = agenceImmobiliereService.findByNomAgence(agenceImmobiliere.getNomAgence());

            if (existingAgenceImmobiliere != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une agence immobilière avec ce nom " + agenceImmobiliere.getNomAgence() + " existe déjà.");
            }

            agenceImmobiliere = this.agenceImmobiliereService.save(agenceImmobiliere, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du rôle : " + e.getMessage());
        }
        return ResponseEntity.ok(agenceImmobiliere);
    }

    @RequestMapping(value = "/agence-immobiliere/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierAgenceImmobiliere(Principal principal, @RequestBody AgenceImmobiliere agenceImmobiliereModifie, @PathVariable  Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findById(id);
        AgenceImmobiliere existingAgenceImmobiliere = agenceImmobiliereService.findByNomAgence(agenceImmobiliere.getNomAgence());
        try {
            if (existingAgenceImmobiliere != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une agence immobilière avec ce nom " + agenceImmobiliere.getNomAgence() + " existe déjà.");
            }
            agenceImmobiliere.setNomAgence(agenceImmobiliereModifie.getNomAgence());
            agenceImmobiliere.setAdresse(agenceImmobiliereModifie.getAdresse());
            agenceImmobiliere.setTelephone(agenceImmobiliereModifie.getTelephone());
            agenceImmobiliere.setAdresseEmail(agenceImmobiliereModifie.getAdresseEmail());
            agenceImmobiliere.setHeureOuverture(agenceImmobiliere.getHeureOuverture());
            agenceImmobiliere.setHeureFermeture(agenceImmobiliere.getHeureFermeture());
            agenceImmobiliere = this.agenceImmobiliereService.update(agenceImmobiliere, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return ResponseEntity.ok(agenceImmobiliere);
    }

    @RequestMapping(value = "/count/agences-immobilieres", method = RequestMethod.GET)
    public int nombreAgencesImmobilieres(){
        int nombres = this.agenceImmobiliereService.countAgencesImmobilieres();
        return nombres;
    }

    @RequestMapping(value = "/count/agence-immobiliere", method = RequestMethod.GET)
    public int nombreAgenceImmobiliere(Principal principal){
        int nombres = this.agenceImmobiliereService.countAgencesByAgentImmobilier(principal);
        return nombres;
    }
}
