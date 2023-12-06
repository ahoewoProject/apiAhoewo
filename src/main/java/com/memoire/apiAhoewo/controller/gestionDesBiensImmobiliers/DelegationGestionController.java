package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.requestForm.DelegationGestionForm;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.DelegationGestionService;
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
public class DelegationGestionController {
    @Autowired
    private DelegationGestionService delegationGestionService;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;

    @RequestMapping(value = "/delegations-gestions/proprietaire", method = RequestMethod.GET)
    public List<DelegationGestion> getAllByProprietaire(Principal principal) {

        List<DelegationGestion> delegationGestions = new ArrayList<>();
        try {
            delegationGestions = this.delegationGestionService.getDelegationsByProprietaire(principal);
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
            delegationGestions = this.delegationGestionService.getDelegationsByGestionnaire(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return delegationGestions;
    }

    @RequestMapping(value = "/delegations-gestions/agences/responsable", method = RequestMethod.GET)
    public List<DelegationGestion> getDelegationsGestionsOfAgencesByResponsable(Principal principal) {

        List<DelegationGestion> delegationGestions = new ArrayList<>();
        try {
            delegationGestions = this.delegationGestionService.getDelegationsOfAgencesByResponsable(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return delegationGestions;
    }

    @RequestMapping(value = "/delegations-gestions/agences/agent", method = RequestMethod.GET)
    public List<DelegationGestion> getDelegationsGestionsOfAgencesByAgent(Principal principal) {

        List<DelegationGestion> delegationGestions = new ArrayList<>();
        try {
            delegationGestions = this.delegationGestionService.getDelegationsOfAgencesByAgent(principal);
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

    @RequestMapping(value = "/delegation-gestion/ajouter/matricule", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterDelegationMatricule(Principal principal, @RequestBody DelegationGestionForm delegationGestionForm) {
        DelegationGestion delegationGestion = new DelegationGestion();
        try {

            if (personneService.matriculeExists(delegationGestionForm.getMatricule())) {
                Personne personne = personneService.findByMatricule(delegationGestionForm.getMatricule());
                if (delegationGestionService.bienImmobilierAndStatutDelegationExists(
                        delegationGestionForm.getBienImmobilier(), 1)
                ) {
                    return new ResponseEntity<>("Ce bien immobilier a été déjà délégué à un gestionnaire !", HttpStatus.CONFLICT);
                }
                delegationGestion.setGestionnaire(personne);
                delegationGestion.setBienImmobilier(delegationGestionForm.getBienImmobilier());
                delegationGestion = this.delegationGestionService.save(delegationGestion, principal);
                return ResponseEntity.ok(delegationGestion);
            } else {
                return new ResponseEntity<>("La matricule du gestionnaire est introuvable !", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du type de bien : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/delegation-gestion/ajouter/code-agence", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterDelegationCodeAgence(Principal principal, @RequestBody DelegationGestionForm delegationGestionForm) {
        DelegationGestion delegationGestion = new DelegationGestion();
        try {

            if (agenceImmobiliereService.codeAgenceExists(delegationGestionForm.getCodeAgence())) {
                AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findByCodeAgence(
                        delegationGestionForm.getCodeAgence());
                if (delegationGestionService.bienImmobilierAndStatutDelegationExists(
                        delegationGestionForm.getBienImmobilier(), 1)
                ) {
                    return new ResponseEntity<>("Ce bien immobilier a été déjà délégué à une agence immobilière !", HttpStatus.CONFLICT);
                }
                delegationGestion.setAgenceImmobiliere(agenceImmobiliere);
                delegationGestion.setBienImmobilier(delegationGestionForm.getBienImmobilier());
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

    @RequestMapping(value = "/accepter/delegation-gestion/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public ResponseEntity<?> accepterDelegationGestion(@PathVariable Long id) {
        DelegationGestion delegationGestion = delegationGestionService.findById(id);
        if (delegationGestionService.bienImmobilierAndStatutDelegationExists(delegationGestion.getBienImmobilier(), 1)) {
            return new ResponseEntity<>("Délégation de gestion déjà acceptée", HttpStatus.BAD_REQUEST);
        }
        else {
            this.delegationGestionService.accepterDelegationGestion(delegationGestion.getId());
            return ResponseEntity.ok(delegationGestion);
        }
    }

    @RequestMapping(value = "/refuser/delegation-gestion/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void refuserDelegationGestion(@PathVariable Long id) {
        this.delegationGestionService.refuserDelegationGestion(id);
    }

    @RequestMapping(value = "/delegation-gestion/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerDelegationGestion(@PathVariable Long id) {
        this.delegationGestionService.supprimerDelegationGestion(id);
    }

}
