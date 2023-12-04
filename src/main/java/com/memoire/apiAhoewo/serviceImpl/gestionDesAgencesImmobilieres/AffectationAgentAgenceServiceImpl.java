package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AffectationAgentAgenceRepository;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AffectationResponsableAgenceRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AffectationAgentAgenceService;
import com.memoire.apiAhoewo.service.gestionDesComptes.AgentImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AffectationAgentAgenceServiceImpl implements AffectationAgentAgenceService {
    @Autowired
    private AffectationAgentAgenceRepository affectationAgentAgenceRepository;
    @Autowired
    private AffectationResponsableAgenceRepository affectationResponsableAgenceRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private AgentImmobilierService agentImmobilierService;

    @Override
    public List<AffectationAgentAgence> getAll() {
        return affectationAgentAgenceRepository.findAll();
    }

    @Override
    public List<AffectationAgentAgence> getAgentsByAgences(Principal principal) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere =
                (ResponsableAgenceImmobiliere) personneService.findByUsername(principal.getName());

        List<AffectationResponsableAgence> affectationResponsableAgences =
                affectationResponsableAgenceRepository.findByResponsableAgenceImmobiliere(responsableAgenceImmobiliere);

        List<AgenceImmobiliere> agenceImmobilieres = affectationResponsableAgences.stream()
                .map(AffectationResponsableAgence::getAgenceImmobiliere)
                .collect(Collectors.toList());

        return affectationAgentAgenceRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public List<AffectationResponsableAgence> getAgencesByAgent(Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneService.findByUsername(principal.getName());

        List<AffectationAgentAgence> agentAgences =  affectationAgentAgenceRepository.findByAgentImmobilier(agentImmobilier);

        List<AgenceImmobiliere> agenceImmobilieres = agentAgences.stream()
                .map(AffectationAgentAgence::getAgenceImmobiliere)
                .collect(Collectors.toList());

        return  affectationResponsableAgenceRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public AffectationAgentAgence findById(Long id) {
        return affectationAgentAgenceRepository.findById(id).orElse(null);
    }

    @Override
    public AffectationAgentAgence save(AgentImmobilier agentImmobilier,
                                       AgenceImmobiliere agenceImmobiliere, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        AgentImmobilier agentImmobilierSaved = agentImmobilierService.save(agentImmobilier, principal);
        AffectationAgentAgence affectationAgentAgenceSaved = new AffectationAgentAgence();
        affectationAgentAgenceSaved.setAgentImmobilier(agentImmobilierSaved);
        affectationAgentAgenceSaved.setAgenceImmobiliere(agenceImmobiliere);
        affectationAgentAgenceSaved.setDateAffectation(new Date());
        affectationAgentAgenceSaved.setCreerLe(new Date());
        affectationAgentAgenceSaved.setCreerPar(personne.getId());
        affectationAgentAgenceSaved.setStatut(true);
        return affectationAgentAgenceRepository.save(affectationAgentAgenceSaved);
    }

    @Override
    public boolean agenceAndAgentExists(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier) {
        return this.affectationAgentAgenceRepository.existsByAgenceImmobiliereAndAgentImmobilier(agenceImmobiliere, agentImmobilier);
    }

    @Override
    public boolean agenceAndMatriculeAgentExists(AgenceImmobiliere agenceImmobiliere, String matricule) {
        return this.affectationAgentAgenceRepository.existsByAgenceImmobiliereAndAgentImmobilier_Matricule(agenceImmobiliere, matricule);
    }
}
