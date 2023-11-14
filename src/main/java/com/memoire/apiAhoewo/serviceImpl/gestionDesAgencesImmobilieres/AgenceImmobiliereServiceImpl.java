package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AgenceImmobiliereRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.AgentImmobilierRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.PersonneRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.ResponsableAgenceImmobiliereRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesComptes.ResponsableAgenceImmobiliereService;
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
    private ResponsableAgenceImmobiliereService responsableAgenceImmobiliereService;

    @Autowired
    private ResponsableAgenceImmobiliereRepository responsableAgenceImmobiliereRepository;

    @Autowired
    private AgentImmobilierRepository agentImmobilierRepository;

    @Autowired
    private PersonneRepository personneRepository;

    @Override
    public List<AgenceImmobiliere> getAll() {
        return agenceImmobiliereRepository.findAll();
    }

    @Override
    public List<AgenceImmobiliere> getAllByResponsableAgenceImmobiliere(Principal principal) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = (ResponsableAgenceImmobiliere) personneRepository.findByUsername(principal.getName());
        return agenceImmobiliereRepository.findByResponsableAgenceImmobiliere(responsableAgenceImmobiliere);
    }

    @Override
    public List<AgenceImmobiliere> getAgenceImmobiliereParAgentImmobilier(Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = responsableAgenceImmobiliereService.findById(agentImmobilier.getCreerPar());
        return agenceImmobiliereRepository.findByResponsableAgenceImmobiliere(responsableAgenceImmobiliere);
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
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = (ResponsableAgenceImmobiliere) personneRepository.findByUsername(principal.getName());
        agenceImmobiliere.setResponsableAgenceImmobiliere(responsableAgenceImmobiliere);
        agenceImmobiliere.setCreerLe(new Date());
        agenceImmobiliere.setCreerPar(responsableAgenceImmobiliere.getId());
        agenceImmobiliere.setStatut(true);
        return agenceImmobiliereRepository.save(agenceImmobiliere);
    }

    @Override
    public AgenceImmobiliere update(AgenceImmobiliere agenceImmobiliere, Principal principal) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = (ResponsableAgenceImmobiliere) personneRepository.findByUsername(principal.getName());
        agenceImmobiliere.setModifierLe(new Date());
        agenceImmobiliere.setModifierPar(responsableAgenceImmobiliere.getId());
        return agenceImmobiliereRepository.save(agenceImmobiliere);
    }

    @Override
    public void activerAgence(Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findById(id).orElse(null);
        agenceImmobiliere.setEtatAgence(true);
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = responsableAgenceImmobiliereService.findById(agenceImmobiliere.getResponsableAgenceImmobiliere().getId());
        responsableAgenceImmobiliere.setEtatCompte(true);
        responsableAgenceImmobiliereRepository.save(responsableAgenceImmobiliere);
        List<AgentImmobilier> agentImmobiliers = agentImmobilierRepository.findByCreerPar(agenceImmobiliere.getResponsableAgenceImmobiliere().getId());
        if(!agentImmobiliers.isEmpty()) {
            for (AgentImmobilier agent : agentImmobiliers) {
                agent.setEtatCompte(true);
                agentImmobilierRepository.save(agent);
            }
        }
        agenceImmobiliereRepository.save(agenceImmobiliere);
    }

    @Override
    public void desactiverAgence(Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findById(id).orElse(null);
        agenceImmobiliere.setEtatAgence(false);
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = responsableAgenceImmobiliereService.findById(agenceImmobiliere.getResponsableAgenceImmobiliere().getId());
        responsableAgenceImmobiliere.setEtatCompte(false);
        responsableAgenceImmobiliereRepository.save(responsableAgenceImmobiliere);
        List<AgentImmobilier> agentImmobiliers = agentImmobilierRepository.findByCreerPar(agenceImmobiliere.getResponsableAgenceImmobiliere().getId());
        if(!agentImmobiliers.isEmpty()) {
            agentImmobiliers.forEach(agent -> {
                agent.setEtatCompte(false);
                agentImmobilierRepository.save(agent);
            });
        }
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
    public int countAgencesByResponsableAgenceImmobiliere(Principal principal) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = (ResponsableAgenceImmobiliere) personneRepository.findByUsername(principal.getName());
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereRepository.findByResponsableAgenceImmobiliere(responsableAgenceImmobiliere);
        int count = agenceImmobilieres.size();
        return count;
    }

    @Override
    public int countAgencesByAgentImmobilier(Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = responsableAgenceImmobiliereService.findById(agentImmobilier.getCreerPar());
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereRepository.findByResponsableAgenceImmobiliere(responsableAgenceImmobiliere);
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
