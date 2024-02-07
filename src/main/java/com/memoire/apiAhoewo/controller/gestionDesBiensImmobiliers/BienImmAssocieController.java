package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmAssocie;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Caracteristiques;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.ImagesBienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.CaracteristiquesService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.ImagesBienImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.BienImmobilierAssocieService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BienImmAssocieController {
    @Autowired
    private BienImmobilierAssocieService bienImmobilierAssocieService;
    @Autowired
    private CaracteristiquesService caracteristiquesService;
    @Autowired
    private ImagesBienImmobilierService imagesBienImmobilierService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/biens-imm-associes/pagines/{id}", method = RequestMethod.GET)
    public Page<BienImmAssocie> getBiensAssociesPaginesByBienImmobilier(@PathVariable Long id, Principal principal,
                                                              @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                              @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.bienImmobilierAssocieService.getBiensAssociesPaginesByBienImmobilier(id, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des biens immobiliers associés.", e);
        }
    }

    @RequestMapping(value = "/bien-imm-associe/{id}", method = RequestMethod.GET)
    public BienImmAssocie findById(@PathVariable Long id) {

        BienImmAssocie bienImmAssocie = new BienImmAssocie();
        try {
            bienImmAssocie = this.bienImmobilierAssocieService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return bienImmAssocie;
    }

    @RequestMapping(value = "/bien-imm-associe/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterBienImmAssocie(Principal principal,
                                          @RequestParam(value = "images", required = false) List<MultipartFile> files,
                                          String bienImmAssocieJson,
                                          @RequestParam(value = "caracteristiquesJson", required = false) String caracteristiquesJson)
    {

        try {
            Personne personne = personneService.findByUsername(principal.getName());
            BienImmAssocie bienImmAssocie = new BienImmAssocie();
            Caracteristiques caracteristique;

            if (bienImmAssocieJson != null && !bienImmAssocieJson.isEmpty()) {
                bienImmAssocie = new ObjectMapper().readValue(bienImmAssocieJson, BienImmAssocie.class);
            }
            bienImmAssocie.setPersonne(personne);
            bienImmAssocie = this.bienImmobilierAssocieService.save(bienImmAssocie, principal);

            if (files != null) {
                for(MultipartFile images: files){
                    ImagesBienImmobilier imagesBienImmobilier = new ImagesBienImmobilier();
                    String nomImageDuBien = imagesBienImmobilierService.enregistrerImageDuBien(images);
                    imagesBienImmobilier.setNomImage(nomImageDuBien);
                    imagesBienImmobilier.setBienImmobilier(bienImmAssocie);
                    imagesBienImmobilierService.save(imagesBienImmobilier, principal);
                }
            }

            if (caracteristiquesJson != null && !caracteristiquesJson.trim().isEmpty() && !caracteristiquesJson.equals("{}")) {
                caracteristique = new ObjectMapper().readValue(caracteristiquesJson, Caracteristiques.class);
                caracteristiquesService.save(bienImmAssocie, caracteristique, principal);
            }

            return ResponseEntity.ok(bienImmAssocie);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du bien associé : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/bien-imm-associe/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierBienImmobilier(@PathVariable Long id,
                                                 Principal principal,
                                                 @RequestParam(value = "images", required = false) List<MultipartFile> files,
                                                 String bienImmAssocieJson,
                                                 @RequestParam(value = "caracteristiquesJson", required = false) String caracteristiquesJson)
    {
        try {
            BienImmAssocie bienImmAssocie = bienImmobilierAssocieService.findById(id);

            if (bienImmAssocieJson != null && !bienImmAssocieJson.isEmpty()) {
                BienImmAssocie bienImmAssocieUpdate = new ObjectMapper().readValue(bienImmAssocieJson, BienImmAssocie.class);
                bienImmAssocie.setDescription(bienImmAssocieUpdate.getDescription());
                bienImmAssocie.setCategorie(bienImmAssocieUpdate.getCategorie());
                bienImmAssocie.setAdresse(bienImmAssocieUpdate.getAdresse());
                bienImmAssocie.setPays(bienImmAssocieUpdate.getPays());
                bienImmAssocie.setRegion(bienImmAssocieUpdate.getRegion());
                bienImmAssocie.setVille(bienImmAssocieUpdate.getVille());
                bienImmAssocie.setQuartier(bienImmAssocieUpdate.getQuartier());
                bienImmAssocie.setSurface(bienImmAssocieUpdate.getSurface());
                bienImmAssocie.setBienImmobilier(bienImmAssocieUpdate.getBienImmobilier());
            }

            bienImmAssocie = bienImmobilierAssocieService.update(bienImmAssocie, principal);

            if (files != null) {
                for (MultipartFile image : files) {
                    ImagesBienImmobilier imagesBienImmobilier = new ImagesBienImmobilier();
                    String nomImageDuBien = imagesBienImmobilierService.enregistrerImageDuBien(image);
                    imagesBienImmobilier.setNomImage(nomImageDuBien);
                    imagesBienImmobilier.setBienImmobilier(bienImmAssocie);
                    imagesBienImmobilierService.update(imagesBienImmobilier, principal);
                }
            }

            if (caracteristiquesJson != null && !caracteristiquesJson.trim().isEmpty() && !caracteristiquesJson.equals("{}")) {
                Caracteristiques caracteristiqueUpdate = new ObjectMapper().readValue(caracteristiquesJson, Caracteristiques.class);
                caracteristiquesService.update(id, caracteristiqueUpdate, principal);
            }

            return ResponseEntity.ok(bienImmAssocie);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du bien associé : " + e.getMessage());
        }
    }
}
