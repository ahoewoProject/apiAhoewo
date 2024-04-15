package com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
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

    boolean agenceAndAgentAndActifExists(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier, Boolean actif);

    boolean agenceAndMatriculeAgentAndActifExists(AgenceImmobiliere agenceImmobiliere, String matricule, Boolean actif);

    void activerCompteAgentAgence(Long id);

    void desactiverCompteAgentAgence(Long id);
}
