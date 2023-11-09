package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.exception.UnsupportedFileTypeException;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.ImagesBienImmobilier;
import com.memoire.apiAhoewo.service.FileManagerService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.ImagesBienImmobilierService;
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
    private FileManagerService fileManagerService;

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

    @RequestMapping(value = "/biens-immobiliers/gerant", method = RequestMethod.GET)
    public List<BienImmobilier> getAllByGerant(Principal principal) {

        List<BienImmobilier> bienImmobiliers = new ArrayList<>();
        try {
            bienImmobiliers = this.bienImmobilierService.getAllByGerant(principal);
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
                                                   @RequestParam(value = "imagePrincipale", required = false) MultipartFile file,
                                                   @RequestParam(value = "imagesBienImmobilier", required = false) List<MultipartFile> files,
                                                   String bienImmobilierJson) throws JsonProcessingException {

        BienImmobilier bienImmobilier = new ObjectMapper()
                .readValue(bienImmobilierJson, BienImmobilier.class);

        try {

            if (file != null){
                String nomImagePrincipaleDuBien = bienImmobilierService.enregistrerImagePrincipaleDuBien(file);
                bienImmobilier.setImagePrincipale(nomImagePrincipaleDuBien);
            }

            bienImmobilier = this.bienImmobilierService.save(bienImmobilier, principal);

            if (files != null){
                for(MultipartFile imagesBienImmobilier: files){
                    ImagesBienImmobilier imagesBienImmobilierEnregistre = new ImagesBienImmobilier();
                    String nomImageDuBien = imagesBienImmobilierService.enregistrerImageDuBien(imagesBienImmobilier);
                    imagesBienImmobilierEnregistre.setNomImage(nomImageDuBien);
                    imagesBienImmobilierEnregistre.setBienImmobilier(bienImmobilier);
                    imagesBienImmobilierService.save(imagesBienImmobilierEnregistre, principal);
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du bien immobilier : " + e.getMessage());
        }
        return ResponseEntity.ok(bienImmobilier);
    }

    @RequestMapping(value = "/bien-immobilier/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierBienImmobilier(@PathVariable Long id, Principal principal,
                                                   @RequestParam(value = "imagePrincipale", required = false) MultipartFile file,
                                                   @RequestParam(value = "imagesBienImmobilier", required = false) List<MultipartFile> files,
                                                   String bienImmobilierJson) throws JsonProcessingException {

        BienImmobilier bienImmobilierModifie = new ObjectMapper()
                .readValue(bienImmobilierJson, BienImmobilier.class);

        BienImmobilier bienImmobilier = bienImmobilierService.findById(id);
        try {

            if (file != null){
                String nomImagePrincipaleDuBien = bienImmobilierService.enregistrerImagePrincipaleDuBien(file);
                bienImmobilier.setImagePrincipale(nomImagePrincipaleDuBien);
            }
            bienImmobilier.setDescription(bienImmobilierModifie.getDescription());
            bienImmobilier.setAdresse(bienImmobilierModifie.getAdresse());
            bienImmobilier.setVille(bienImmobilierModifie.getVille());
            bienImmobilier.setSurface(bienImmobilierModifie.getSurface());

            bienImmobilier = this.bienImmobilierService.update(bienImmobilier, principal);

            if (files != null){
                for(MultipartFile imagesBienImmobilier: files){
                    ImagesBienImmobilier imagesBienImmobilierModifie = new ImagesBienImmobilier();
                    String nomImageDuBien = imagesBienImmobilierService.enregistrerImageDuBien(imagesBienImmobilier);
                    imagesBienImmobilierModifie.setNomImage(nomImageDuBien);
                    imagesBienImmobilierModifie.setBienImmobilier(bienImmobilier);
                    imagesBienImmobilierService.save(imagesBienImmobilierModifie, principal);
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du bien immobilier : " + e.getMessage());
        }
        return ResponseEntity.ok(bienImmobilier);
    }


    @RequestMapping(value = "/count/biens-immobiliers/proprietaire", method = RequestMethod.GET)
    public int nombreBienImmobilierParProprietaire(Principal principal){
        int nombres = this.bienImmobilierService.countBienImmobilierByProprietaire(principal);
        return nombres;
    }

    @RequestMapping(value = "/count/biens-immobiliers/gerant", method = RequestMethod.GET)
    public int nombreBienImmobilierParGerant(Principal principal){
        int nombres = this.bienImmobilierService.countBienImmobilierByGerant(principal);
        return nombres;
    }

    @RequestMapping(value = "/activer/bien-immobilier/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerBienImmobilier(@PathVariable Long id){
        this.bienImmobilierService.activerBienImmobilier(id);
    }

    @RequestMapping(value = "/desactiver/bien-immobilier/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverBienImmobilier(@PathVariable Long id){
        this.bienImmobilierService.desactiverBienImmobilier(id);
    }

    @RequestMapping(value = "/image-principale/bien-immobilier/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImagePrincipaleBienImmobilier(@PathVariable Long id) {
        BienImmobilier bienImmobilier = bienImmobilierService.findById(id);

        try {
            String cheminFichier = bienImmobilierService.construireCheminFichier(bienImmobilier);
            byte[] imageBytes = fileManagerService.lireFichier(cheminFichier);

            HttpHeaders headers = fileManagerService.construireHeaders(cheminFichier, imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedFileTypeException e) {
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }
}
