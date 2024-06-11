package com.memoire.apiAhoewo.repositories.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentImmobilierRepository extends JpaRepository<AgentImmobilier, Long> {
    List<AgentImmobilier> findAllByOrderByCreerLeDesc();
    AgentImmobilier findByMatricule(String matricule);
    boolean existsByMatricule(String matricule);
}
