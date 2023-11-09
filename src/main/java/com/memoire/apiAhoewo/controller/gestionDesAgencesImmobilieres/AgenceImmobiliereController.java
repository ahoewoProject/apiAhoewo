package com.memoire.apiAhoewo.controller.gestionDesAgencesImmobilieres;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.exception.UnsupportedFileTypeException;
import com.memoire.apiAhoewo.fileManager.FileFilter;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.service.FileManagerService;
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
    private FileManagerService fileManagerService;

    @RequestMapping(value = "/agences-immobilieres", method = RequestMethod.GET)
    public List<AgenceImmobiliere> getAll() {

        List<AgenceImmobiliere> agenceImmobilieres = new ArrayList<>();
        try {
            agenceImmobilieres = this.agenceImmobiliereService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agenceImmobilieres;
    }

    @RequestMapping(value = "/agences-immobilieres/agent-immobilier", method = RequestMethod.GET)
    public List<AgenceImmobiliere> getAllByAgentImmobilier(Principal principal) {

        List<AgenceImmobiliere> agenceImmobiliereList = new ArrayList<>();
        try {
            agenceImmobiliereList = this.agenceImmobiliereService.getAllByAgentImmobilier(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agenceImmobiliereList;
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

    /*@RequestMapping(value = "/agence-immobiliere/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterAgenceImmobiliere(Principal principal, @RequestBody AgenceImmobiliere agenceImmobiliere) {
        try {
            AgenceImmobiliere existingAgenceImmobiliere = agenceImmobiliereService.findByNomAgence(agenceImmobiliere.getNomAgence());

            if (existingAgenceImmobiliere != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une agence immobilière avec ce nom " + agenceImmobiliere.getNomAgence() + " existe déjà.");
            }

            agenceImmobiliere = this.agenceImmobiliereService.save(agenceImmobiliere, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du rôle : " + e.getMessage());
        }
        return ResponseEntity.ok(agenceImmobiliere);
    }*/

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

        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de l'agence immobilière : " + e.getMessage());
        }
        return ResponseEntity.ok(agenceImmobiliere);
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

        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification de l'agence immobilière : " + e.getMessage());
        }
        return ResponseEntity.ok(agenceImmobiliere);
    }

    /*@RequestMapping(value = "/agence-immobiliere/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierAgenceImmobiliere(Principal principal, @RequestBody AgenceImmobiliere agenceImmobiliereModifie, @PathVariable  Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findById(id);
        AgenceImmobiliere existingAgenceImmobiliere = agenceImmobiliereService.findByNomAgence(agenceImmobiliere.getNomAgence());
        try {
            if (existingAgenceImmobiliere != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Une agence immobilière avec ce nom " + agenceImmobiliere.getNomAgence() + " existe déjà.");
            }
            agenceImmobiliere.setNomAgence(agenceImmobiliereModifie.getNomAgence());
            agenceImmobiliere.setAdresse(agenceImmobiliereModifie.getAdresse());
            agenceImmobiliere.setTelephone(agenceImmobiliereModifie.getTelephone());
            agenceImmobiliere.setAdresseEmail(agenceImmobiliereModifie.getAdresseEmail());
            agenceImmobiliere.setHeureOuverture(agenceImmobiliereModifie.getHeureOuverture());
            agenceImmobiliere.setHeureFermeture(agenceImmobiliereModifie.getHeureFermeture());
            agenceImmobiliere = this.agenceImmobiliereService.update(agenceImmobiliere, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return ResponseEntity.ok(agenceImmobiliere);
    }*/

    @RequestMapping(value = "/activer/agence-immobiliere/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerAgenceImmobiliere(@PathVariable Long id){
        this.agenceImmobiliereService.activerAgence(id);
    }

    @RequestMapping(value = "/desactiver/agence-immobiliere/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverAgenceImmobiliere(@PathVariable Long id){
        this.agenceImmobiliereService.desactiverAgence(id);
    }

    @RequestMapping(value = "/agence-immobiliere/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerAgenceImmobiliere(@PathVariable Long id) {
        this.agenceImmobiliereService.deleteById(id);
    }

    @RequestMapping(value = "/count/agences-immobilieres", method = RequestMethod.GET)
    public int nombreAgencesImmobilieres(){
        int nombres = this.agenceImmobiliereService.countAgencesImmobilieres();
        return nombres;
    }

    @RequestMapping(value = "/count/agences-immobilieres/agent-immobilier", method = RequestMethod.GET)
    public int nombreAgenceImmobiliere(Principal principal){
        int nombres = this.agenceImmobiliereService.countAgencesByAgentImmobilier(principal);
        return nombres;
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
