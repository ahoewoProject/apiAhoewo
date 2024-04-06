package com.memoire.apiAhoewo.controller.gestionDesLocationsEtVentes;

import com.itextpdf.io.exceptions.IOException;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.ContratVente;
import com.memoire.apiAhoewo.requestForm.MotifRejetForm;
import com.memoire.apiAhoewo.service.gestionDesLocationsEtVentes.ContratVenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContratVenteController {
    @Autowired
    private ContratVenteService contratVenteService;

    @RequestMapping(value = "/contrats-ventes", method = RequestMethod.GET)
    public Page<ContratVente> getContratsVentes(Principal principal,
                                                @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.contratVenteService.getContratVentes(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des contrats de ventes.", e);
        }
    }

    @RequestMapping(value = "/contrat-vente/{id}", method = RequestMethod.GET)
    public ContratVente findById(@PathVariable Long id) {

        ContratVente contratVente = new ContratVente();
        try {
            contratVente = this.contratVenteService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return contratVente;
    }

    @RequestMapping(value = "/contrat-vente/ajouter", method = RequestMethod.POST)
    public ResponseEntity<?> ajouterContratVente(Principal principal, @RequestBody ContratVente contratVente) {
        try {
            if (contratVenteService.existingContratLocationByDemandeAchat(contratVente.getDemandeAchat())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un contrat de vente existe déjà pour cette demande d'achat.");
            } else if (contratVenteService.existingContratLocationByBienImmobilierAndEtatContrat(
                    contratVente.getBienImmobilier(), "Validé")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un contrat de vente est déjà validé pour ce bien immobilier.");
            } else {
                ContratVente contratVenteAdd = this.contratVenteService.save(contratVente, principal);
                return ResponseEntity.status(HttpStatus.OK).body(contratVenteAdd);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la soumission de la demande d'achat : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/contrat-vente/modifier/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> modifierContratVente(@PathVariable Long id, Principal principal, @RequestBody ContratVente contratVenteEdit) {
        try {
            contratVenteEdit.setId(id);
            ContratVente contratVenteUpdate = contratVenteService.modifier(principal, contratVenteEdit);
            return ResponseEntity.status(HttpStatus.OK).body(contratVenteUpdate);

        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du contrat de vente : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/contrat-vente/valider/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> validerContratVente(Principal principal, @PathVariable Long id) {
        ContratVente contratVente = contratVenteService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (contratVenteService.existingContratLocationByBienImmobilierAndEtatContrat(
                contratVente.getBienImmobilier(), "Validé")) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Un contrat de vente est déjà validé pour ce bien immobilier.");
        } else {
            contratVenteService.valider(principal, id);

            response.put("message", "Le contrat de vente a été validé avec succès.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @RequestMapping(value = "/contrat-vente/demande-modification/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void demandeModificationContratVente(@PathVariable Long id, @RequestBody MotifRejetForm motifRejetForm, Principal principal) {
        this.contratVenteService.demandeModification(principal, id, motifRejetForm);
    }

    @RequestMapping(value = "/contrat-vente/refuser/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void refuserContratVente(@PathVariable Long id, @RequestBody MotifRejetForm motifRejetForm, Principal principal) {
        this.contratVenteService.refuser(principal, id, motifRejetForm);
    }

    @RequestMapping(value = "/contrat-vente/generer-pdf/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generateContratVentePdf(@PathVariable Long id) throws IOException, MalformedURLException {
        byte[] pdfBytes = contratVenteService.generateContratVentePdf(id);
        ContratVente contratVente = contratVenteService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "contrat-vente" + contratVente.getCodeContrat() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }
}