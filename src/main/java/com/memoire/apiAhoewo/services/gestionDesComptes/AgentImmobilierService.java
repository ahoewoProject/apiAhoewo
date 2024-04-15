package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;

import java.security.Principal;
import java.util.List;

public interface AgentImmobilierService {
    public List<AgentImmobilier> getAll();

    public AgentImmobilier findById(Long id);

    public AgentImmobilier findByMatricule(String matricule);

    public AgentImmobilier save(AgentImmobilier agentImmobilier, Principal principal);

    boolean matriculeExists(String matricule);
}
