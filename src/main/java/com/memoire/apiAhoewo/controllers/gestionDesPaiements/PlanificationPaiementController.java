package com.memoire.apiAhoewo.controllers.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesPaiements.PlanificationPaiement;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PlanificationPaiementService;
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
public class PlanificationPaiementController {
    @Autowired
    private PlanificationPaiementService planificationPaiementService;

    @RequestMapping(value = "/planifications-paiements", method = RequestMethod.GET)
    public Page<PlanificationPaiement> getPlanificationsPaiement(Principal principal,
                                                   @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                   @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.planificationPaiementService.getPlanificationsPaiement(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des planifications de paiement .", e);
        }
    }

    @RequestMapping(value = "/planification-paiement/{id}", method = RequestMethod.GET)
    public PlanificationPaiement findById(@PathVariable Long id) {

        PlanificationPaiement planificationPaiement = new PlanificationPaiement();
        try {
            planificationPaiement = this.planificationPaiementService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return planificationPaiement;
    }

    @RequestMapping(value = "last-planification-paiement/{codeContrat}", method = RequestMethod.GET)
    public PlanificationPaiement lastPlanificationPaiement(@PathVariable String codeContrat) {

        PlanificationPaiement planificationPaiement = new PlanificationPaiement();
        try {
            planificationPaiement = this.planificationPaiementService.dernierePlanificationPaiementAchat(codeContrat);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return planificationPaiement;
    }

    @RequestMapping(value = "planifications-paiements/{codeContrat}", method = RequestMethod.GET)
    public List<PlanificationPaiement> getPlanificationsByCodeContrat(@PathVariable String codeContrat) {

        List<PlanificationPaiement> planificationPaiementList =  new ArrayList<>();
        try {
            planificationPaiementList = this.planificationPaiementService.getPlanificationsByCodeContrat(codeContrat);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return planificationPaiementList;
    }

    @RequestMapping(value = "/planification-paiement/location/ajouter", method = RequestMethod.POST)
    public ResponseEntity<?> ajouterPlanificationPaiementLocation(Principal principal, @RequestBody PlanificationPaiement planificationPaiement) {
        try {
            if (planificationPaiementService.existsByContratAndDatePlanifiee(planificationPaiement.getContrat(), planificationPaiement.getDatePlanifiee())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une planification de paiement existe déjà pour ce contrat à la date spécifiée.");
            } else {
                PlanificationPaiement paiementAdd =  this.planificationPaiementService.savePlanificationPaiementLocation(principal, planificationPaiement);
                return ResponseEntity.status(HttpStatus.OK).body(paiementAdd);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de la planification du paiement de location : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/planification-paiement/achat/ajouter", method = RequestMethod.POST)
    public ResponseEntity<?> ajouterPlanificationAchat(Principal principal, @RequestBody PlanificationPaiement planificationPaiement) {
        try {
            if (planificationPaiementService.existsByContratAndDatePlanifiee(planificationPaiement.getContrat(), planificationPaiement.getDatePlanifiee())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une planification de paiement existe déjà pour ce contrat à la date spécifiée.");
            } else {
                PlanificationPaiement paiementAdd =  this.planificationPaiementService.savePlanificationPaiementLocation(principal, planificationPaiement);
                return ResponseEntity.status(HttpStatus.OK).body(paiementAdd);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de la planification du paiement d'achat : " + e.getMessage());
        }
    }
}
