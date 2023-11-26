package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;

import java.security.Principal;
import java.util.List;

public interface AffectationAgentAgenceService {

    List<AffectationAgentAgence> getAll();

    List<AffectationAgentAgence> getAgentsByAgences(Principal principal);

    List<AffectationResponsableAgence> getAgencesByAgent(Principal principal);

    AffectationAgentAgence findById(Long id);

    AffectationAgentAgence save(AgentImmobilier agentImmobilier,
                                AgenceImmobiliere agenceImmobiliere, Principal principal);

    boolean agenceImmobiliereAndAgentImmobiliereExists(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier);

    boolean agenceImmobiliereAndMatriculeAgentImmobilier(AgenceImmobiliere agenceImmobiliere, String matricule);
}
