package com.memoire.apiAhoewo.controller.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.requestForm.MotifRejetForm;
import com.memoire.apiAhoewo.service.gestionDesLocationsEtVentes.ContratLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContratLocationController {
    @Autowired
    private ContratLocationService contratLocationService;

    @RequestMapping(value = "/contrats-locations", method = RequestMethod.GET)
    public Page<ContratLocation> getContratsLocations(Principal principal,
                                                @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.contratLocationService.getContratLocations(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des contrats de locations.", e);
        }
    }

    @RequestMapping(value = "/contrat-location/{id}", method = RequestMethod.GET)
    public ContratLocation findById(@PathVariable Long id) {

        ContratLocation contratLocation = new ContratLocation();
        try {
            contratLocation = this.contratLocationService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return contratLocation;
    }

    @RequestMapping(value = "/contrat-location/ajouter", method = RequestMethod.POST)
    public ResponseEntity<?> ajouterContratLocation(Principal principal, @RequestBody ContratLocation contratLocation) {
        try {
            if (contratLocationService.existingContratLocationByDemandeLocation(contratLocation.getDemandeLocation())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un contrat de location existe déjà pour cette demande de location.");
            } else if (contratLocationService.existingContratLocationByBienImmobilierAndEtatContrat(
                    contratLocation.getBienImmobilier(), "En cours")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un contrat de location est déjà en cours pour ce bien immobilier.");
            } else {
                ContratLocation contratLocationAdd = this.contratLocationService.save(contratLocation, principal);
                return ResponseEntity.status(HttpStatus.OK).body(contratLocationAdd);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la soumission de la demande de location : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/contrat-location/modifier/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> modifierContratLocation(@PathVariable Long id, Principal principal, @RequestBody ContratLocation contratLocationEdit) {
        try {
            contratLocationEdit.setId(id);
            ContratLocation contratLocationUpdate = contratLocationService.modifier(principal, contratLocationEdit);
            return ResponseEntity.status(HttpStatus.OK).body(contratLocationUpdate);

        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du contrat de location : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/contrat-location/valider/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> validerContratLocation(Principal principal, @PathVariable Long id) {
        ContratLocation contratLocation = contratLocationService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (contratLocationService.existingContratLocationByBienImmobilierAndEtatContrat(
                contratLocation.getBienImmobilier(), "En cours")) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Un contrat de location est déjà en cours pour ce bien immobilier.");
        } else {
            contratLocationService.valider(principal, id);

            response.put("message", "Le contrat de location a été validé avec succès.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @RequestMapping(value = "/contrat-location/demande-modification/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void demandeModificationContratLocation(@PathVariable Long id, @RequestBody MotifRejetForm motifRejetForm, Principal principal) {
        this.contratLocationService.demandeModification(principal, motifRejetForm, id);
    }

    @RequestMapping(value = "/contrat-location/refuser/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void refuserContratLocation(@PathVariable Long id, @RequestBody MotifRejetForm motifRejetForm, Principal principal) {
        this.contratLocationService.refuser(principal, id, motifRejetForm);
    }

    @RequestMapping(value = "/contrat-location/mettre-fin/{id}", method = RequestMethod.GET)
    public void mettreFin(@PathVariable Long id, Principal principal) {
        this.contratLocationService.mettreFin(principal, id);
    }

    @RequestMapping(value = "/contrat-location/generer-pdf/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generateContratLocationPdf(@PathVariable Long id) throws IOException {
        byte[] pdfBytes = contratLocationService.generateContratLocationPdf(id);
        ContratLocation contratLocation = contratLocationService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "contrat-location" + contratLocation.getCodeContrat() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }

}
