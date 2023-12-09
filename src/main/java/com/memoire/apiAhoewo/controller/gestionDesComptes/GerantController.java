package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
import com.memoire.apiAhoewo.model.gestionDesComptes.Role;
import com.memoire.apiAhoewo.service.gestionDesComptes.GerantService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
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
public class GerantController {
    @Autowired
    private GerantService gerantService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/gerants/pagines", method = RequestMethod.GET)
    public Page<Gerant> getGerants(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.gerantService.getGerants(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des gerants.", e);
        }
    }

    @RequestMapping(value = "/gerants/proprietaire/pagines", method = RequestMethod.GET)
    public Page<Gerant> getGerantsParProprietaire(Principal principal,
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.gerantService.findGerantsByProprietaire(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des gerants.", e);
        }
    }

    @RequestMapping(value = "/gerant/{id}", method = RequestMethod.GET)
    public Gerant findById(@PathVariable Long id) {

        Gerant gerant = new Gerant();
        try {
            gerant = this.gerantService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return gerant;
    }

    @RequestMapping(value = "/gerant/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterGerant(Principal principal, @RequestBody Gerant gerant) {
        try {
            if (personneService.emailExists(gerant.getEmail())) {
                new ResponseEntity<>("Un gérant avec cette adresse e-mail existe déjà", HttpStatus.CONFLICT);
            }
            gerant = this.gerantService.save(gerant, principal);
            return ResponseEntity.ok(gerant);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du gérant : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/gerant/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerGerant(@PathVariable Long id) {
        this.gerantService.deleteById(id);
    }

    @RequestMapping(value = "/count/gerants", method = RequestMethod.GET)
    public int nombreDeGerants(){
        int nombres = this.gerantService.countGerants();
        return nombres;
    }

    @RequestMapping(value = "/count/gerants-proprietaire", method = RequestMethod.GET)
    public int nombreDeGerantsByProprietaire(Principal principal){
        int nombres = this.gerantService.countGerantsByProprietaire(principal);
        return nombres;
    }
}
