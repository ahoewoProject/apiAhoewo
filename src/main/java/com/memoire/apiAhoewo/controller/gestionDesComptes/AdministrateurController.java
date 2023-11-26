package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Administrateur;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.service.gestionDesComptes.AdministrateurService;
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
public class AdministrateurController {

    @Autowired
    private AdministrateurService administrateurService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/administrateurs", method = RequestMethod.GET)
    public List<Administrateur> getAll() {

        List<Administrateur> administrateurList = new ArrayList<>();
        try {
            administrateurList = this.administrateurService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return administrateurList;
    }

    @RequestMapping(value = "/administrateur/{id}", method = RequestMethod.GET)
    public Administrateur findById(@PathVariable Long id) {

        Administrateur administrateur = new Administrateur();
        try {
            administrateur = this.administrateurService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return administrateur;
    }

    @RequestMapping(value = "/administrateur/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterAdministrateur(Principal principal, @RequestBody Administrateur administrateur) {
        try {
            if (personneService.emailExists(administrateur.getEmail())) {
                new ResponseEntity<>("Un administrateur avec cette adresse e-mail existe déjà", HttpStatus.CONFLICT);
            }
            administrateur = this.administrateurService.save(administrateur, principal);
            return ResponseEntity.ok(administrateur);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de l'administrateur : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/administrateur/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerAdministrateur(@PathVariable Long id) {
        this.administrateurService.deleteById(id);
    }

    @RequestMapping(value = "/count/administrateurs", method = RequestMethod.GET)
    public int nombreDeAdministrateurs(){
        int nombres = this.administrateurService.countAdministrateurs();
        return nombres;
    }
}
