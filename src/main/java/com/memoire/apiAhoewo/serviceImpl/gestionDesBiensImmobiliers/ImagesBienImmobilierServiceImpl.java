package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.ImagesBienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.ImagesBienImmobilierRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.ImagesBienImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class ImagesBienImmobilierServiceImpl implements ImagesBienImmobilierService {
    @Autowired
    private ImagesBienImmobilierRepository imagesBienImmobilierRepository;

    @Autowired
    private PersonneService personneService;

    @Override
    public List<ImagesBienImmobilier> getAll() {
        return imagesBienImmobilierRepository.findAll();
    }

    @Override
    public ImagesBienImmobilier findById(Long id) {
        return imagesBienImmobilierRepository.findById(id).orElse(null);
    }

    @Override
    public List<ImagesBienImmobilier> findByBienImmobilier(BienImmobilier bienImmobilier) {
        return imagesBienImmobilierRepository.findByBienImmobilier(bienImmobilier);
    }

    @Override
    public ImagesBienImmobilier save(ImagesBienImmobilier imagesBienImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        imagesBienImmobilier.setCreerLe(new Date());
        imagesBienImmobilier.setCreerPar(personne.getId());
        imagesBienImmobilier.setStatut(true);
        return imagesBienImmobilierRepository.save(imagesBienImmobilier);
    }

    @Override
    public ImagesBienImmobilier update(ImagesBienImmobilier imagesBienImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        imagesBienImmobilier.setModifierLe(new Date());
        imagesBienImmobilier.setModifierPar(personne.getId());
        return imagesBienImmobilierRepository.save(imagesBienImmobilier);
    }

    /* Fonction pour l'enregistrement des images d'un bien immobilier */
    @Override
    public String enregistrerImageDuBien(MultipartFile file) {
        String nomImageDuBien = null;

        try {
            String repertoireImage = "src/main/resources/imagesBienImmobilier";
            File repertoire = creerRepertoire(repertoireImage);

            String imageDuBien = file.getOriginalFilename();
            nomImageDuBien = FilenameUtils.
                    getBaseName(imageDuBien) + "." + FilenameUtils.getExtension(imageDuBien);

            File ressourceImage = new File(repertoire, nomImageDuBien);

            FileUtils.writeByteArrayToFile(ressourceImage, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nomImageDuBien;
    }

    /* Fonction pour la création du repertoire des images d'un bien immobilier' */
    private File creerRepertoire(String repertoireImagePrincipaleBien) {
        File repertoire = new File(repertoireImagePrincipaleBien);
        if (!repertoire.exists()) {
            boolean repertoireCree = repertoire.mkdirs();
            if (!repertoireCree) {
                throw new RuntimeException("Impossible de créer ce répertoire.");
            }
        }
        return repertoire;
    }

    /* Fonction pour construire le chemin vers les images d'un bien immobilier */
    @Override
    public String construireCheminFichier(ImagesBienImmobilier imagesBienImmobilier) {
        String repertoireFichier = "src/main/resources/imagesBienImmobilier";
        return repertoireFichier + "/" + imagesBienImmobilier.getNomImage();
    }
}
