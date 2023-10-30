package com.memoire.apiAhoewo.controller.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    @RequestMapping(value = "/services/agence-immobiliere", method = RequestMethod.GET)
    public List<Services> getAllByAgenceImmobiliere(Principal principal) {

        List<Services> servicesList = new ArrayList<>();
        try {
            servicesList = this.servicesService.getAllByAgence(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return servicesList;
    }

    @RequestMapping(value = "/services/{id}", method = RequestMethod.GET)
    public Services findById(@PathVariable Long id) {

        Services services = new Services();
        try {
            services = this.servicesService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return services;
    }

    @RequestMapping(value = "/services/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterService(Principal principal, @RequestBody Services services) {
        try {
            List<Services> servicesExistants = this.servicesService.getAllByAgence(principal);

            for (Services serviceExistant : servicesExistants) {
                if (serviceExistant.getNomService().equals(services.getNomService())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Un service avec ce nom " + services.getNomService() + " existe déjà.");
                }

            }
            services = servicesService.saveService(services, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du service : " + e.getMessage());
        }
        return ResponseEntity.ok(services);
    }

    @RequestMapping(value = "/services/modifier/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> modifierService(Principal principal, @RequestBody Services servicesModifie, @PathVariable Long id) {
        Services services;
        try {
            services = servicesService.findById(id);
            List<Services> servicesExistants = this.servicesService.getAllByAgence(principal);

            for (Services serviceExistant : servicesExistants) {
                if (serviceExistant.getNomService().equals(servicesModifie.getNomService())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Un service avec ce nom " + services.getNomService() + " existe déjà.");
                }
            }
            services.setNomService(servicesModifie.getNomService());
            services.setDescription(servicesModifie.getDescription());
            services = servicesService.updateService(services, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du service : " + e.getMessage());
        }
        return ResponseEntity.ok(services);
    }

    @RequestMapping(value = "/services/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerServices(@PathVariable Long id) {
        this.servicesService.deleteById(id);
    }

    @RequestMapping(value = "/count/services", method = RequestMethod.GET)
    public int nombreServices(Principal principal){
        int nombres = this.servicesService.countServicesByAgence(principal);
        return nombres;
    }
}
