package com.memoire.apiAhoewo.servicesImpls.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.ImagesBienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers.ImagesBienImmobilierRepository;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.ImagesBienImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        int nouvelleLargeur = 800;
        int nouvelleHauteur = 600;

        try {
            String repertoireImage = "src/main/resources/imagesBienImmobilier";
            File repertoire = creerRepertoire(repertoireImage);

            String imageDuBien = file.getOriginalFilename();
            nomImageDuBien = FilenameUtils.
                    getBaseName(imageDuBien) + "." + FilenameUtils.getExtension(imageDuBien);

            File ressourceImage = new File(repertoire, nomImageDuBien);

            // Redimensionner l'image avant de l'enregistrer
            File tempFile = new File(repertoire, "temp_" + nomImageDuBien);
            FileUtils.writeByteArrayToFile(tempFile, file.getBytes());
            redimensionnerImage(tempFile, ressourceImage, nouvelleLargeur, nouvelleHauteur);
            tempFile.delete(); // Supprimer le fichier temporaire
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

    /* Fonction pour redimensionner l'image avant son enregistrement */
    private void redimensionnerImage(File imageSource, File imageDest, int newWidth, int newHeight) throws Exception {
        BufferedImage image = ImageIO.read(imageSource);
        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedResizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = bufferedResizedImage.createGraphics();
        graphics2D.drawImage(resizedImage, 0, 0, null);
        graphics2D.dispose();

        ImageIO.write(bufferedResizedImage, FilenameUtils.getExtension(imageDest.getName()), imageDest);
    }

    /* Fonction pour construire le chemin vers les images d'un bien immobilier */
    @Override
    public String construireCheminFichier(ImagesBienImmobilier imagesBienImmobilier) {
        String repertoireFichier = "src/main/resources/imagesBienImmobilier";
        return repertoireFichier + "/" + imagesBienImmobilier.getNomImage();
    }
}
