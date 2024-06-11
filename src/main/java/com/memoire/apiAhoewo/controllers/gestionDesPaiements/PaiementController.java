package com.memoire.apiAhoewo.controllers.gestionDesPaiements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.exceptions.IOException;
import com.memoire.apiAhoewo.exceptions.UnsupportedFileTypeException;
import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.services.FileManagerService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PaiementController {
    @Autowired
    private PaiementService paiementService;
    @Autowired
    private FileManagerService fileManagerService;

    @RequestMapping(value = "/paiements", method = RequestMethod.GET)
    public Page<Paiement> getPaiements(Principal principal,
                                                   @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                   @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.paiementService.getPaiements(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des paiements .", e);
        }
    }

    @RequestMapping(value = "/paiements/par-code-contrat/{codeContrat}", method = RequestMethod.GET)
    public Page<Paiement> getPaiementsByCodeContrat(@PathVariable String codeContrat,
                                       @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                       @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.paiementService.getPaiementsByCodeContrat(codeContrat, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des paiements .", e);
        }
    }


    @RequestMapping(value = "/paiements-list", method = RequestMethod.GET)
    public List<Paiement> getPaiementsList(Principal principal) {

        List<Paiement> paiementList = new ArrayList<>();
        try {
            paiementList = this.paiementService.getPaiements(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return paiementList;
    }

    @RequestMapping(value = "/paiement/{id}", method = RequestMethod.GET)
    public Paiement findById(@PathVariable Long id) {

        Paiement paiement = new Paiement();
        try {
            paiement = this.paiementService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return paiement;
    }

    @RequestMapping(value = "paiement/code-planification/{codePlanification}", method = RequestMethod.GET)
    public Paiement findByCodePlanification(@PathVariable String codePlanification) {

        Paiement paiement = new Paiement();
        try {
            paiement = this.paiementService.findByCodePlanification(codePlanification);
        } catch (Exception e) {
            System.out.println("Erreur" + e.getMessage());
        }
        return paiement;
    }

    @RequestMapping(value = "/paiement/ajouter", method = RequestMethod.POST)
    public ResponseEntity<?> ajouterPaiement(Principal principal,
                                             @RequestParam(value = "preuve", required = false) MultipartFile file,
                                             String paiementJson) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Paiement paiement = objectMapper.readValue(paiementJson, Paiement.class);

        try {

            if (file != null){
                String nomPreuve = paiementService.enregistrerPreuve(file);
                paiement.setPreuve(nomPreuve);
            }

            if (paiement.getPlanificationPaiement().getTypePlanification().equals("Paiement de location")) {
                paiement = paiementService.savePaiementLocation(paiement, principal);
            } else {
                paiement = paiementService.savePaiementAchat(paiement, principal);
            }
            return ResponseEntity.status(HttpStatus.OK).body(paiement);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du paiement : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/paiement/valider/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void valider(@PathVariable Long id, Principal principal) {
        this.paiementService.validerPaiement(id, principal);
    }

    @RequestMapping(value = "/paiement/generer-pdf/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generatePaiementPdf(@PathVariable Long id) throws IOException, java.io.IOException {
        byte[] pdfBytes = paiementService.generatePdf(id);
        Paiement paiement = paiementService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "paiement" + paiement.getCodePaiement() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }

    @RequestMapping(value = "/paiement/preuve/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> telechargerPreuve(@PathVariable Long id) {
        Paiement paiement = paiementService.findById(id);

        try {
            String cheminFichier = paiementService.construireCheminFichier(paiement);
            byte[] imageBytes = fileManagerService.lireFichier(cheminFichier);

            HttpHeaders headers = fileManagerService.construireHeaders(cheminFichier, imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (java.io.IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedFileTypeException e) {
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }
}

