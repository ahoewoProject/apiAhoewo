package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesComptes.AgentImmobilierRepository;
import com.memoire.apiAhoewo.service.gestionDesComptes.AgentImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class AgentImmobilierServiceImpl implements AgentImmobilierService {
    @Autowired
    private AgentImmobilierRepository agentImmobilierRepository;

    @Autowired
    private PersonneService personneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<AgentImmobilier> getAll() {
        return agentImmobilierRepository.findAll();
    }

    @Override
    public AgentImmobilier findById(Long id) {
        return agentImmobilierRepository.findById(id).orElse(null);
    }

    @Override
    public AgentImmobilier save(AgentImmobilier agentImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        agentImmobilier.setEtatCompte(true);
        agentImmobilier.setEstCertifie(true);
        agentImmobilier.setCreerLe(new Date());
        agentImmobilier.setCreerPar(personne.getId());
        agentImmobilier.setStatut(true);
        agentImmobilier.setMotDePasse(passwordEncoder.encode(agentImmobilier.getMotDePasse()));
        return agentImmobilierRepository.save(agentImmobilier);
    }

    @Override
    public void deleteById(Long id) {
        agentImmobilierRepository.deleteById(id);
    }

    @Override
    public int countAgentImmobiliers() {
        return (int) agentImmobilierRepository.count();
    }
}
