package com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmAssocie;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BienImmAssocieRepository extends JpaRepository<BienImmAssocie, Long> {
    Page<BienImmAssocie> findAllByBienImmobilierOrderByCreerLeDesc(BienImmobilier bienImmobilier, Pageable pageable);

    List<BienImmAssocie> findAllByBienImmobilier(BienImmobilier bienImmobilier);

    boolean existsByBienImmobilier(BienImmobilier bienImmobilier);
}
