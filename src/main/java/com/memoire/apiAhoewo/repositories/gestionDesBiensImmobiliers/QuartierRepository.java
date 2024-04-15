package com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Quartier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Ville;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {
    Page<Quartier> findAllByOrderByCreerLeDesc(Pageable pageable);

    List<Quartier> findByEtat(Boolean etat);

    List<Quartier> findByVille_Id(Long id);

    Quartier findByLibelle(String libelle);

    boolean existsByLibelleAndVille(String libelle, Ville ville);
}
