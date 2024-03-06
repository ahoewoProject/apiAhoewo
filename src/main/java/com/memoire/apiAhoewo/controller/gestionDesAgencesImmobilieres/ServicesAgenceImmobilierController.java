package com.memoire.apiAhoewo.controller.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;
import com.memoire.apiAhoewo.requestForm.ServiceNonTrouveForm;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ServicesAgenceImmobilierController {
    @Autowired
    private ServicesAgenceImmobiliereService servicesAgenceImmobiliereService;

    @RequestMapping(value = "/services/agence-immobiliere/pagines", method = RequestMethod.GET)
    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(Principal principal,
                                                                @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                                @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.servicesAgenceImmobiliereService.getServicesOfAgencePagines(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des services de l'agence.", e);
        }
    }

    @RequestMapping(value = "/services/agence-immobiliere/pagines/{id}", method = RequestMethod.GET)
    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(@PathVariable Long id,
                                                                      @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                                      @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.servicesAgenceImmobiliereService.getServicesOfAgencePagines(id, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des services de l'agence.", e);
        }
    }

    @RequestMapping(value = "/services/agence/{nomAgence}", method = RequestMethod.GET)
    public Page<ServicesAgenceImmobiliere> getServicesByNomAgence(@PathVariable String nomAgence,
                                                                      @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                                      @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.servicesAgenceImmobiliereService.getServicesByNomAgence(nomAgence, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des services de l'agence.", e);
        }
    }

    @RequestMapping(value = "/service/agence-immobiliere/{id}", method = RequestMethod.GET)
    public ServicesAgenceImmobiliere findById(@PathVariable Long id) {

        ServicesAgenceImmobiliere servicesAgenceImmobiliere = new ServicesAgenceImmobiliere();
        try {
            servicesAgenceImmobiliere = this.servicesAgenceImmobiliereService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return servicesAgenceImmobiliere;
    }

    @RequestMapping(value = "/service/agence-immobiliere/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterServiceAgence(Principal principal, @RequestBody ServicesAgenceImmobiliere servicesAgenceImmobiliere) {
        try {
            if (servicesAgenceImmobiliereService.servicesAndAgenceImmobiliereExists(
                    servicesAgenceImmobiliere.getServices(), servicesAgenceImmobiliere.getAgenceImmobiliere())
            ) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un service avec ce nom " + servicesAgenceImmobiliere.getServices().getNomService() +
                                " existe déjà dans cette agence.");
            }
            servicesAgenceImmobiliere = this.servicesAgenceImmobiliereService.save(servicesAgenceImmobiliere, principal);
            return ResponseEntity.ok(servicesAgenceImmobiliere);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du service : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/service/agence-immobiliere/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierServiceAgence(Principal principal, @RequestBody ServicesAgenceImmobiliere servicesModifie, @PathVariable  Long id) {
        ServicesAgenceImmobiliere servicesAgenceImmobiliere = servicesAgenceImmobiliereService.findById(id);
        try {
            if (servicesAgenceImmobiliereService.servicesAndAgenceImmobiliereExists(
                    servicesModifie.getServices(), servicesModifie.getAgenceImmobiliere())
            ) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un service avec ce nom " + servicesAgenceImmobiliere.getServices().getNomService() +
                                " existe déjà dans cette agence.");
            }
            servicesAgenceImmobiliere.setServices(servicesModifie.getServices());
            servicesAgenceImmobiliere.setAgenceImmobiliere(servicesModifie.getAgenceImmobiliere());
            servicesAgenceImmobiliere = this.servicesAgenceImmobiliereService.update(servicesAgenceImmobiliere, principal);
            return ResponseEntity.ok(servicesAgenceImmobiliere);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du service de l'agence immobilière.");
        }
    }

    @RequestMapping(value = "/demande/ajout-nouveau-service", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> demandeAjoutNouveauService(Principal principal, @RequestBody ServiceNonTrouveForm serviceNonTrouveForm) {
        try {
            servicesAgenceImmobiliereService.demandeAjoutServiceNonTrouve(principal, serviceNonTrouveForm);
            Map<String, Object> response = new HashMap<>();
            String message = "Demande envoyée.";
            response.put("message", message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>("Demande non envoyée", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/activer/service/agence-immobiliere/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerServiceAgence(@PathVariable Long id){
        this.servicesAgenceImmobiliereService.activerServiceAgence(id);
    }

    @RequestMapping(value = "/desactiver/service/agence-immobiliere/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverServiceAgence(@PathVariable Long id){
        this.servicesAgenceImmobiliereService.desactiverServiceAgence(id);
    }
}
