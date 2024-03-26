package com.memoire.apiAhoewo.controller.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Client;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.DemandeLocation;
import com.memoire.apiAhoewo.requestForm.MotifRejetForm;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.service.gestionDesLocationsEtVentes.DemandeLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class DemandeLocationController {
    @Autowired
    private DemandeLocationService demandeLocationService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/demandes-locations", method = RequestMethod.GET)
    public Page<DemandeLocation> getDemandesLocations(Principal principal,
                                  @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                  @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.demandeLocationService.getDemandesLocations(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des demandes de locations.", e);
        }
    }

    @RequestMapping(value = "/demande-location/{id}", method = RequestMethod.GET)
    public DemandeLocation findById(@PathVariable Long id) {

        DemandeLocation demandeLocation = new DemandeLocation();
        try {
            demandeLocation = this.demandeLocationService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return demandeLocation;
    }

    @RequestMapping(value = "/demande-location/soumettre", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> soumettreDemandeLocation(Principal principal, @RequestBody DemandeLocation demandeLocation) {
        Client client = (Client) personneService.findByUsername(principal.getName());

        try {
            if (demandeLocationService.clientAndPublicationExist(client, demandeLocation.getPublication())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Vous avez déjà soumis une demande de location pour cette publication.");
            } else {
                DemandeLocation demandeLocationAdd = this.demandeLocationService.soumettre(demandeLocation, principal);
                return ResponseEntity.status(HttpStatus.OK).body(demandeLocationAdd);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la soumission de la demande de location : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/demande-location/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierDemandeLocation(@PathVariable Long id, @RequestBody DemandeLocation demandeLocationUpdate, Principal principal) {
        try {
            DemandeLocation demandeLocation = demandeLocationService.findById(id);
            demandeLocation.setPrixDeLocation(demandeLocationUpdate.getPrixDeLocation());
            demandeLocation.setAvance(demandeLocationUpdate.getAvance());
            demandeLocation.setCaution(demandeLocationUpdate.getCaution());

            demandeLocation = demandeLocationService.modifier(demandeLocation, principal);
            return ResponseEntity.status(HttpStatus.OK).body(demandeLocation);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification de la demande de location : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/demande-location/relancer/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void relancer(@PathVariable Long id, Principal principal) {
        this.demandeLocationService.relancer(id, principal);
    }

    @RequestMapping(value = "/demande-location/valider/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void valider(@PathVariable Long id, Principal principal) {
        this.demandeLocationService.valider(id, principal);
    }

    @RequestMapping(value = "/demande-location/refuser/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void refuser(@PathVariable Long id, @RequestBody MotifRejetForm motifRejetForm, Principal principal) {
        this.demandeLocationService.refuser(id, motifRejetForm, principal);
    }

    @RequestMapping(value = "/demande-location/annuler/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void annuler(@PathVariable Long id, @RequestBody MotifRejetForm motifRejetForm, Principal principal) {
        this.demandeLocationService.annuler(id, motifRejetForm, principal);
    }
}
