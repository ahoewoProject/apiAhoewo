package com.memoire.apiAhoewo.repositories.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentImmobilierRepository extends JpaRepository<AgentImmobilier, Long> {
    AgentImmobilier findByMatricule(String matricule);
    boolean existsByMatricule(String matricule);
}
