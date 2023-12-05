package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Ville;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class VilleController {
    @Autowired
    private VilleService villeService;

    @RequestMapping(value = "/villes", method = RequestMethod.GET)
    public List<Ville> getAll() {

        List<Ville> villes = new ArrayList<>();
        try {
            villes = this.villeService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return villes;
    }

    @RequestMapping(value = "/villes/actifs", method = RequestMethod.GET)
    public List<Ville> getVillesActifs() {

        List<Ville> villes = new ArrayList<>();
        try {
            villes = this.villeService.getRegionsActifs(true);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return villes;
    }

    @RequestMapping(value = "/ville/{id}", method = RequestMethod.GET)
    public Ville findById(@PathVariable Long id) {

        Ville ville = new Ville();
        try {
            ville = this.villeService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return ville;
    }

    @RequestMapping(value = "/ville/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterVille(Principal principal, @RequestBody Ville ville) {
        try {
            Ville villeExistant = villeService.findByLibelle(ville.getLibelle());

            if (villeExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une ville avec ce libelle " + ville.getLibelle() + " existe déjà.");
            }

            ville = this.villeService.save(ville, principal);
            return ResponseEntity.ok(ville);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de la ville : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/ville/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierVille(Principal principal, @RequestBody Ville villeModifie, @PathVariable  Long id) {
        Ville ville = villeService.findById(id);
        Ville villeExistant = villeService.findByLibelle(villeModifie.getLibelle());
        try {
            if (villeExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une ville avec ce libelle " + villeModifie.getLibelle() + " existe déjà.");
            }
            ville.setLibelle(villeModifie.getLibelle());
            ville = this.villeService.update(ville, principal);
            return ResponseEntity.ok(ville);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification de la ville : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/ville/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerTypeDeBien(@PathVariable Long id){
        this.villeService.activerVille(id);
    }

    @RequestMapping(value = "/desactiver/ville/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverTypeDeBien(@PathVariable Long id){
        this.villeService.desactiverVille(id);
    }

}
