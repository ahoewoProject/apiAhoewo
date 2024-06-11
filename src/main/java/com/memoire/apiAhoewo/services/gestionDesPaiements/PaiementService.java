package com.memoire.apiAhoewo.services.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface PaiementService {
    Page<Paiement> getPaiements(Principal principal, int numeroDeLaPage, int elementsParPage);

    Page<Paiement> getPaiementsByCodeContrat(String codeContrat, int numeroDeLaPage, int elementsParPage);

    List<Paiement> getPaiements(Principal principal);

    List<Paiement> getPaiementByModePaiementEnAttente();

    List<Paiement> getPaiementsByStatutPaiementIfPayoutBatchIdExist();

    Paiement findById(Long id);

    Paiement findByPayoutBatchId(String payoutBatchId);

    Paiement findByCodePlanification(String codePlanification);

    Paiement findByContratId(Long id);

    List<Paiement> dernierePaiement(String codeContrat);

    Paiement savePaiementLocation(Paiement paiement, Principal principal);

    Paiement savePaiementAchat(Paiement paiement, Principal principal);

    Paiement savePaiementByClientIfStatutPaiementIsPending(Paiement paiement);

    Paiement savePaiementLocationByClientIfStatutIsCompleted(Paiement paiement);

    Paiement savePaiementAchatByClientIfStatutIsCompleted(Paiement paiement);

    void savePaiementIsCompleted(Long id);

    void validerPaiement(Long id, Principal principal);

    byte[] generatePdf(Long id) throws IOException;

    String enregistrerPreuve(MultipartFile file);

    String construireCheminFichier(Paiement paiement);

    void setPaiement(Paiement paiement);
}
