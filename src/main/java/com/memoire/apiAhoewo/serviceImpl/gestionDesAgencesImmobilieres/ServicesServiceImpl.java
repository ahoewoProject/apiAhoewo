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
import java.util.ArrayList;
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
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereRepository.findByAgentImmobilier(agentImmobilier);

        List<Services> allServices = new ArrayList<>();

        for (AgenceImmobiliere agenceImmobiliere : agenceImmobilieres) {
            List<Services> servicesForAgence = servicesRepository.findByAgenceImmobiliere(agenceImmobiliere);
            allServices.addAll(servicesForAgence);
        }

        return allServices;
    }


    @Override
    public Services findById(Long id) {
        return servicesRepository.findById(id).orElse(null);
    }

    @Override
    public Services saveService(Services services, Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
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
        servicesRepository.deleteById(id);
    }

    @Override
    public int countServicesByAgence(Principal principal) {
        AgentImmobilier agentImmobilier = (AgentImmobilier) personneRepository.findByUsername(principal.getName());
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereRepository.findByAgentImmobilier(agentImmobilier);

        int totalCount = 0; // Initialisation du compteur total

        for (AgenceImmobiliere agenceImmobiliere : agenceImmobilieres) {
            List<Services> servicesList = servicesRepository.findByAgenceImmobiliere(agenceImmobiliere);
            int count = servicesList.size(); // Comptage des services pour cette agence
            totalCount += count; // Ajout du compte de services pour cette agence au total
        }

        return totalCount;
    }
}
