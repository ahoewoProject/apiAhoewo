package com.memoire.apiAhoewo.controllers.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesPaiements.ComptePaiement;
import com.memoire.apiAhoewo.services.gestionDesPaiements.ComptePaiementService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ComptePaiementController {
    private final ComptePaiementService comptePaiementService;


    public ComptePaiementController(ComptePaiementService comptePaiementService) {
        this.comptePaiementService = comptePaiementService;
    }

    @RequestMapping(value = "/comptes-paiements", method = RequestMethod.GET)
    public Page<ComptePaiement> getComptesPaiements(Principal principal,
                                                    @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                    @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.comptePaiementService.getComptesPaiements(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des comptes de paiements .", e);
        }
    }

    @RequestMapping(value = "/compte-paiement/{comptePaiementId}", method = RequestMethod.GET)
    public ComptePaiement findById(@PathVariable Long comptePaiementId) {

        ComptePaiement comptePaiement = new ComptePaiement();
        try {
            comptePaiement = this.comptePaiementService.findById(comptePaiementId);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return comptePaiement;
    }

    @RequestMapping(value = "/compte-paiement/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterComptePaiement(Principal principal, @RequestBody ComptePaiement comptePaiement) {
        try {
            comptePaiement = this.comptePaiementService.save(comptePaiement, principal);
            return ResponseEntity.ok(comptePaiement);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du compte paiement : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/compte-paiement/modifier/{comptePaiementId}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> ModifierComptePaiement(Principal principal, @RequestBody ComptePaiement comptePaiementAModifie, @PathVariable  Long comptePaiementId) {
        ComptePaiement comptePaiement = comptePaiementService.findById(comptePaiementId);
        try {
            comptePaiement.setType(comptePaiementAModifie.getType());
            comptePaiement.setContact(comptePaiementAModifie.getContact());
            comptePaiement = this.comptePaiementService.update(comptePaiement, principal);
            return ResponseEntity.ok(comptePaiement);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du compte paiement : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer-user/compte-paiement/{comptePaiementId}", method = RequestMethod.GET)
    public void activerComptePaiementByPersonne(@PathVariable Long comptePaiementId, Principal principal) {
        this.comptePaiementService.activerComptePaiementByPersonne(principal, comptePaiementId);
    }

    @RequestMapping(value = "/desactiver-user/compte-paiement/{comptePaiementId}", method = RequestMethod.GET)
    public void desactiverComptePaiementByPersonne(@PathVariable Long comptePaiementId, Principal principal) {
        this.comptePaiementService.desactiverComptePaiementByPersonne(principal, comptePaiementId);
    }

    @RequestMapping(value = "/activer-agence/compte-paiement/{comptePaiementId}/{agenceId}", method = RequestMethod.GET)
    public void activerComptePaiementByAgence(@PathVariable Long comptePaiementId, @PathVariable Long agenceId) {
        this.comptePaiementService.activerComptePaiementByAgence(comptePaiementId, agenceId);
    }

    @RequestMapping(value = "/desactiver-agence/compte-paiement/{comptePaiementId}/{agenceId}", method = RequestMethod.GET)
    public void desactiverComptePaiementByAgence(@PathVariable Long comptePaiementId, @PathVariable Long agenceId) {
        this.comptePaiementService.desactiverComptePaiementByAgence(comptePaiementId, agenceId);
    }

}
