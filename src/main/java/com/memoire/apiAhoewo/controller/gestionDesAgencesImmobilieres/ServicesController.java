package com.memoire.apiAhoewo.controller.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.ServicesService;
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
public class ServicesController {
    @Autowired
    private ServicesService servicesService;

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public List<Services> getAll() {

        List<Services> servicesList = new ArrayList<>();
        try {
            servicesList = this.servicesService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return servicesList;
    }

    @RequestMapping(value = "/services/pagines", method = RequestMethod.GET)
    public Page<Services> getServicesPagines(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.servicesService.getServices(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des services.", e);
        }
    }

    @RequestMapping(value = "/services/actifs", method = RequestMethod.GET)
    public List<Services> getServicesActifs() {

        List<Services> servicesList = new ArrayList<>();
        try {
            servicesList = this.servicesService.servicesActifs();
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
    public ResponseEntity<?> ajouterServices(Principal principal, @RequestBody Services services) {
        try {
            Services servicesExistant = servicesService.findByNomService(services.getNomService());

            if (servicesExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un service avec ce nom " + services.getNomService() + " existe déjà.");
            }

            services = this.servicesService.save(services, principal);
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du service : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/services/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierServices(Principal principal, @RequestBody Services servicesModifie, @PathVariable  Long id) {
        Services services = servicesService.findById(id);
        Services servicesExistant = servicesService.findByNomService(servicesModifie.getNomService());
        try {
            if (servicesExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un type de bien avec cette désignation " + servicesExistant.getNomService() + " existe déjà.");
            }
            services.setNomService(servicesModifie.getNomService());
            services = this.servicesService.update(services, principal);
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du service : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/services/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerServices(@PathVariable Long id){
        this.servicesService.activerServices(id);
    }

    @RequestMapping(value = "/desactiver/services/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverServices(@PathVariable Long id){
        this.servicesService.desactiverServices(id);
    }
}
