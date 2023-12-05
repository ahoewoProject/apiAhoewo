package com.memoire.apiAhoewo.controller.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ServicesAgenceImmobilierController {
    @Autowired
    private ServicesAgenceImmobiliereService servicesAgenceImmobiliereService;

    @RequestMapping(value = "/services/agence-immobiliere", method = RequestMethod.GET)
    public List<ServicesAgenceImmobiliere> getServicesOfAgence(Principal principal) {

        List<ServicesAgenceImmobiliere> servicesAgenceImmobilieres = new ArrayList<>();
        try {
            servicesAgenceImmobilieres = this.servicesAgenceImmobiliereService.getServicesOfAgence(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return servicesAgenceImmobilieres;
    }

    @RequestMapping(value = "/services/agence-immobiliere/{id}", method = RequestMethod.GET)
    public List<ServicesAgenceImmobiliere> getServicesOfAgence(@PathVariable Long id) {

        List<ServicesAgenceImmobiliere> servicesAgenceImmobilieres = new ArrayList<>();
        try {
            servicesAgenceImmobilieres = this.servicesAgenceImmobiliereService.getServicesOfAgence(id);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return servicesAgenceImmobilieres;
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

    @RequestMapping(value = "/activer/service/agence-immobiliere/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerServiceAgence(@PathVariable Long id){
        this.servicesAgenceImmobiliereService.activerServiceAgence(id);
    }

    @RequestMapping(value = "/desactiver/service/agence-immobiliere/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverServiceAgence(@PathVariable Long id){
        this.servicesAgenceImmobiliereService.desactiverServiceAgence(id);
    }
}
