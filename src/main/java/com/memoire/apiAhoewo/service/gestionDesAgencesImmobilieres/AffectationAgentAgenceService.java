package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface AffectationAgentAgenceService {
    List<AffectationAgentAgence> getAll();

    List<AffectationAgentAgence> getAgentsByAgences(Principal principal);

    List<AffectationResponsableAgence> getAgencesByAgent(Principal principal);

    Page<AffectationResponsableAgence> getAgencesByAgentPaginees(Principal principal, int numeroDeLaPage, int elementsParPage);

    AffectationAgentAgence findById(Long id);

    AffectationAgentAgence save(AgentImmobilier agentImmobilier,
                                AgenceImmobiliere agenceImmobiliere, Principal principal);

    boolean agenceAndAgentExists(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier);

    boolean agenceAndMatriculeAgentExists(AgenceImmobiliere agenceImmobiliere, String matricule);
}
