package com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.ImagesBienImmobilier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesBienImmobilierRepository extends JpaRepository<ImagesBienImmobilier, Long> {
    List<ImagesBienImmobilier> findByBienImmobilier(BienImmobilier bienImmobilier);
}
