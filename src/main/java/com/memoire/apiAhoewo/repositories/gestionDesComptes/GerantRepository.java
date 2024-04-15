package com.memoire.apiAhoewo.repositories.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Gerant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GerantRepository extends JpaRepository<Gerant, Long> {
    Page<Gerant> findAllByOrderByCreerLeDesc(Pageable pageable);

    Page<Gerant> findAllByCreerParOrderByCreerLeDesc(Long id, Pageable pageable);

    public List<Gerant> findByCreerPar(Long id);
}
