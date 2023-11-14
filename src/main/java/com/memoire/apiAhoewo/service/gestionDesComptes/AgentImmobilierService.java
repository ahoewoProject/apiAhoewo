package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;

import java.security.Principal;
import java.util.List;

public interface AgentImmobilierService {
    public List<AgentImmobilier> getAll();

    public List<AgentImmobilier> findAgentsImmobiliersByResponsable(Principal principal);

    public AgentImmobilier findById(Long id);

    public AgentImmobilier save(AgentImmobilier agentImmobilier, Principal principal);

    public void deleteById(Long id);

    public int countAgentImmobiliers();

    public int countAgentsImmobiliersByResponsable(Principal principal);
}
