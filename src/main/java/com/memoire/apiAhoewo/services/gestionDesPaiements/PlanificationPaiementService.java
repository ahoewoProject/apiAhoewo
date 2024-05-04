package com.memoire.apiAhoewo.services.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.Contrat;
import com.memoire.apiAhoewo.models.gestionDesPaiements.PlanificationPaiement;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.Date;
import java.util.List;

public interface PlanificationPaiementService {
    Page<PlanificationPaiement> getPlanificationsPaiement(Principal principal, int numeroDeLaPage, int elementsParPage);

    List<PlanificationPaiement> getPlanificationsPaiement(Principal principal);

    List<PlanificationPaiement> getPlanificationsPaiementEnAttente();

    List<PlanificationPaiement> getPlanificationsByCodeContrat(String codeContrat);

    PlanificationPaiement findById(Long id);

    PlanificationPaiement savePlanificationPaiementLocation(Principal principal, PlanificationPaiement planificationPaiement);

    PlanificationPaiement savePlanificationPaiementAchat(Principal principal, PlanificationPaiement planificationPaiement);

    PlanificationPaiement dernierePlanificationPaiementAchat(String codeContrat);

    void creerTransaction(PlanificationPaiement planificationPaiement);

    void setStatutPlanification(PlanificationPaiement planificationPaiement);

    boolean existsByContratAndDatePlanifiee(Contrat contrat, Date datePlanifiee);
}
