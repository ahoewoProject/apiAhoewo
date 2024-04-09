package com.memoire.apiAhoewo.repository.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.DemandeLocation;
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
