package com.memoire.apiAhoewo.controllers.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.TypeDeBien;
import com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers.BienImmobilierRepository;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.TypeDeBienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TypeDeBienController {
    @Autowired
    private TypeDeBienService typeDeBienService;
    @Autowired
    private BienImmobilierRepository bienImmobilierRepository;

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

    @RequestMapping(value = "/types-de-bien/pagines", method = RequestMethod.GET)
    public Page<TypeDeBien> getTypesDeBienPagines(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.typeDeBienService.getTypesDeBienPagines(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des types de biens.", e);
        }
    }

    @RequestMapping(value = "/types-de-bien/actifs", method = RequestMethod.GET)
    public List<TypeDeBien> getTypeDeBienActifs() {

        List<TypeDeBien> typeDeBiens = new ArrayList<>();
        try {
            typeDeBiens = this.typeDeBienService.findTypeDeBienActifs();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return typeDeBiens;
    }

    @RequestMapping(value = "/types-de-bien/to-start", method = RequestMethod.GET)
    public List<TypeDeBien> getTypeDeBienToStart() {

        List<TypeDeBien> typeDeBiens = new ArrayList<>();

        List<String> designations = new ArrayList<>();
        designations.add("Terrain");
        designations.add("Maison");
        designations.add("Immeuble");
        designations.add("Villa");
        try {
            typeDeBiens = this.typeDeBienService.findTypeDeBienActifsByLibelle(designations);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return typeDeBiens;
    }

    @RequestMapping(value = "/types-de-bien/pour-maison", method = RequestMethod.GET)
    public List<TypeDeBien> getTypeDeBienPourMaison() {

        List<TypeDeBien> typeDeBiens = new ArrayList<>();

        List<String> designations = new ArrayList<>();
        designations.add("Appartement");
        designations.add("Magasin");
        designations.add("Bureau");
        designations.add("Boutique");
        designations.add("Chambre");
        designations.add("Chambre salon");
        try {
            typeDeBiens = this.typeDeBienService.findTypeDeBienActifsByLibelle(designations);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return typeDeBiens;
    }

    @RequestMapping(value = "/types-de-bien/pour-immeuble", method = RequestMethod.GET)
    public List<TypeDeBien> getTypeDeBienPourImmeuble() {

        List<TypeDeBien> typeDeBiens = new ArrayList<>();

        List<String> designations = new ArrayList<>();
        designations.add("Appartement");
        designations.add("Magasin");
        designations.add("Bureau");
        designations.add("Boutique");
        designations.add("Chambre");
        designations.add("Chambre salon");
        try {
            typeDeBiens = this.typeDeBienService.findTypeDeBienActifsByLibelle(designations);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return typeDeBiens;
    }

    @RequestMapping(value = "/types-de-bien/pour-villa", method = RequestMethod.GET)
    public List<TypeDeBien> getTypeDeBienPourVilla() {

        List<TypeDeBien> typeDeBiens = new ArrayList<>();

        List<String> designations = new ArrayList<>();
        designations.add("Chambre");
        designations.add("Chambre salon");
        try {
            typeDeBiens = this.typeDeBienService.findTypeDeBienActifsByLibelle(designations);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return typeDeBiens;
    }

    @RequestMapping(value = "/types-de-bien/location", method = RequestMethod.GET)
    public List<TypeDeBien> getTypeDeBienByLocation() {

        List<TypeDeBien> typeDeBiens = new ArrayList<>();

        List<String> designations = new ArrayList<>();
        designations.add("Maison");
        designations.add("Immeuble");
        designations.add("Villa");
        designations.add("Chambre");
        designations.add("Chambre salon");
        designations.add("Appartement");
        designations.add("Bureau");
        designations.add("Boutique");
        designations.add("Magasin");
        try {
            typeDeBiens = this.typeDeBienService.findTypeDeBienActifsByLibelle(designations);
        } catch (Exception e) {
        // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return typeDeBiens;
    }

    @RequestMapping(value = "/types-de-bien/vente", method = RequestMethod.GET)
    public List<TypeDeBien> getTypeDeBienByVente() {

        List<TypeDeBien> typeDeBiens = new ArrayList<>();

        List<String> designations = new ArrayList<>();
        designations.add("Terrain");
        designations.add("Maison");
        designations.add("Immeuble");
        designations.add("Villa");
        try {
            typeDeBiens = this.typeDeBienService.findTypeDeBienActifsByLibelle(designations);
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
            return ResponseEntity.ok(typeDeBien);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du type de bien : " + e.getMessage());
        }
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
            return ResponseEntity.ok(typeDeBien);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du type de bien : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/type-de-bien/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerTypeDeBien(@PathVariable Long id){
        this.typeDeBienService.activerTypeDeBien(id);
    }

    @RequestMapping(value = "/desactiver/type-de-bien/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverTypeDeBien(@PathVariable Long id){
        this.typeDeBienService.desactiverTypeDeBien(id);
    }

    @RequestMapping(value = "/type-de-bien/supprimer/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<?> supprimerTypeDeBien(@PathVariable Long id) {
        TypeDeBien typeDeBien = typeDeBienService.findById(id);
        List<BienImmobilier> biensAssocies = bienImmobilierRepository.findByTypeDeBien(typeDeBien);
        Map<String, Object> response = new HashMap<>();
        if (!biensAssocies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Des biens sont liés à ce type de bien. Suppression impossible.");
        } else {
            typeDeBienService.deleteById(typeDeBien.getId());
            response.put("message", "Type de bien supprimé avec succès");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
