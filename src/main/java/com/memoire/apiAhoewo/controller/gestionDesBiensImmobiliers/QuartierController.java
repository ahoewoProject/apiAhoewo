package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Quartier;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.QuartierService;
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
public class QuartierController {
    @Autowired
    private QuartierService quartierService;

    @RequestMapping(value = "/quartiers", method = RequestMethod.GET)
    public List<Quartier> getAll() {

        List<Quartier> quartierList = new ArrayList<>();
        try {
            quartierList = this.quartierService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return quartierList;
    }

    @RequestMapping(value = "/quartiers/pagines", method = RequestMethod.GET)
    public Page<Quartier> getQuartiersPagines(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.quartierService.getQuartiersPagines(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des quartiers.", e);
        }
    }

    @RequestMapping(value = "/quartiers/actifs", method = RequestMethod.GET)
    public List<Quartier> getQuartiersActifs() {

        List<Quartier> quartiers = new ArrayList<>();
        try {
            quartiers = this.quartierService.getQuartiersActifs(true);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return quartiers;
    }

    @RequestMapping(value = "/quartiers/ville/{id}", method = RequestMethod.GET)
    public List<Quartier> getQuartiersByVilleId(@PathVariable Long id) {

        List<Quartier> quartiers = new ArrayList<>();
        try {
            quartiers = this.quartierService.quartiersByVilleId(id);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return quartiers;
    }

    @RequestMapping(value = "/quartier/{id}", method = RequestMethod.GET)
    public Quartier findById(@PathVariable Long id) {

        Quartier quartier = new Quartier();
        try {
            quartier = this.quartierService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return quartier;
    }

    @RequestMapping(value = "/quartier/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterQuartier(Principal principal, @RequestBody Quartier quartier) {
        try {
            if (quartierService.libelleAndVilleExists(quartier.getLibelle(), quartier.getVille())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un quartier avec ce libelle " + quartier.getLibelle() + " existe déjà dans cette ville.");
            }

            quartier = this.quartierService.save(quartier, principal);
            return ResponseEntity.ok(quartier);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du quartier : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/quartier/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierQuartier(Principal principal, @RequestBody Quartier quartierModifie, @PathVariable  Long id) {
        Quartier quartier = quartierService.findById(id);
        try {
            if (quartierService.libelleAndVilleExists(quartierModifie.getLibelle(), quartierModifie.getVille())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un quartier avec ce libelle " + quartierModifie.getLibelle() + " existe déjà dans cette ville.");
            }
            quartier.setLibelle(quartierModifie.getLibelle());
            quartier = this.quartierService.update(quartier, principal);
            return ResponseEntity.ok(quartier);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du quartier : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/quartier/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerQuartier(@PathVariable Long id){
        this.quartierService.activerQuartier(id);
    }

    @RequestMapping(value = "/desactiver/quartier/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverQuartier(@PathVariable Long id){
        this.quartierService.desactiverQuartier(id);
    }

}
