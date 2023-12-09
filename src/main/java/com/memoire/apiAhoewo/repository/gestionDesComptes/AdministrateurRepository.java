package com.memoire.apiAhoewo.repository.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Administrateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    @Override
    Page<Administrateur> findAll(Pageable pageable);
}
