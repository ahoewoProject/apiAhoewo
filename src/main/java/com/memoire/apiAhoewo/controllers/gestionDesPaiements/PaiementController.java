package com.memoire.apiAhoewo.controllers.gestionDesPaiements;

import com.itextpdf.io.exceptions.IOException;
import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class PaiementController {
    @Autowired
    private PaiementService paiementService;

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

    @RequestMapping(value = "paiement/contrat-id/{contratId}", method = RequestMethod.GET)
    public Paiement findByContratId(@PathVariable Long contratId) {

        Paiement paiement = new Paiement();
        try {
            paiement = this.paiementService.findByContratId(contratId);
        } catch (Exception e) {
            System.out.println("Erreur" + e.getMessage());
        }
        return paiement;
    }


    @RequestMapping(value = "/paiement/ajouter", method = RequestMethod.POST)
    public ResponseEntity<?> ajouterPaiement(@RequestBody Paiement paiement, Principal principal) {
        try {
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
}

