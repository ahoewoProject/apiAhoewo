package com.memoire.apiAhoewo.repositories.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Demarcheur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemarcheurRepository extends JpaRepository<Demarcheur, Long> {
    Page<Demarcheur> findAllByOrderByCreerLeDesc(Pageable pageable);
}
