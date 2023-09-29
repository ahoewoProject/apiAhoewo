package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoire.apiAhoewo.exception.UnsupportedFileTypeException;
import com.memoire.apiAhoewo.fileManager.FileFilter;
import com.memoire.apiAhoewo.model.gestionDesComptes.DemandeCertification;
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
    private FileFilter fileFilter;
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

    @RequestMapping(value = "/demande-certification/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public DemandeCertification ajouterDemandeCertification(Principal principal,
                                                            @RequestParam("documentJustificatif") MultipartFile file,
                                                            String demandeCertificationJson) throws JsonProcessingException {

        DemandeCertification demandeCertification = new ObjectMapper().readValue(demandeCertificationJson,
                DemandeCertification.class);

        String nomDocument = enregistrerDocumentJustificatif(file);

        demandeCertification.setDocumentJustificatif(nomDocument);

        try {
            demandeCertification = sauvegarderDemandeCertification(demandeCertification, principal);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }

        return demandeCertification;
    }

    @RequestMapping(value = "/document/demande-certification/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDocumentJustificatif(@PathVariable Long id) {
        DemandeCertification demandeCertification = demandeCertificationService.findById(id);

        try {
            String cheminFichier = construireCheminFichier(demandeCertification);
            byte[] imageBytes = lireFichier(cheminFichier);

            HttpHeaders headers = construireHeaders(cheminFichier, imageBytes.length);
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

    /* Fonction pour l'enregistrement du document justificatif de
    la demande de certification */
    private String enregistrerDocumentJustificatif(MultipartFile file) {
        String nomDocument = null;

        try {
            String repertoireImage = "src/main/resources/documentsJustificatifs";
            File repertoire = creerRepertoire(repertoireImage);

            String image = file.getOriginalFilename();
            nomDocument = FilenameUtils.getBaseName(image) + "." + FilenameUtils.getExtension(image);
            File ressourceImage = new File(repertoire, nomDocument);

            FileUtils.writeByteArrayToFile(ressourceImage, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nomDocument;
    }

    /* Fonction pour la création du repertoire des documents
    justificatifs de la demande de certification */
    private File creerRepertoire(String repertoireImage) {
        File repertoire = new File(repertoireImage);
        if (!repertoire.exists()) {
            boolean repertoireCree = repertoire.mkdirs();
            if (!repertoireCree) {
                throw new RuntimeException("Impossible de créer ce répertoire.");
            }
        }
        return repertoire;
    }

    /* Fonction pour enregistrer la demande de certification en utilsant le service
    d'enregistrement de ce dernier */
    private DemandeCertification sauvegarderDemandeCertification(DemandeCertification demandeCertification, Principal principal) {
        try {
            return this.demandeCertificationService.save(demandeCertification, principal);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de la demande de certification: " + e.getMessage());
        }
    }

    /* Fonction pour construire le chemin vers le document justificatif */
    private String construireCheminFichier(DemandeCertification demandeCertification) {
        String repertoireFichier = "src/main/resources/documentsJustificatifs";
        return repertoireFichier + "/" + demandeCertification.getDocumentJustificatif();
    }

    /* Fonction pour lire le document, c'est-à-dire afficher le document en visualisant son
    contenu */
    private byte[] lireFichier(String cheminFichier) throws IOException {
        Path cheminVersFichier = Paths.get(cheminFichier);
        return Files.readAllBytes(cheminVersFichier);
    }

    /* Fonction pour l'entête de, c'est-à-dire, de quel type de fichier,
    s'agit-il, ainsi de suite */
    private HttpHeaders construireHeaders(String cheminFichier, long contentLength) throws UnsupportedFileTypeException {
        String contentType = fileFilter.determineContentType(cheminFichier);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(contentLength);

        return headers;
    }
}
