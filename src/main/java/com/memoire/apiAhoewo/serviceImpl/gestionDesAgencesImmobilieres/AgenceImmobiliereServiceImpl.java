package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AffectationResponsableAgenceRepository;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AgenceImmobiliereRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AffectationResponsableAgenceService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AgenceImmobiliereServiceImpl implements AgenceImmobiliereService {
    @Autowired
    private AgenceImmobiliereRepository agenceImmobiliereRepository;
    @Autowired
    private AffectationResponsableAgenceRepository affectationResponsableAgenceRepository;
    @Autowired
    private AffectationResponsableAgenceService affectationResponsableAgenceService;
    @Autowired
    private PersonneService personneService;


    @Override
    public List<AgenceImmobiliere> getAgencesByResponsable(Principal principal) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere =
                (ResponsableAgenceImmobiliere) personneService.findByUsername(principal.getName());

        List<AffectationResponsableAgence> affectationResponsableAgences =
                affectationResponsableAgenceRepository.findByResponsableAgenceImmobiliere(responsableAgenceImmobiliere);

        List<AgenceImmobiliere> agenceImmobilieres = new ArrayList<>();

        for (AffectationResponsableAgence affectation: affectationResponsableAgences) {
            agenceImmobilieres.add(affectation.getAgenceImmobiliere());
        }
        return agenceImmobilieres;
    }

    @Override
    public AgenceImmobiliere findById(Long id) {
        return agenceImmobiliereRepository.findById(id).orElse(null);
    }

    @Override
    public AgenceImmobiliere findByNomAgence(String nomService) {
        return agenceImmobiliereRepository.findByNomAgence(nomService);
    }

    @Override
    public AgenceImmobiliere save(AgenceImmobiliere agenceImmobiliere, Principal principal) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = (ResponsableAgenceImmobiliere) personneService.findByUsername(principal.getName());
        agenceImmobiliere.setCodeAgence("AGENCE00");
        agenceImmobiliere.setCreerLe(new Date());
        agenceImmobiliere.setCreerPar(responsableAgenceImmobiliere.getId());
        agenceImmobiliere.setStatut(true);
        AgenceImmobiliere agenceImmobiliereInseree = agenceImmobiliereRepository.save(agenceImmobiliere);
        agenceImmobiliereInseree.setCodeAgence("AGENCE00" + agenceImmobiliereInseree.getId());
        AffectationResponsableAgence affectationResponsableAgence = new AffectationResponsableAgence();
        affectationResponsableAgence.setAgenceImmobiliere(agenceImmobiliereInseree);
        affectationResponsableAgence.setResponsableAgenceImmobiliere(responsableAgenceImmobiliere);
        affectationResponsableAgenceService.save(affectationResponsableAgence, principal);
        return agenceImmobiliereRepository.save(agenceImmobiliereInseree);
    }

    @Override
    public AgenceImmobiliere update(AgenceImmobiliere agenceImmobiliere, Principal principal) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = (ResponsableAgenceImmobiliere) personneService.findByUsername(principal.getName());
        agenceImmobiliere.setModifierLe(new Date());
        agenceImmobiliere.setModifierPar(responsableAgenceImmobiliere.getId());
        agenceImmobiliere = agenceImmobiliereRepository.save(agenceImmobiliere);
        return agenceImmobiliere;
    }

    /* Fonction pour l'enregistrement le logo d'une agence immobilière */
    @Override
    public String enregistrerLogo(MultipartFile file) {
        String nomLogo = null;

        try {
            String repertoireImage = "src/main/resources/logos";
            File repertoire = creerRepertoire(repertoireImage);

            String logo = file.getOriginalFilename();
            nomLogo = FilenameUtils.getBaseName(logo) + "." + FilenameUtils.getExtension(logo);
            File ressourceImage = new File(repertoire, nomLogo);

            FileUtils.writeByteArrayToFile(ressourceImage, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nomLogo;
    }

    /* Fonction pour la création du repertoire des logos des agences immobilières */
    private File creerRepertoire(String repertoireLogo) {
        File repertoire = new File(repertoireLogo);
        if (!repertoire.exists()) {
            boolean repertoireCree = repertoire.mkdirs();
            if (!repertoireCree) {
                throw new RuntimeException("Impossible de créer ce répertoire.");
            }
        }
        return repertoire;
    }

    /* Fonction pour construire le chemin vers le logo d'une agence immobilière */
    @Override
    public String construireCheminFichier(AgenceImmobiliere agenceImmobiliere) {
        String repertoireFichier = "src/main/resources/logos";
        return repertoireFichier + "/" + agenceImmobiliere.getLogoAgence();
    }
}
