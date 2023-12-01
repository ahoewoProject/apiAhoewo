package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.exception.UnsupportedFileTypeException;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.*;
import com.memoire.apiAhoewo.service.FileManagerService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BienImmobilierController {
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private ImagesBienImmobilierService imagesBienImmobilierService;
    @Autowired
    private ConfortService confortService;
    @Autowired
    private DivertissementService divertissementService;
    @Autowired
    private UtilitaireService utilitaireService;

    @RequestMapping(value = "/biens-immobiliers", method = RequestMethod.GET)
    public List<BienImmobilier> getAll() {

        List<BienImmobilier> bienImmobiliers = new ArrayList<>();
        try {
            bienImmobiliers = this.bienImmobilierService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobiliers;
    }

    @RequestMapping(value = "/biens-immobiliers/proprietaire", method = RequestMethod.GET)
    public List<BienImmobilier> getAllByProprietaire(Principal principal) {

        List<BienImmobilier> bienImmobiliers = new ArrayList<>();
        try {
            bienImmobiliers = this.bienImmobilierService.getAllByProprietaire(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobiliers;
    }

    @RequestMapping(value = "/biens-immobiliers/agences/responsable", method = RequestMethod.GET)
    public List<BienImmobilier> getBiensOfAgencesByResponsable(Principal principal) {

        List<BienImmobilier> bienImmobiliers = new ArrayList<>();
        try {
            bienImmobiliers = this.bienImmobilierService.getBiensOfAgencesByResponsable(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobiliers;
    }

    @RequestMapping(value = "/biens-immobiliers/agences/agent", method = RequestMethod.GET)
    public List<BienImmobilier> getBiensOfAgencesByAgent(Principal principal) {

        List<BienImmobilier> bienImmobiliers = new ArrayList<>();
        try {
            bienImmobiliers = this.bienImmobilierService.getBiensOfAgencesByAgent(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmobiliers;
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
               @RequestParam(value = "confortJson", required = false) String confortJson,
               @RequestParam(value = "utilitaireJson", required = false) String utilitaireJson,
               @RequestParam(value = "divertissementJson", required = false) String divertissementJson) {

        try {

            BienImmobilier bienImmobilier = new BienImmobilier();
            Confort confort = new Confort();
            Utilitaire utilitaire = new Utilitaire();
            Divertissement divertissement = new Divertissement();

            if (bienImmobilierJson != null && !bienImmobilierJson.isEmpty()) {
                bienImmobilier = new ObjectMapper().readValue(bienImmobilierJson, BienImmobilier.class);
            }

            if (confortJson != null && !confortJson.isEmpty()) {
                confort = new ObjectMapper().readValue(confortJson, Confort.class);
            }

            if (utilitaireJson != null && !utilitaireJson.isEmpty()) {
                utilitaire = new ObjectMapper().readValue(utilitaireJson, Utilitaire.class);
            }

            if (divertissementJson != null && !divertissementJson.isEmpty()) {
                divertissement = new ObjectMapper().readValue(divertissementJson, Divertissement.class);
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

            if (bienImmobilier.getTypeDeBien().getDesignation() != "Terrains") {
                if (confort != null) {
                    confortService.save(bienImmobilier, confort, principal);
                }
                if (utilitaire != null) {
                    utilitaireService.save(bienImmobilier, utilitaire, principal);
                }
                if (divertissement != null) {
                    divertissementService.save(bienImmobilier, divertissement, principal);
                }
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
                                    @RequestParam(value = "confortJson", required = false) String confortJson,
                                    @RequestParam(value = "utilitaireJson", required = false) String utilitaireJson,
                                    @RequestParam(value = "divertissementJson", required = false) String divertissementJson) {

        try {
            BienImmobilier bienImmobilier = bienImmobilierService.findById(id);

            BienImmobilier bienImmobilierUpdate = new ObjectMapper().readValue(bienImmobilierJson, BienImmobilier.class);
            bienImmobilier.setDescription(bienImmobilierUpdate.getDescription());
            bienImmobilier.setAdresse(bienImmobilierUpdate.getAdresse());
            bienImmobilier.setVille(bienImmobilierUpdate.getVille());
            bienImmobilier.setSurface(bienImmobilierUpdate.getSurface());

            // Enregistrer la mise à jour du bien immobilier dans la base de données
            bienImmobilier = bienImmobilierService.update(bienImmobilier, principal);

            // Gérer Confort, Utilitaire et Divertissement si ce n'est pas un terrain
            if (!bienImmobilier.getTypeDeBien().getDesignation().equals("Terrains")) {
                Confort confort = confortService.getByBienImmobilier(id);
                Utilitaire utilitaire = utilitaireService.getByBienImmobilier(id);
                Divertissement divertissement = divertissementService.getByBienImmobilier(id);

                if (confort != null && confortJson != null && !confortJson.isEmpty()) {
                    Confort confortUpdate = new ObjectMapper().readValue(confortJson, Confort.class);
                    confortUpdate.setBienImmobilier(confort.getBienImmobilier());
                    confortService.update(bienImmobilier, confortUpdate, principal);
                }

                if (utilitaire != null && utilitaireJson != null && !utilitaireJson.isEmpty()) {
                    Utilitaire utilitaireUpdate = new ObjectMapper().readValue(utilitaireJson, Utilitaire.class);
                    utilitaireUpdate.setBienImmobilier(utilitaire.getBienImmobilier());
                    utilitaireService.update(bienImmobilier, utilitaireUpdate, principal);
                }

                if (divertissement != null && divertissementJson != null && !divertissementJson.isEmpty()) {
                    Divertissement divertissementUpdate = new ObjectMapper().readValue(divertissementJson, Divertissement.class);
                    divertissementUpdate.setBienImmobilier(divertissement.getBienImmobilier());
                    divertissementService.update(bienImmobilier, divertissementUpdate, principal);
                }
            }

            // Enregistrer de nouvelles images si elles sont fournies
            if (files != null) {
                for (MultipartFile image : files) {
                    ImagesBienImmobilier imagesBienImmobilier = new ImagesBienImmobilier();
                    String nomImageDuBien = imagesBienImmobilierService.enregistrerImageDuBien(image);
                    imagesBienImmobilier.setNomImage(nomImageDuBien);
                    imagesBienImmobilier.setBienImmobilier(bienImmobilier);
                    imagesBienImmobilierService.save(imagesBienImmobilier, principal);
                }
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
