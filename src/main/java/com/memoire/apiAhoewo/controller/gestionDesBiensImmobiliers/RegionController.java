package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Region;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.RegionService;
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
public class RegionController {
    @Autowired
    private RegionService regionService;

    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    public List<Region> getAll() {

        List<Region> regionList = new ArrayList<>();
        try {
            regionList = this.regionService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return regionList;
    }

    @RequestMapping(value = "/regions/actifs", method = RequestMethod.GET)
    public List<Region> getRegionsActifs() {

        List<Region> regionList = new ArrayList<>();
        try {
            regionList = this.regionService.getRegionsActifs(true);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return regionList;
    }

    @RequestMapping(value = "/region/{id}", method = RequestMethod.GET)
    public Region findById(@PathVariable Long id) {

        Region region = new Region();
        try {
            region = this.regionService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return region;
    }

    @RequestMapping(value = "/region/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterRegion(Principal principal, @RequestBody Region region) {
        try {
            Region typeDeBienExistant = regionService.findByLibelle(region.getLibelle());

            if (typeDeBienExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une région avec ce libelle " + region.getLibelle() + " existe déjà.");
            }

            region = this.regionService.save(region, principal);
            return ResponseEntity.ok(region);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de la région : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/region/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierRegion(Principal principal, @RequestBody Region regionModifie, @PathVariable  Long id) {
        Region region = regionService.findById(id);
        Region regionExistant = regionService.findByLibelle(regionModifie.getLibelle());
        try {
            if (regionExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une région avec ce libelle " + regionModifie.getLibelle() + " existe déjà.");
            }
            region.setLibelle(regionModifie.getLibelle());
            region = this.regionService.update(region, principal);
            return ResponseEntity.ok(region);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification de la région : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/region/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerRegion(@PathVariable Long id){
        this.regionService.activerRegion(id);
    }

    @RequestMapping(value = "/desactiver/region/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverRegion(@PathVariable Long id){
        this.regionService.desactiverRegion(id);
    }

}
