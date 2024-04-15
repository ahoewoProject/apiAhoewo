package com.memoire.apiAhoewo.servicesImpls.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres.AffectationAgentAgenceRepository;
import com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres.AffectationResponsableAgenceRepository;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.AffectationAgentAgenceService;
import com.memoire.apiAhoewo.services.gestionDesComptes.AgentImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<AffectationResponsableAgence> getAgencesByAgentPaginees(Principal principal, int numeroDeLaPage, int elementsParPage) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        List<AffectationAgentAgence> agentAgences =  affectationAgentAgenceRepository.findByAgentImmobilier(agentImmobilier);

        List<AgenceImmobiliere> agenceImmobilieres = agentAgences.stream()
                .map(AffectationAgentAgence::getAgenceImmobiliere)
                .collect(Collectors.toList());

        return affectationResponsableAgenceRepository.findAllByAgenceImmobiliereInOrderByCreerLeDesc(agenceImmobilieres, pageRequest);
    }

    @Override
    public AffectationAgentAgence findById(Long id) {
        return affectationAgentAgenceRepository.findById(id).orElse(null);
    }

    @Override
    public AffectationAgentAgence save(AgentImmobilier agentImmobilier,
                                       AgenceImmobiliere agenceImmobiliere, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        AffectationAgentAgence affectationAgentAgenceSaved = new AffectationAgentAgence();
        if (agentImmobilier.getMatricule().isEmpty()) {
            agentImmobilier = agentImmobilierService.save(agentImmobilier, principal);
        }
        affectationAgentAgenceSaved.setAgentImmobilier(agentImmobilier);
        affectationAgentAgenceSaved.setAgenceImmobiliere(agenceImmobiliere);
        affectationAgentAgenceSaved.setDateAffectation(new Date());
        affectationAgentAgenceSaved.setActif(true);
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

    @Override
    public boolean agenceAndAgentAndActifExists(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier, Boolean actif) {
        return this.affectationAgentAgenceRepository.existsByAgenceImmobiliereAndAgentImmobilierAndActif(agenceImmobiliere,
                agentImmobilier, actif);
    }

    @Override
    public boolean agenceAndMatriculeAgentAndActifExists(AgenceImmobiliere agenceImmobiliere, String matricule, Boolean actif) {
        return this.affectationAgentAgenceRepository.existsByAgenceImmobiliereAndAgentImmobilier_MatriculeAndActif(
                agenceImmobiliere, matricule, actif);
    }

    @Override
    public void activerCompteAgentAgence(Long id) {
        AffectationAgentAgence affectationAgentAgence = this.affectationAgentAgenceRepository.findById(id).orElse(null);
        affectationAgentAgence.setActif(true);
        affectationAgentAgence.setDateAffectation(new Date());
        affectationAgentAgence.setDateFin(null);
        affectationAgentAgenceRepository.save(affectationAgentAgence);
    }

    @Override
    public void desactiverCompteAgentAgence(Long id) {
        AffectationAgentAgence affectationAgentAgence = this.affectationAgentAgenceRepository.findById(id).orElse(null);
        affectationAgentAgence.setActif(false);
        affectationAgentAgence.setDateFin(new Date());
        affectationAgentAgenceRepository.save(affectationAgentAgence);
    }
}
