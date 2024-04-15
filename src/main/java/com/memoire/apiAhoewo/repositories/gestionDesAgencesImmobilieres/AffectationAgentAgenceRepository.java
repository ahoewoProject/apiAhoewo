package com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffectationAgentAgenceRepository extends JpaRepository<AffectationAgentAgence, Long> {
    List<AffectationAgentAgence> findByAgenceImmobiliereIn(List<AgenceImmobiliere> agenceImmobiliereList);

    List<AffectationAgentAgence> findByAgentImmobilier(AgentImmobilier agentImmobilier);
     
    List<AffectationAgentAgence> findByAgentImmobilierAndActif(AgentImmobilier agentImmobilier, Boolean actif);

    List<AffectationAgentAgence> findByAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere);

    boolean existsByAgenceImmobiliereAndAgentImmobilier(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier);

    boolean existsByAgenceImmobiliereAndAgentImmobilier_Matricule(AgenceImmobiliere agenceImmobiliere, String matricule);

    boolean existsByAgenceImmobiliereAndAgentImmobilierAndActif(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier, Boolean actif);

    boolean existsByAgenceImmobiliereAndAgentImmobilier_MatriculeAndActif(AgenceImmobiliere agenceImmobiliere, String matricule, Boolean actif);
}
