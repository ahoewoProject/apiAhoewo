package com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.SuiviEntretien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuiviEntretienRepository extends JpaRepository<SuiviEntretien, Long> {
    Page<SuiviEntretien> findByContratLocationInOrderByIdDesc(List<ContratLocation> contratLocations, Pageable pageable);

    List<SuiviEntretien> findByContratLocation_CodeContratOrderByIdDesc(String codeContrat);
}
