package com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Quartier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {
    List<Quartier> findByEtat(Boolean etat);

    Quartier findByLibelle(String libelle);
}
