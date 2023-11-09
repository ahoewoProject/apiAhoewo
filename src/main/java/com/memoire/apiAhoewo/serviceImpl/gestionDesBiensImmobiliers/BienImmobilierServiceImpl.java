package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.BienImmobilierRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.BienImmobilierService;
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
public class BienImmobilierServiceImpl implements BienImmobilierService {
    @Autowired
    private BienImmobilierRepository bienImmobilierRepository;

    @Autowired
    private PersonneService personneService;

    @Override
    public List<BienImmobilier> getAll() {
        return bienImmobilierRepository.findAll();
    }

    @Override
    public List<BienImmobilier> getAllByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return bienImmobilierRepository.findByPersonne(personne);
    }

    @Override
    public List<BienImmobilier> getAllByGerant(Principal principal) {
        Gerant gerant = (Gerant) personneService.findByUsername(principal.getName());
        Personne personne = personneService.findById(gerant.getCreerPar());
        return bienImmobilierRepository.findByPersonne(personne);
    }

    @Override
    public BienImmobilier findById(Long id) {
        return bienImmobilierRepository.findById(id).orElse(null);
    }

    @Override
    public BienImmobilier save(BienImmobilier bienImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        bienImmobilier.setPersonne(personne);
        bienImmobilier.setStatutBien("Disponible");
        bienImmobilier.setEtatBien(true);
        bienImmobilier.setCreerLe(new Date());
        bienImmobilier.setCreerPar(personne.getId());
        bienImmobilier.setStatut(true);
        BienImmobilier bienImmobilierInsere = bienImmobilierRepository.save(bienImmobilier);
        bienImmobilierInsere.setNumeroIdentifiant("BIEN - 00"+bienImmobilierInsere.getId());
        return bienImmobilierRepository.save(bienImmobilierInsere);
    }

    @Override
    public BienImmobilier update(BienImmobilier bienImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        bienImmobilier.setModifierLe(new Date());
        bienImmobilier.setModifierPar(personne.getId());
        return bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void activerBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierRepository.findById(id).orElse(null);
        bienImmobilier.setEtatBien(true);
        bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void desactiverBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierRepository.findById(id).orElse(null);
        bienImmobilier.setEtatBien(false);
        bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void deleteById(Long id) {
        bienImmobilierRepository.deleteById(id);
    }

    @Override
    public int countBienImmobilierByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByPersonne(personne);
        int count = bienImmobiliers.size();
        return count;
    }

    @Override
    public int countBienImmobilierByGerant(Principal principal) {
        Gerant gerant = (Gerant) personneService.findByUsername(principal.getName());
        Personne personne = personneService.findById(gerant.getCreerPar());
        List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByPersonne(personne);
        int count = bienImmobiliers.size();
        return count;
    }

    /* Fonction pour l'enregistrement de l'image principale d'un bien immobilier */
    @Override
    public String enregistrerImagePrincipaleDuBien(MultipartFile file) {
        String nomImagePrincipaleDuBien = null;

        try {
            String repertoireImage = "src/main/resources/imagesBienImmobilier";
            File repertoire = creerRepertoire(repertoireImage);

            String imagePrincipaleDuBien = file.getOriginalFilename();
            nomImagePrincipaleDuBien = FilenameUtils.
                    getBaseName(imagePrincipaleDuBien) + "." + FilenameUtils.getExtension(imagePrincipaleDuBien);

            File ressourceImage = new File(repertoire, nomImagePrincipaleDuBien);

            FileUtils.writeByteArrayToFile(ressourceImage, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nomImagePrincipaleDuBien;
    }

    /* Fonction pour la création du repertoire de l'image principale des biens immobiliers */
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

    /* Fonction pour construire le chemin vers l'image principale d'un bien immobilier */
    @Override
    public String construireCheminFichier(BienImmobilier bienImmobilier) {
        String repertoireFichier = "src/main/resources/imagesBienImmobilier";
        return repertoireFichier + "/" + bienImmobilier.getImagePrincipale();
    }
}
