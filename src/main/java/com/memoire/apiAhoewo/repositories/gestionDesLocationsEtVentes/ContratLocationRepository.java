package com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratLocationRepository extends JpaRepository<ContratLocation, Long> {
    Page<ContratLocation> findByDemandeLocationInOrderByIdDesc(List<DemandeLocation> demandeLocations, Pageable pageable);

    List<ContratLocation> findByDemandeLocationIn(List<DemandeLocation> demandeLocations);

    boolean existsByDemandeLocation(DemandeLocation demandeLocation);

    boolean existsByBienImmobilierAndEtatContrat(BienImmobilier bienImmobilier, String etatContrat);
}
