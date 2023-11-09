package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AgenceImmobiliereRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.PersonneRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
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
public class AgenceImmobiliereServiceImpl implements AgenceImmobiliereService {

    @Autowired
    private AgenceImmobiliereRepository agenceImmobiliereRepository;

    @Autowired
    private PersonneRepository personneRepository;

    @Override
    public List<AgenceImmobiliere> getAll() {
        return agenceImmobiliereRepository.findAll();
    }

    @Override
    public List<AgenceImmobiliere> getAllByAgentImmobilier(Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        return agenceImmobiliereRepository.findByAgentImmobilier(agentImmobilier);
    }

    @Override
    public AgenceImmobiliere findById(Long id) {
        return agenceImmobiliereRepository.findById(id).orElse(null);
    }

    @Override
    public AgenceImmobiliere findByNomAgence(String nomAgence) {
        return agenceImmobiliereRepository.findByNomAgence(nomAgence);
    }

    @Override
    public AgenceImmobiliere save(AgenceImmobiliere agenceImmobiliere, Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        agenceImmobiliere.setAgentImmobilier(agentImmobilier);
        agenceImmobiliere.setCreerLe(new Date());
        agenceImmobiliere.setCreerPar(agentImmobilier.getId());
        agenceImmobiliere.setStatut(true);
        return agenceImmobiliereRepository.save(agenceImmobiliere);
    }

    @Override
    public AgenceImmobiliere update(AgenceImmobiliere agenceImmobiliere, Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        agenceImmobiliere.setModifierLe(new Date());
        agenceImmobiliere.setModifierPar(agentImmobilier.getId());
        return agenceImmobiliereRepository.save(agenceImmobiliere);
    }

    @Override
    public void activerAgence(Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findById(id).orElse(null);
        agenceImmobiliere.setEtatAgence(true);
        agenceImmobiliereRepository.save(agenceImmobiliere);
    }

    @Override
    public void desactiverAgence(Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findById(id).orElse(null);
        agenceImmobiliere.setEtatAgence(false);
        agenceImmobiliereRepository.save(agenceImmobiliere);
    }

    @Override
    public void deleteById(Long id) {
        agenceImmobiliereRepository.deleteById(id);
    }

    @Override
    public int countAgencesImmobilieres() {
        List<AgenceImmobiliere> agenceImmobiliereList = agenceImmobiliereRepository.findAll();
        int count = agenceImmobiliereList.size();
        return count;
    }

    @Override
    public int countAgencesByAgentImmobilier(Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereRepository.findByAgentImmobilier(agentImmobilier);
        int count = agenceImmobilieres.size();
        return count;
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
