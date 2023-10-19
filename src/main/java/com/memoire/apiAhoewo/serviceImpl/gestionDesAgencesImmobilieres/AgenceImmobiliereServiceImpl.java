package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AgenceImmobiliereRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.PersonneRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
