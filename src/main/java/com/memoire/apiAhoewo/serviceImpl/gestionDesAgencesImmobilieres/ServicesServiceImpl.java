package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AgenceImmobiliereRepository;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.ServicesRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.PersonneRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class ServicesServiceImpl implements ServicesService {

    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private AgenceImmobiliereRepository agenceImmobiliereRepository;

    @Override
    public List<Services> getAllByAgence(Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findByCreerPar(agentImmobilier.getId());
        return servicesRepository.findByAgenceImmobiliere(agenceImmobiliere);
    }

    @Override
    public Services findById(Long id) {
        return servicesRepository.findById(id).orElse(null);
    }

    @Override
    public Services saveService(Services services, Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findByCreerPar(agentImmobilier.getId());
        services.setAgenceImmobiliere(agenceImmobiliere);
        services.setCreerLe(new Date());
        services.setCreerPar(agentImmobilier.getId());
        services.setStatut(true);
        return servicesRepository.save(services);
    }

    @Override
    public Services updateService(Services services, Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        services.setModifierLe(new Date());
        services.setModifierPar(agentImmobilier.getId());
        return servicesRepository.save(services);
    }

    @Override
    public void deleteById(Long id) {
        servicesRepository.findById(id);
    }

    @Override
    public int countServicesByAgence(Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findByCreerPar(agentImmobilier.getId());
        List<Services> servicesList = servicesRepository.findByAgenceImmobiliere(agenceImmobiliere);
        int count = servicesList.size();
        return count;
    }
}
