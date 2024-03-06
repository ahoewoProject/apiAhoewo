package com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Pays;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Page<Region> findAllByOrderByCreerLeDesc(Pageable pageable);

    List<Region> findByEtat(Boolean etat);

    List<Region> findByPays_id(Long id);

    Region findByLibelle(String libelle);

    Region findByCodeRegion(String code);

    boolean existsByLibelleAndPays(String libelle, Pays pays);
}
