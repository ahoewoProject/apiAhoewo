package com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenceImmobiliereRepository extends JpaRepository<AgenceImmobiliere, Long> {
    List<AgenceImmobiliere> findByAgentImmobilier(AgentImmobilier agentImmobilier);
    AgenceImmobiliere findByCreerPar(Long id);
    AgenceImmobiliere findByNomAgence(String nomAgence);
}
