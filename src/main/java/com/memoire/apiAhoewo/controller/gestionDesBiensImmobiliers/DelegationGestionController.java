package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.DelegationGestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DelegationGestionController {
    @Autowired
    private DelegationGestionService delegationGestionService;

    @RequestMapping(value = "/delegations-gestions/proprietaire", method = RequestMethod.GET)
    public List<DelegationGestion> getAllByProprietaire(Principal principal) {

        List<DelegationGestion> delegationGestions = new ArrayList<>();
        try {
            delegationGestions = this.delegationGestionService.getAllByProprietaire(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return delegationGestions;
    }

    @RequestMapping(value = "/delegations-gestions/gestionnaire", method = RequestMethod.GET)
    public List<DelegationGestion> getAllByGestionnaire(Principal principal) {

        List<DelegationGestion> delegationGestions = new ArrayList<>();
        try {
            delegationGestions = this.delegationGestionService.getAllByGestionnaire(principal);
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

    @RequestMapping(value = "/delegation-gestion/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterDelegationGestion(Principal principal, @RequestBody DelegationGestion delegationGestion) {
        try {
            delegationGestion = this.delegationGestionService.save(delegationGestion, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du type de bien : " + e.getMessage());
        }
        return ResponseEntity.ok(delegationGestion);
    }

    @RequestMapping(value = "/accepter/delegation-gestion/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void accepterDelegationGestion(@PathVariable Long id){
        this.delegationGestionService.accepterDelegationGestion(id);
    }

    @RequestMapping(value = "/delegation-gestion/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerDelegationGestion(@PathVariable Long id) {
        this.delegationGestionService.supprimerDelegationGestion(id);
    }

    @RequestMapping(value = "/count/delegations-gestions/proprietaire", method = RequestMethod.GET)
    public int nombreDelegationGestionProprietaire(Principal principal) {
        int nombres = this.delegationGestionService.countDelegationGestionProprietaire(principal);
        return nombres;
    }

    @RequestMapping(value = "/count/delegations-gestions/gestionnaire", method = RequestMethod.GET)
    public int nombreDelegationGestionGestionnaire(Principal principal) {
        int nombres = this.delegationGestionService.countDelegationGestionGestionnaire(principal);
        return nombres;
    }
}
