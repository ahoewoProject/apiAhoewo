package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AffectationAgentAgenceRepository;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AffectationResponsableAgenceRepository;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AgenceImmobiliereRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.AgentImmobilierRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.ResponsableAgenceImmobiliereRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AffectationResponsableAgenceService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AgenceImmobiliereServiceImpl implements AgenceImmobiliereService {
    @Autowired
    private AgenceImmobiliereRepository agenceImmobiliereRepository;
    @Autowired
    private AffectationResponsableAgenceRepository affectationResponsableAgenceRepository;
    @Autowired
    private AffectationResponsableAgenceService affectationResponsableAgenceService;
    @Autowired
    private AffectationAgentAgenceRepository affectationAgentAgenceRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private AgentImmobilierRepository agentImmobilierRepository;
    @Autowired
    private ResponsableAgenceImmobiliereRepository responsableRepository;


    @Override
    public Page<AgenceImmobiliere> getAgencesActives(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return agenceImmobiliereRepository.findByEtatAgenceOrderByIdDesc(true, pageRequest);
    }

    @Override
    public Page<AgenceImmobiliere> getAgencesActivesByRegionId(Long id, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return agenceImmobiliereRepository.findByQuartier_Ville_Region_IdAndEtatAgenceOrderByIdDesc(id, true, pageRequest);
    }

    @Override
    public Page<AgenceImmobiliere> getAgencesActivesByVilleId(Long id, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return agenceImmobiliereRepository.findByQuartier_Ville_IdAndEtatAgenceOrderByIdDesc(id, true, pageRequest);
    }

    @Override
    public Page<AgenceImmobiliere> getAgencesActivesByQuartierId(Long id, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return agenceImmobiliereRepository.findByQuartier_IdAndEtatAgenceOrderByIdDesc(id, true, pageRequest);
    }

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
    public Page<AgenceImmobiliere> getAgencesByResponsablePaginees(Principal principal, int numeroDeLaPage, int elementsParPage) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere =
                (ResponsableAgenceImmobiliere) personneService.findByUsername(principal.getName());

        // Récupérer les affectations pour le responsable
        List<AffectationResponsableAgence> affectationResponsableAgences =
                affectationResponsableAgenceRepository.findByResponsableAgenceImmobiliere(responsableAgenceImmobiliere);

        // Récupérer les IDs des agences pour ce responsable
        List<Long> idsAgences = affectationResponsableAgences.stream()
                .map(affectation -> affectation.getAgenceImmobiliere().getId())
                .collect(Collectors.toList());

        // Récupérer les agences paginées
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return agenceImmobiliereRepository.findByIdInOrderByCreerLeDesc(idsAgences, pageRequest);
    }

    @Override
    public List<AgenceImmobiliere> getAgencesByAgent(Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneService.findByUsername(principal.getName());

        List<AffectationAgentAgence> agentAgences =  affectationAgentAgenceRepository.findByAgentImmobilier(agentImmobilier);

        List<AgenceImmobiliere> agenceImmobilieres = agentAgences.stream()
                .map(AffectationAgentAgence::getAgenceImmobiliere)
                .collect(Collectors.toList());

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
    public AgenceImmobiliere findByCodeAgence(String codeAgence) {
        return agenceImmobiliereRepository.findByCodeAgence(codeAgence);
    }

    @Override
    public AgenceImmobiliere save(AgenceImmobiliere agenceImmobiliere, Principal principal) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = (ResponsableAgenceImmobiliere) personneService.findByUsername(principal.getName());
        if (responsableAgenceImmobiliere.getEstCertifie()) {
            agenceImmobiliere.setEstCertifie(true);
        }
        agenceImmobiliere.setCodeAgence("AGENCE" + UUID.randomUUID());
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

    @Override
    public void activerAgence(Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findById(id).orElse(null);
        agenceImmobiliere.setEtatAgence(true);
        agenceImmobiliereRepository.save(agenceImmobiliere);
        List<AffectationResponsableAgence> affectationResponsableAgences = affectationResponsableAgenceRepository
                .findByAgenceImmobiliere(agenceImmobiliere);

        List<AffectationAgentAgence> affectationAgentAgences = affectationAgentAgenceRepository
                .findByAgenceImmobiliere(agenceImmobiliere);

        List<AgentImmobilier> agentImmobiliers = new ArrayList<>();

        List<ResponsableAgenceImmobiliere> responsables = new ArrayList<>();

        for (AffectationResponsableAgence affectation: affectationResponsableAgences) {
            responsables.add(affectation.getResponsableAgenceImmobiliere());
        }

        for (AffectationAgentAgence affectation: affectationAgentAgences) {
            agentImmobiliers.add(affectation.getAgentImmobilier());
        }

        for (ResponsableAgenceImmobiliere responsable: responsables) {
            if (!responsable.getEtatCompte()) {
                responsable.setEtatCompte(true);
                responsableRepository.save(responsable);
            }
        }

        for (AgentImmobilier agentImmobilier: agentImmobiliers) {
            if (!agentImmobilier.getEtatCompte()) {
                agentImmobilier.setEtatCompte(true);
                agentImmobilierRepository.save(agentImmobilier);
            }
        }
    }

    @Override
    public void desactiverAgence(Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findById(id).orElse(null);
        agenceImmobiliere.setEtatAgence(false);
        agenceImmobiliereRepository.save(agenceImmobiliere);
        List<AffectationResponsableAgence> affectationResponsableAgences = affectationResponsableAgenceRepository
                .findByAgenceImmobiliere(agenceImmobiliere);

        List<AffectationAgentAgence> affectationAgentAgences = affectationAgentAgenceRepository
                .findByAgenceImmobiliere(agenceImmobiliere);

        List<AgentImmobilier> agentImmobiliers = new ArrayList<>();

        List<ResponsableAgenceImmobiliere> responsables = new ArrayList<>();

        for (AffectationResponsableAgence affectation: affectationResponsableAgences) {
            responsables.add(affectation.getResponsableAgenceImmobiliere());
        }

        for (AffectationAgentAgence affectation: affectationAgentAgences) {
            agentImmobiliers.add(affectation.getAgentImmobilier());
        }

        for (ResponsableAgenceImmobiliere responsable: responsables) {
            if (responsable.getEtatCompte()) {
                responsable.setEtatCompte(false);
                responsableRepository.save(responsable);
            }
        }

        for (AgentImmobilier agentImmobilier: agentImmobiliers) {
            if (agentImmobilier.getEtatCompte()) {
                agentImmobilier.setEtatCompte(false);
                agentImmobilierRepository.save(agentImmobilier);
            }
        }
    }

    @Override
    public boolean codeAgenceExists(String codeAgence) {
        return agenceImmobiliereRepository.existsByCodeAgence(codeAgence);
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
