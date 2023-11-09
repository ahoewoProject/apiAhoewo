package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.TypeDeBien;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.TypeDeBienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TypeDeBienController {

    @Autowired
    private TypeDeBienService typeDeBienService;

    @RequestMapping(value = "/types-de-bien", method = RequestMethod.GET)
    public List<TypeDeBien> getAll() {

        List<TypeDeBien> typeDeBiens = new ArrayList<>();
        try {
            typeDeBiens = this.typeDeBienService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return typeDeBiens;
    }

    @RequestMapping(value = "/type-de-bien/{id}", method = RequestMethod.GET)
    public TypeDeBien findById(@PathVariable Long id) {

        TypeDeBien typeDeBien = new TypeDeBien();
        try {
            typeDeBien = this.typeDeBienService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return typeDeBien;
    }

    @RequestMapping(value = "/type-de-bien/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterTypeDeBien(Principal principal, @RequestBody TypeDeBien typeDeBien) {
        try {
            TypeDeBien typeDeBienExistant = typeDeBienService.findByDesignation(typeDeBien.getDesignation());

            if (typeDeBienExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un type de bien avec cette désignation " + typeDeBien.getDesignation() + " existe déjà.");
            }

            typeDeBien = this.typeDeBienService.save(typeDeBien, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du type de bien : " + e.getMessage());
        }
        return ResponseEntity.ok(typeDeBien);
    }

    @RequestMapping(value = "/type-de-bien/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierTypeDeBien(Principal principal, @RequestBody TypeDeBien typeDeBienModifie, @PathVariable  Long id) {
        TypeDeBien typeDeBien = typeDeBienService.findById(id);
        TypeDeBien typeDeBienExistant = typeDeBienService.findByDesignation(typeDeBienModifie.getDesignation());
        try {
            if (typeDeBienExistant != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un type de bien avec cette désignation " + typeDeBienModifie.getDesignation() + " existe déjà.");
            }
            typeDeBien.setDesignation(typeDeBienModifie.getDesignation());
            typeDeBien = this.typeDeBienService.update(typeDeBien, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return ResponseEntity.ok(typeDeBien);
    }

    @RequestMapping(value = "/type-de-bien/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerTypeDeBien(@PathVariable Long id) {
        this.typeDeBienService.deleteById(id);
    }
}
