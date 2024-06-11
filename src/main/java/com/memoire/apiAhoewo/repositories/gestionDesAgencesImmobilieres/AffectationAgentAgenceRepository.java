package com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffectationAgentAgenceRepository extends JpaRepository<AffectationAgentAgence, Long> {
    Page<AffectationAgentAgence> findByAgentImmobilierOrderByIdDesc(AgentImmobilier agentImmobilier, Pageable pageable);

    Page<AffectationAgentAgence> findByAgenceImmobiliereInOrderByIdDesc(List<AgenceImmobiliere> agenceImmobiliereList, Pageable pageable);

    List<AffectationAgentAgence> findByAgenceImmobiliereInOrderByIdDesc(List<AgenceImmobiliere> agenceImmobiliereList);

    List<AffectationAgentAgence> findByAgentImmobilierOrderByIdDesc(AgentImmobilier agentImmobilier);

    List<AffectationAgentAgence> findByAgentImmobilierAndActif(AgentImmobilier agentImmobilier, Boolean actif);

    List<AffectationAgentAgence> findByAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere);

    boolean existsByAgenceImmobiliereAndAgentImmobilier(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier);

    boolean existsByAgenceImmobiliereAndAgentImmobilier_Matricule(AgenceImmobiliere agenceImmobiliere, String matricule);

    boolean existsByAgenceImmobiliereAndAgentImmobilierAndActif(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier, Boolean actif);

    boolean existsByAgenceImmobiliereAndAgentImmobilier_MatriculeAndActif(AgenceImmobiliere agenceImmobiliere, String matricule, Boolean actif);
}
