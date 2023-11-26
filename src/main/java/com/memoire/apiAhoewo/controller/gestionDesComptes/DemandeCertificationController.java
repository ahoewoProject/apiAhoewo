package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.exception.UnsupportedFileTypeException;
import com.memoire.apiAhoewo.fileManager.FileFilter;
import com.memoire.apiAhoewo.model.gestionDesComptes.DemandeCertification;
import com.memoire.apiAhoewo.service.FileManagerService;
import com.memoire.apiAhoewo.service.gestionDesComptes.DemandeCertificationService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DemandeCertificationController {

    @Autowired
    private DemandeCertificationService demandeCertificationService;
    @Autowired
    private FileManagerService fileManagerService;

    @RequestMapping(value = "/demandes-certifications", method = RequestMethod.GET)
    public List<DemandeCertification> getAll(){

        List<DemandeCertification> demandeCertifications = new ArrayList<>();
        try {
            demandeCertifications = this.demandeCertificationService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return demandeCertifications;
    }

    @RequestMapping(value = "/demande-certification/{id}", method = RequestMethod.GET)
    public DemandeCertification findById(@PathVariable Long id) {

        DemandeCertification demandeCertification = new DemandeCertification();
        try {
            demandeCertification = this.demandeCertificationService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return demandeCertification;
    }

    @RequestMapping(value = "/user/demande-certification", method = RequestMethod.GET)
    public List<DemandeCertification> getByUser(Principal principal){

        List<DemandeCertification> demandeCertificationList = new ArrayList<>();
        try {
            demandeCertificationList = this.demandeCertificationService.getByUser(principal);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return demandeCertificationList;
    }

    @RequestMapping(value = "/compte/demande-certification/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public DemandeCertification ajouterDemandeCertificationCompte(Principal principal,
                                                            @RequestParam(value = "cni", required = false) MultipartFile file,
                                                            String demandeCertificationJson) throws JsonProcessingException {

        DemandeCertification demandeCertification = new ObjectMapper()
                .readValue(demandeCertificationJson, DemandeCertification.class);
        try {
            if (file != null){
                String nomDocument = demandeCertificationService.enregistrerDocumentJustificatif(file);
                demandeCertification.setDocumentJustificatif(nomDocument);
            }
            demandeCertification = sauvegarderDemandeCertificationCompte(demandeCertification, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }

        return demandeCertification;
    }

    @RequestMapping(value = "/agence/demande-certification/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public DemandeCertification ajouterDemandeCertificationAgence(Principal principal,
                                                                  @RequestParam(value = "cni", required = false) MultipartFile file,
                                                                  @RequestParam(value = "carteCfe", required = false) MultipartFile file2,
                                                                  String demandeCertificationJson) throws JsonProcessingException {

        DemandeCertification demandeCertification = new ObjectMapper().readValue(demandeCertificationJson,
                DemandeCertification.class);
        try {
            if (file != null){
                String nomDocument = demandeCertificationService.enregistrerDocumentJustificatif(file);
                demandeCertification.setDocumentJustificatif(nomDocument);
            }
            if (file2 != null) {
                String nomCarteCfe = demandeCertificationService.enregistrerDocumentJustificatif(file2);
                demandeCertification.setCarteCfe(nomCarteCfe);
            }
            demandeCertification = sauvegarderDemandeCertificationAgence(demandeCertification, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }

        return demandeCertification;
    }

    @RequestMapping(value = "/cni/demande-certification/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDocumentJustificatif(@PathVariable Long id) {
        DemandeCertification demandeCertification = demandeCertificationService.findById(id);

        try {
            String cheminFichier = demandeCertificationService.construireCheminFichier(demandeCertification.getDocumentJustificatif());
            byte[] imageBytes = fileManagerService.lireFichier(cheminFichier);

            HttpHeaders headers = fileManagerService.construireHeaders(cheminFichier, imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedFileTypeException e) {
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @RequestMapping(value = "/carte-cfe/demande-certification/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCarteCfe(@PathVariable Long id) {
        DemandeCertification demandeCertification = demandeCertificationService.findById(id);

        try {
            String cheminFichier = demandeCertificationService.construireCheminFichier(demandeCertification.getCarteCfe());
            byte[] imageBytes = fileManagerService.lireFichier(cheminFichier);

            HttpHeaders headers = fileManagerService.construireHeaders(cheminFichier, imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedFileTypeException e) {
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @RequestMapping(value = "/certifier/user/{idPersonne}/{idDemandeCertif}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void certifierCompte(@PathVariable Long idPersonne, @PathVariable Long idDemandeCertif){
        this.demandeCertificationService.certifierCompte(idPersonne, idDemandeCertif);
    }

    @RequestMapping(value = "/certifier/agence/{idAgence}/{idDemandeCertif}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void certifierAgence(@PathVariable Long idAgence, @PathVariable Long idDemandeCertif){
        this.demandeCertificationService.certifierAgence(idAgence, idDemandeCertif);
    }

    @RequestMapping(value = "/count/demandes-certifications", method = RequestMethod.GET)
    public int nombreDemandeCertifications(){
        int nombres = this.demandeCertificationService.countDemandeCertifications();
        return nombres;
    }

    @RequestMapping(value = "/count/demandes-certifications/validees", method = RequestMethod.GET)
    public int nombreDemandeCertificationsValidees(){
        int nombres = this.demandeCertificationService.countDemandeCertifValidees();
        return nombres;
    }

    @RequestMapping(value = "/count/demandes-certifications/en-attente", method = RequestMethod.GET)
    public int nombreDemandeCertificationsEnAttente(){
        int nombres = this.demandeCertificationService.countDemandeCertifEnAttente();
        return nombres;
    }

    /* Fonction pour enregistrer la demande de certification de compte en utilisant le service
    d'enregistrement de ce dernier */
    private DemandeCertification sauvegarderDemandeCertificationCompte(DemandeCertification demandeCertification, Principal principal) {
        try {
            return this.demandeCertificationService.saveDemandeCertificationCompte(demandeCertification, principal);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de la demande de certification: " + e.getMessage());
        }
    }

    /* Fonction pour enregistrer la demande de certification d'une agence en utilisant le service
    d'enregistrement de ce dernier */
    private DemandeCertification sauvegarderDemandeCertificationAgence(DemandeCertification demandeCertification, Principal principal) {
        try {
            return this.demandeCertificationService.saveDemandeCertificationAgence(demandeCertification, principal);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de la demande de certification: " + e.getMessage());
        }
    }
}
