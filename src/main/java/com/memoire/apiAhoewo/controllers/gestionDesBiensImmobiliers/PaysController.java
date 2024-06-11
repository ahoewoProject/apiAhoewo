package com.memoire.apiAhoewo.controllers.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Pays;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.PaysService;
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
public class PaysController {
    @Autowired
    private PaysService paysService;

    @RequestMapping(value = "/pays", method = RequestMethod.GET)
    public List<Pays> getAll() {

        List<Pays> pays = new ArrayList<>();
        try {
            pays = this.paysService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return pays;
    }

    @RequestMapping(value = "/pays/pagines", method = RequestMethod.GET)
    public Page<Pays> getPaysPaginees(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.paysService.getPaysPagines(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des pays.", e);
        }
    }

    @RequestMapping(value = "/pays/actifs", method = RequestMethod.GET)
    public List<Pays> getPaysActifs() {

        List<Pays> paysList = new ArrayList<>();
        try {
            paysList = this.paysService.getPaysActifs(true);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return paysList;
    }

    @RequestMapping(value = "/pays/{id}", method = RequestMethod.GET)
    public Pays findById(@PathVariable Long id) {

        Pays pays = new Pays();
        try {
            pays = this.paysService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return pays;
    }

    @RequestMapping(value = "/pays/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterPays(Principal principal, @RequestBody Pays pays) {
        try {
            Pays paysExistant = paysService.findByLibelle(pays.getLibelle());

            if (paysExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un pays avec ce libelle " + pays.getLibelle() + " existe déjà.");
            }

            pays = this.paysService.save(pays, principal);
            return ResponseEntity.ok(pays);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du pays : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/pays/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierPays(Principal principal, @RequestBody Pays paysModifie, @PathVariable  Long id) {
        Pays pays = paysService.findById(id);
        Pays paysExistant = paysService.findByLibelle(paysModifie.getLibelle());
        try {
            if (paysExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un pays avec ce libelle " + paysModifie.getLibelle() + " existe déjà.");
            }
            pays.setLibelle(paysModifie.getLibelle());
            pays = this.paysService.update(pays, principal);
            return ResponseEntity.ok(pays);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du pays : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/pays/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerPays(@PathVariable Long id){
        this.paysService.activerPays(id);
    }

    @RequestMapping(value = "/desactiver/pays/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverPays(@PathVariable Long id){
        this.paysService.desactiverPays(id);
    }
}
