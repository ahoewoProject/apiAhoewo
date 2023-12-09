package com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Quartier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {
    @Override
    Page<Quartier> findAll(Pageable pageable);

    List<Quartier> findByEtat(Boolean etat);

    Quartier findByLibelle(String libelle);
}
