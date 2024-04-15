package com.memoire.apiAhoewo.controllers.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeVisite;
import com.memoire.apiAhoewo.dto.MotifRejetForm;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeVisiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class DemandeVisiteController {
    @Autowired
    private DemandeVisiteService demandeVisiteService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/demandes-visites", method = RequestMethod.GET)
    public Page<DemandeVisite> getDemandesVisites(Principal principal,
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.demandeVisiteService.getDemandesVisites(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des demandes de visites.", e);
        }
    }

    @RequestMapping(value = "/demande-visite/{id}", method = RequestMethod.GET)
    public DemandeVisite findById(@PathVariable Long id) {

        DemandeVisite demandeVisite = new DemandeVisite();
        try {
            demandeVisite = this.demandeVisiteService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return demandeVisite;
    }

    @RequestMapping(value = "/demande-visite/soumettre", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> soumettreDemandeVisite(Principal principal, @RequestBody DemandeVisite demandeVisite) {
        Client client = (Client) personneService.findByUsername(principal.getName());

        try {
            if (demandeVisiteService.clientAndPublicationExist(client, demandeVisite.getPublication())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Vous avez déjà soumis une demande de visite pour cette publication.");
            } else {
                DemandeVisite demandeVisiteAdd = this.demandeVisiteService.soumettre(demandeVisite, principal);
                return ResponseEntity.status(HttpStatus.OK).body(demandeVisiteAdd);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la soumission de la demande de visite : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/demande-visite/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierDemandeVisite(@PathVariable Long id, @RequestBody DemandeVisite demandeVisiteUpdate, Principal principal) {
        try {
            DemandeVisite demandeVisite = this.demandeVisiteService.findById(id);
            demandeVisite.setDateHeureVisite(demandeVisiteUpdate.getDateHeureVisite());

            demandeVisite = this.demandeVisiteService.modifier(demandeVisite, principal);

            return ResponseEntity.status(HttpStatus.OK).body(demandeVisite);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification de la demande de visite : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/demande-visite/relancer/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void relancer(@PathVariable Long id, Principal principal) {
        this.demandeVisiteService.relancer(id, principal);
    }

    @RequestMapping(value = "/demande-visite/valider/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void valider(@PathVariable Long id, Principal principal) {
        this.demandeVisiteService.valider(id, principal);
    }

    @RequestMapping(value = "/demande-visite/refuser/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void refuser(@PathVariable Long id, @RequestBody MotifRejetForm motifRejetForm, Principal principal) {
        this.demandeVisiteService.refuser(id, motifRejetForm, principal);
    }

    @RequestMapping(value = "/demande-visite/annuler/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void annuler(@PathVariable Long id, @RequestBody MotifRejetForm motifRejetForm, Principal principal) {
        this.demandeVisiteService.annuler(id, motifRejetForm, principal);
    }
}
