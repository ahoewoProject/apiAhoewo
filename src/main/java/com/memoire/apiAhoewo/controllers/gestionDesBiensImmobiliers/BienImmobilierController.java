package com.memoire.apiAhoewo.controllers.gestionDesBiensImmobiliers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Caracteristiques;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.ImagesBienImmobilier;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.CaracteristiquesService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.ImagesBienImmobilierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BienImmobilierController {
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private CaracteristiquesService caracteristiquesService;
    @Autowired
    private ImagesBienImmobilierService imagesBienImmobilierService;

    @RequestMapping(value = "/biens-immobiliers", method = RequestMethod.GET)
    public List<BienImmobilier> getBiensImmobiliers(Principal principal) {

        List<BienImmobilier> bienImmobiliers = new ArrayList<>();
        try {
            bienImmobiliers = this.bienImmobilierService.getBiensImmobiliers(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobiliers;
    }

    @RequestMapping(value = "/biens-supports-pagines", method = RequestMethod.GET)
    public Page<BienImmobilier> getBiensSupportsPagines(Principal principal,
                                                              @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                              @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.bienImmobilierService.getBiensSupportsPagines(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des biens supports.", e);
        }
    }

    @RequestMapping(value = "/biens-immobiliers/proprietaire", method = RequestMethod.GET)
    public List<BienImmobilier> getBiensByProprietaire(Principal principal) {

        List<BienImmobilier> bienImmobiliers = new ArrayList<>();
        try {
            bienImmobiliers = this.bienImmobilierService.getBiensByProprietaire(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobiliers;
    }

    @RequestMapping(value = "/biens-supports", method = RequestMethod.GET)
    public List<BienImmobilier> getBiensSupports(Principal principal) {

        List<BienImmobilier> bienImmobiliers = new ArrayList<>();
        try {
            bienImmobiliers = this.bienImmobilierService.getBiensSupports(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobiliers;
    }

    @RequestMapping(value = "/biens-immobiliers/propres-delegues", method = RequestMethod.GET)
    public List<BienImmobilier> getBiensPropresAndBiensDelegues(Principal principal) {

        List<BienImmobilier> bienImmobiliers = new ArrayList<>();
        try {
            bienImmobiliers = this.bienImmobilierService.getBiensPropresAndBiensDelegues(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobiliers;
    }

    @RequestMapping(value = "/bien-immobilier/code-bien/{codeBien}", method = RequestMethod.GET)
    public BienImmobilier findByCodeBien(@PathVariable String codeBien) {

        BienImmobilier bienImmobilier = new BienImmobilier();
        try {
            bienImmobilier = this.bienImmobilierService.findByCodeBien(codeBien);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobilier;
    }

    @RequestMapping(value = "/bien-immobilier/{id}", method = RequestMethod.GET)
    public BienImmobilier findById(@PathVariable Long id) {

        BienImmobilier bienImmobilier = new BienImmobilier();
        try {
            bienImmobilier = this.bienImmobilierService.findById(id);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobilier;
    }

    @RequestMapping(value = "/bien-immobilier/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterBienImmobilier(Principal principal,
                                                   @RequestParam(value = "images", required = false) List<MultipartFile> files,
                                                   String bienImmobilierJson,
                                                   @RequestParam(value = "caracteristiquesJson", required = false) String caracteristiquesJson         ) {

        try {
            BienImmobilier bienImmobilier = new BienImmobilier();
            Caracteristiques caracteristique;

            if (bienImmobilierJson != null && !bienImmobilierJson.isEmpty()) {
                bienImmobilier = new ObjectMapper().readValue(bienImmobilierJson, BienImmobilier.class);
            }

            bienImmobilier = this.bienImmobilierService.save(bienImmobilier, principal);

            if (files != null) {
                for(MultipartFile images: files){
                    ImagesBienImmobilier imagesBienImmobilier = new ImagesBienImmobilier();
                    String nomImageDuBien = imagesBienImmobilierService.enregistrerImageDuBien(images);
                    imagesBienImmobilier.setNomImage(nomImageDuBien);
                    imagesBienImmobilier.setBienImmobilier(bienImmobilier);
                    imagesBienImmobilierService.save(imagesBienImmobilier, principal);
                }
            }

            if (caracteristiquesJson != null && !caracteristiquesJson.trim().isEmpty() && !caracteristiquesJson.equals("{}")) {
                caracteristique = new ObjectMapper().readValue(caracteristiquesJson, Caracteristiques.class);
                caracteristiquesService.save(bienImmobilier, caracteristique, principal);
            }

            return ResponseEntity.ok(bienImmobilier);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du bien immobilier : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/bien-immobilier/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierBienImmobilier(@PathVariable Long id,
                                                    Principal principal,
                                                    @RequestParam(value = "images", required = false) List<MultipartFile> files,
                                                    String bienImmobilierJson,
                                                    @RequestParam(value = "caracteristiquesJson", required = false) String caracteristiquesJson)
    {
        try {
            BienImmobilier bienImmobilier = bienImmobilierService.findById(id);

            BienImmobilier bienImmobilierUpdate = new ObjectMapper().readValue(bienImmobilierJson, BienImmobilier.class);
            bienImmobilier.setDescription(bienImmobilierUpdate.getDescription());
            if (bienImmobilierUpdate.getAgenceImmobiliere() != null) {
                bienImmobilier.setAgenceImmobiliere(bienImmobilierUpdate.getAgenceImmobiliere());
            } else {
                bienImmobilier.setPersonne(bienImmobilierUpdate.getPersonne());
            }
            bienImmobilier.setCategorie(bienImmobilierUpdate.getCategorie());
            bienImmobilier.setAdresse(bienImmobilierUpdate.getAdresse());
            bienImmobilier.setQuartier(bienImmobilierUpdate.getQuartier());
            bienImmobilier.setSurface(bienImmobilierUpdate.getSurface());

            bienImmobilier = bienImmobilierService.update(bienImmobilier, principal);

            if (files != null) {
                for (MultipartFile image : files) {
                    ImagesBienImmobilier imagesBienImmobilier = new ImagesBienImmobilier();
                    String nomImageDuBien = imagesBienImmobilierService.enregistrerImageDuBien(image);
                    imagesBienImmobilier.setNomImage(nomImageDuBien);
                    imagesBienImmobilier.setBienImmobilier(bienImmobilier);
                    imagesBienImmobilierService.save(imagesBienImmobilier, principal);
                }
            }

            if (caracteristiquesJson != null && !caracteristiquesJson.trim().isEmpty() && !caracteristiquesJson.equals("{}")) {
                Caracteristiques caracteristiqueUpdate = new ObjectMapper().readValue(caracteristiquesJson, Caracteristiques.class);
                caracteristiquesService.update(id, caracteristiqueUpdate, principal);
            }

            return ResponseEntity.ok(bienImmobilier);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du bien immobilier : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/bien-immobilier/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerBienImmobilier(@PathVariable Long id){
        this.bienImmobilierService.activerBienImmobilier(id);
    }

    @RequestMapping(value = "/desactiver/bien-immobilier/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverBienImmobilier(@PathVariable Long id){
        this.bienImmobilierService.desactiverBienImmobilier(id);
    }
}
