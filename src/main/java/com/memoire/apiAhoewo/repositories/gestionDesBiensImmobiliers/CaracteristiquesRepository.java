package com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Caracteristiques;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristiquesRepository extends JpaRepository<Caracteristiques, Long> {

    Caracteristiques findByBienImmobilier(BienImmobilier bienImmobilier);
}
