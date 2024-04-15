package com.memoire.apiAhoewo.controllers.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Ville;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.VilleService;
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

    @RequestMapping(value = "/villes/paginees", method = RequestMethod.GET)
    public Page<Ville> getVillesPaginees(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.villeService.getVillesPaginees(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des villes.", e);
        }
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

    @RequestMapping(value = "/villes/region/{id}", method = RequestMethod.GET)
    public List<Ville> getVillesByRegionId(@PathVariable Long id) {

        List<Ville> villes = new ArrayList<>();
        try {
            villes = this.villeService.villesByRegionId(id);
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
            if (villeService.libelleAndRegionExists(ville.getLibelle(), ville.getRegion())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une ville avec ce libelle " + ville.getLibelle() + " existe déjà dans cette region.");
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
        try {
            if (villeService.libelleAndRegionExists(villeModifie.getLibelle(), villeModifie.getRegion())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une ville avec ce libelle " + villeModifie.getLibelle() + " existe déjà dans cette région.");
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
