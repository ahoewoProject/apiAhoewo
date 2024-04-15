package com.memoire.apiAhoewo.repositories.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.ResponsableAgenceImmobiliere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsableAgenceImmobiliereRepository extends JpaRepository<ResponsableAgenceImmobiliere, Long> {
    Page<ResponsableAgenceImmobiliere> findAllByOrderByCreerLeDesc(Pageable pageable);

    ResponsableAgenceImmobiliere findByMatricule(String matricule);

    boolean existsByMatricule(String matricule);
}
