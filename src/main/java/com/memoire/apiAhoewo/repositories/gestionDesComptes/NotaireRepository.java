package com.memoire.apiAhoewo.repositories.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Notaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaireRepository extends JpaRepository<Notaire, Long> {
    Page<Notaire> findAllByOrderByCreerLeDesc(Pageable pageable);

    List<Notaire> findAllByEtatCompte(Boolean etatCompte);
}
