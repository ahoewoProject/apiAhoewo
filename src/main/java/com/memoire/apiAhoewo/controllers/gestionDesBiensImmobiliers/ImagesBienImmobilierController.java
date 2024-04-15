package com.memoire.apiAhoewo.controllers.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.exceptions.UnsupportedFileTypeException;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.ImagesBienImmobilier;
import com.memoire.apiAhoewo.services.FileManagerService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.ImagesBienImmobilierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ImagesBienImmobilierController {
    @Autowired
    private ImagesBienImmobilierService imagesBienImmobilierService;
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private FileManagerService fileManagerService;

    @RequestMapping(value = "/images/bien-immobilier/{id}", method = RequestMethod.GET)
    public List<ImagesBienImmobilier> getImagesByBienImmobilier(@PathVariable Long id) {

        List<ImagesBienImmobilier> imagesBienImmobilierList = new ArrayList<>();

        try {
            BienImmobilier bienImmobilier = bienImmobilierService.findById(id);
            imagesBienImmobilierList = this.imagesBienImmobilierService.findByBienImmobilier(bienImmobilier);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return imagesBienImmobilierList;
    }

    @RequestMapping(value = "/premiere-image/bien-immobilier/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPremiereImageBienImmobilier(@PathVariable Long id) {
        BienImmobilier bienImmobilier = bienImmobilierService.findById(id);
        List<ImagesBienImmobilier> imagesBienImmobilierList = this.imagesBienImmobilierService.findByBienImmobilier(bienImmobilier);

        if (!imagesBienImmobilierList.isEmpty()) {
            ImagesBienImmobilier premiereImageBienImmobilier = imagesBienImmobilierList.get(0);
            ImagesBienImmobilier imagesBienImmobilier = imagesBienImmobilierService.findById(premiereImageBienImmobilier.getId());

            try {
                String cheminFichier = imagesBienImmobilierService.construireCheminFichier(imagesBienImmobilier);
                byte[] imageBytes = fileManagerService.lireFichier(cheminFichier);

                HttpHeaders headers = fileManagerService.construireHeaders(cheminFichier, imageBytes.length);
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (UnsupportedFileTypeException e) {
                return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
        } else {
            // Si aucune image n'est trouvée, renvoyer une image par défaut
            try {
                String cheminFichierImageParDefaut = "src/main/resources/imagesBienImmobilier/house.jpg";
                byte[] imageParDefautBytes = fileManagerService.lireFichier(cheminFichierImageParDefaut);

                HttpHeaders headers = fileManagerService.construireHeaders(cheminFichierImageParDefaut, imageParDefautBytes.length);
                return new ResponseEntity<>(imageParDefautBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    @RequestMapping(value = "/image/bien-immobilier/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageBienImmobilier(@PathVariable Long id) {
        ImagesBienImmobilier imagesBienImmobilier = imagesBienImmobilierService.findById(id);

        try {
            String cheminFichier = imagesBienImmobilierService.construireCheminFichier(imagesBienImmobilier);
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
