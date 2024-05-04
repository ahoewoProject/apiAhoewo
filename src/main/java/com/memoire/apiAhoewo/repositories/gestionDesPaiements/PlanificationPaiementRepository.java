package com.memoire.apiAhoewo.repositories.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.Contrat;
import com.memoire.apiAhoewo.models.gestionDesPaiements.PlanificationPaiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlanificationPaiementRepository extends JpaRepository<PlanificationPaiement, Long> {
    Page<PlanificationPaiement> findByContratInOrderByIdDesc(List<Contrat> contratList, Pageable pageable);

    List<PlanificationPaiement> findByContratIn(List<Contrat> contratList);

    PlanificationPaiement findByContrat_CodeContratOrderByCreerLeDesc(String codeContrat);

    List<PlanificationPaiement> findByContrat_CodeContrat(String codeContrat);

    List<PlanificationPaiement> findByStatutPlanification(String planification);

    boolean existsByContratAndDatePlanifiee(Contrat contrat, Date datePlanifiee);
}
