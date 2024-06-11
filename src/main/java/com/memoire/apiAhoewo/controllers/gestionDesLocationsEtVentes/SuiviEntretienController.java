package com.memoire.apiAhoewo.controllers.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.SuiviEntretien;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.SuiviEntretienService;
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
public class SuiviEntretienController {
    @Autowired
    private SuiviEntretienService suiviEntretienService;

    @RequestMapping(value = "/suivis-entretiens", method = RequestMethod.GET)
    public Page<SuiviEntretien> getSuiviEntretiens(Principal principal,
                                                      @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                      @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.suiviEntretienService.getSuivisEntretiens(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des suivis d'entretiens .", e);
        }
    }

    @RequestMapping(value = "/suivis-entretiens/par-code-contrat/{codeContrat}", method = RequestMethod.GET)
    public Page<SuiviEntretien> getSuiviEntretiensByCodeContratLocation(@PathVariable String codeContrat,
                                                   @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                   @RequestParam(value = "elementsParPage") int elementsParPage) {
        try {
            return this.suiviEntretienService.getSuivisEntretiensByCodeContratLocation(codeContrat, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des suivis d'entretiens .", e);
        }
    }

    @RequestMapping(value = "/suivis-entretiens-list", method = RequestMethod.GET)
    public List<SuiviEntretien> getSuivisEntretiensList(Principal principal) {

        List<SuiviEntretien> suiviEntretienList = new ArrayList<>();
        try {
            suiviEntretienList = this.suiviEntretienService.getSuivisEntretiens(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return suiviEntretienList;
    }

    @RequestMapping(value = "/suivi-entretien/{id}", method = RequestMethod.GET)
    public SuiviEntretien findById(@PathVariable Long id) {

        SuiviEntretien suiviEntretien = new SuiviEntretien();
        try {
            suiviEntretien = this.suiviEntretienService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return suiviEntretien;
    }

    @RequestMapping(value = "/suivi-entretien/ajouter", method = RequestMethod.POST)
    public ResponseEntity<?> ajouterSuiviEntretien(Principal principal, @RequestBody SuiviEntretien suiviEntretien) {
        try {
            SuiviEntretien suiviEntretienAdd = this.suiviEntretienService.save(suiviEntretien, principal);
            return ResponseEntity.status(HttpStatus.OK).body(suiviEntretienAdd);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la soumission du suivi d'entretien : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/suivi-entretien/modifier/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> modifierSuiviEntretien(@PathVariable Long id, Principal principal, @RequestBody SuiviEntretien suiviEntretienEdit) {
        try {
            suiviEntretienEdit.setId(id);
            SuiviEntretien suiviEntretienUpdate = suiviEntretienService.update(suiviEntretienEdit, principal);
            return ResponseEntity.status(HttpStatus.OK).body(suiviEntretienUpdate);

        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du suivi d'entretien : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/suivi-entretien/debuter/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void debuterEntretien(@PathVariable Long id, Principal principal) {
        this.suiviEntretienService.debuterEntretien(id, principal);
    }

    @RequestMapping(value = "/suivi-entretien/terminer/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void terminerEntretien(@PathVariable Long id, Principal principal) {
        this.suiviEntretienService.terminerEntretien(id, principal);
    }
}
