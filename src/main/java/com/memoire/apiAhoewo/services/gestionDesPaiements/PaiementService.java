package com.memoire.apiAhoewo.services.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesPaiements.Paiement;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface PaiementService {
    Page<Paiement> getPaiements(Principal principal, int numeroDeLaPage, int elementsParPage);

    Paiement findById(Long id);

    List<Paiement> dernierePaiement(String codeContrat);

    Paiement savePaiementLocation(Paiement paiement, Principal principal);

    Paiement savePaiementAchat(Paiement paiement, Principal principal);

    byte[] generatePdf(Long id) throws IOException;
}
