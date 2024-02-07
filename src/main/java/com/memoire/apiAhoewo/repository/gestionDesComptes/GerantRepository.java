package com.memoire.apiAhoewo.repository.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
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
