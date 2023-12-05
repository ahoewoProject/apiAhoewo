package com.memoire.apiAhoewo.controller.gestionDesAgencesImmobilieres;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.exception.UnsupportedFileTypeException;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.service.FileManagerService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AffectationResponsableAgenceService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
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
public class AgenceImmobiliereController {
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;
    @Autowired
    private AffectationResponsableAgenceService affectationResponsableAgenceService;
    @Autowired
    private FileManagerService fileManagerService;

    @RequestMapping(value = "/agences-immobilieres", method = RequestMethod.GET)
    public List<AffectationResponsableAgence> getAll() {

        List<AffectationResponsableAgence> affectationResponsableAgences = new ArrayList<>();
        try {
            affectationResponsableAgences = this.affectationResponsableAgenceService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return affectationResponsableAgences;
    }

    @RequestMapping(value = "/agences-immobilieres/responsable", method = RequestMethod.GET)
    public List<AgenceImmobiliere> findAgencesByResponsable(Principal principal) {

        List<AgenceImmobiliere> agenceImmobilieres = new ArrayList<>();
        try {
            agenceImmobilieres = this.agenceImmobiliereService.getAgencesByResponsable(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agenceImmobilieres;
    }

    @RequestMapping(value = "/agences-immobilieres/agent", method = RequestMethod.GET)
    public List<AgenceImmobiliere> findAgencesByAgent(Principal principal) {

        List<AgenceImmobiliere> agenceImmobilieres = new ArrayList<>();
        try {
            agenceImmobilieres = this.agenceImmobiliereService.getAgencesByResponsable(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agenceImmobilieres;
    }

    @RequestMapping(value = "/affectation-responsable-agence/{id}", method = RequestMethod.GET)
    public AffectationResponsableAgence detailAffectation(@PathVariable Long id) {

        AffectationResponsableAgence affectationResponsableAgence = new AffectationResponsableAgence();
        try {
            affectationResponsableAgence = this.affectationResponsableAgenceService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return affectationResponsableAgence;
    }

    @RequestMapping(value = "/agence-immobiliere/{id}", method = RequestMethod.GET)
    public AgenceImmobiliere findById(@PathVariable Long id) {

        AgenceImmobiliere agenceImmobiliere = new AgenceImmobiliere();
        try {
            agenceImmobiliere = this.agenceImmobiliereService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return agenceImmobiliere;
    }

    @RequestMapping(value = "/agence-immobiliere/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterAgenceImmobiliere(Principal principal,
                                                      @RequestParam(value = "logoAgence", required = false) MultipartFile file,
                                                      String agenceImmobiliereJson) throws JsonProcessingException {

        AgenceImmobiliere agenceImmobiliere = new ObjectMapper()
                .readValue(agenceImmobiliereJson, AgenceImmobiliere.class);

        try {
            AgenceImmobiliere agenceImmobiliereExistante  = agenceImmobiliereService.findByNomAgence(agenceImmobiliere.getNomAgence());

            if (agenceImmobiliereExistante  != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une agence immobilière avec ce nom " + agenceImmobiliere.getNomAgence() + " existe déjà.");
            }

            if (file != null){
                String nomLogo = agenceImmobiliereService.enregistrerLogo(file);
                agenceImmobiliere.setLogoAgence(nomLogo);
            }

            agenceImmobiliere = this.agenceImmobiliereService.save(agenceImmobiliere, principal);
            return ResponseEntity.ok(agenceImmobiliere);

        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de l'agence immobilière : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/agence-immobiliere/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierAgenceImmobiliere(@PathVariable Long id, Principal principal,
                                                       @RequestParam(value = "logoAgence", required = false) MultipartFile file,
                                                       String agenceImmobiliereJson) throws JsonProcessingException {

        AgenceImmobiliere agenceImmobiliereModifie = new ObjectMapper()
                .readValue(agenceImmobiliereJson, AgenceImmobiliere.class);

        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findById(id);

        try {
            AgenceImmobiliere agenceImmobiliereExistante  = agenceImmobiliereService.findByNomAgence(agenceImmobiliere.getNomAgence());
            if (agenceImmobiliereExistante  != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une agence immobilière avec ce nom " + agenceImmobiliere.getNomAgence() + " existe déjà.");
            }

            if (file != null){
                String nomLogo = agenceImmobiliereService.enregistrerLogo(file);
                agenceImmobiliere.setLogoAgence(nomLogo);
            }
            agenceImmobiliere.setNomAgence(agenceImmobiliereModifie.getNomAgence());
            agenceImmobiliere.setAdresse(agenceImmobiliereModifie.getAdresse());
            agenceImmobiliere.setTelephone(agenceImmobiliereModifie.getTelephone());
            agenceImmobiliere.setAdresseEmail(agenceImmobiliereModifie.getAdresseEmail());
            agenceImmobiliere.setHeureOuverture(agenceImmobiliereModifie.getHeureOuverture());
            agenceImmobiliere.setHeureFermeture(agenceImmobiliereModifie.getHeureFermeture());
            agenceImmobiliere = this.agenceImmobiliereService.update(agenceImmobiliere, principal);
            return ResponseEntity.ok(agenceImmobiliere);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification de l'agence immobilière : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/activer/agence-immobiliere/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerAgence(@PathVariable Long id){
        this.agenceImmobiliereService.activerAgence(id);
    }

    @RequestMapping(value = "/desactiver/agence-immobiliere/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverAgence(@PathVariable Long id){
        this.agenceImmobiliereService.desactiverAgence(id);
    }

    @RequestMapping(value = "/logo/agence-immobiliere/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getLogoAgence(@PathVariable Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findById(id);

        try {
            String cheminFichier = agenceImmobiliereService.construireCheminFichier(agenceImmobiliere);
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
