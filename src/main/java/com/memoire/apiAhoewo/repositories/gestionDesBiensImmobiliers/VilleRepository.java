package com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Region;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Ville;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {
    Page<Ville> findAllByOrderByCreerLeDesc(Pageable pageable);

    List<Ville> findByEtat(Boolean etat);

    List<Ville> findByRegion_Id(Long id);

    Ville findByLibelle(String libelle);

    boolean existsByLibelleAndRegion(String libelle, Region region);
}
