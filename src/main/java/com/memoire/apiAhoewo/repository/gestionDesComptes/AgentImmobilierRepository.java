package com.memoire.apiAhoewo.repository.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentImmobilierRepository extends JpaRepository<AgentImmobilier, Long> {
    AgentImmobilier findByMatricule(String matricule);
    boolean existsByMatricule(String matricule);
}
