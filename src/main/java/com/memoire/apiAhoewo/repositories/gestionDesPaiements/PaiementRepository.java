package com.memoire.apiAhoewo.repositories.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import com.memoire.apiAhoewo.models.gestionDesPaiements.PlanificationPaiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Page<Paiement> findByPlanificationPaiementInOrderByIdDesc(List<PlanificationPaiement> planificationPaiementList, Pageable pageable);

    List<Paiement> findByPlanificationPaiement_Contrat_CodeContrat(String codeContrat);
}
