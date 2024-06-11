package com.memoire.apiAhoewo.controllers.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.dto.MotifForm;
import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeAchat;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.DemandeAchatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DemandeAchatController {
    @Autowired
    private DemandeAchatService demandeAchatService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/demandes-achats", method = RequestMethod.GET)
    public Page<DemandeAchat> getDemandesAchats(Principal principal,
                                                @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
                                                @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.demandeAchatService.getDemandesAchats(principal, numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des demandes d'achats.", e);
        }
    }

    @RequestMapping(value = "/demandes-achats-list", method = RequestMethod.GET)
    public List<DemandeAchat> getDemandesAchatsList(Principal principal) {

        List<DemandeAchat> demandeAchatList = new ArrayList<>();
        try {
            demandeAchatList = this.demandeAchatService.getDemandesAchats(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return demandeAchatList;
    }

    @RequestMapping(value = "/demande-achat/{id}", method = RequestMethod.GET)
    public DemandeAchat findById(@PathVariable Long id) {

        DemandeAchat demandeAchat = new DemandeAchat();
        try {
            demandeAchat = this.demandeAchatService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return demandeAchat;
    }

    @RequestMapping(value = "/demande-achat/soumettre", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> soumettreDemandeAchat(Principal principal, @RequestBody DemandeAchat demandeAchat) {
        Client client = (Client) personneService.findByUsername(principal.getName());

        try {
            if (demandeAchatService.clientAndPublicationExist(client, demandeAchat.getPublication())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Vous avez déjà soumis une demande d'achat pour cette publication.");
            } else {
                DemandeAchat demandeAchatAdd = this.demandeAchatService.soumettre(demandeAchat, principal);
                return ResponseEntity.status(HttpStatus.OK).body(demandeAchatAdd);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la soumission de la demande d'achat : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/demande-achat/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> soumettreDemandeAchat(@PathVariable Long id, Principal principal, @RequestBody DemandeAchat demandeAchatUpdate) {

        try {
            DemandeAchat demandeAchat = this.demandeAchatService.findById(id);
            demandeAchat.setPrixAchat(demandeAchatUpdate.getPrixAchat());
            demandeAchat.setNombreDeTranche(demandeAchatUpdate.getNombreDeTranche());

            demandeAchat = this.demandeAchatService.modifier(demandeAchat, principal);
            return ResponseEntity.status(HttpStatus.OK).body(demandeAchat);

        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification de la demande d'achat : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/demande-achat/relancer/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void relancer(@PathVariable Long id, Principal principal) {
        this.demandeAchatService.relancer(id, principal);
    }

    @RequestMapping(value = "/demande-achat/valider/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void valider(@PathVariable Long id, Principal principal) {
        this.demandeAchatService.valider(id, principal);
    }

    @RequestMapping(value = "/demande-achat/refuser/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void refuser(@PathVariable Long id, @RequestBody MotifForm motifRejetForm, Principal principal) {
        this.demandeAchatService.refuser(id, motifRejetForm, principal);
    }

    @RequestMapping(value = "/demande-achat/annuler/{id}", method = RequestMethod.POST, headers = "accept=Application/json")
    public void annuler(@PathVariable Long id, @RequestBody MotifForm motifRejetForm, Principal principal) {
        this.demandeAchatService.annuler(id, motifRejetForm, principal);
    }
}
