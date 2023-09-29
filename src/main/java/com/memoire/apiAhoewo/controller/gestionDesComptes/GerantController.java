package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
import com.memoire.apiAhoewo.service.gestionDesComptes.GerantService;
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
public class GerantController {
    @Autowired
    private GerantService gerantService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/gerants", method = RequestMethod.GET)
    public List<Gerant> getAll() {

        List<Gerant> gerantList = new ArrayList<>();
        try {
            gerantList = this.gerantService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return gerantList;
    }

    @RequestMapping(value = "/gerants-proprietaire", method = RequestMethod.GET)
    public List<Gerant> findGerantsByProprietaire(Principal principal) {

        List<Gerant> gerantList = new ArrayList<>();
        try {
            gerantList = this.gerantService.findGerantsByProprietaire(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return gerantList;
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
            if (personneService.usernameExists(gerant.getUsername())) {
                return new ResponseEntity<>("Un utilisateur avec ce d'utilisateur existe déjà", HttpStatus.CONFLICT);
            }

            gerant = this.gerantService.save(gerant, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du gérant : " + e.getMessage());
        }
        return ResponseEntity.ok(gerant);
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
