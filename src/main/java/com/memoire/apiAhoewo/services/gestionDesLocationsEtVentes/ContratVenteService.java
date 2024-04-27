package com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.dto.MotifRejetForm;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratVente;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeAchat;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface ContratVenteService {
    Page<ContratVente> getContratVentes(Principal principal, int numeroDeLaPage, int elementsParPage);

    List<ContratVente> getContratVentes(Principal principal);

    ContratVente findById(Long id);

    ContratVente save(ContratVente contratVente, Principal principal);

    ContratVente modifier(Principal principal, ContratVente contratVente);

    ContratVente setEtatContrat(ContratVente contratVente);

    void valider(Principal principal, Long id);

    void refuser(Principal principal, Long id, MotifRejetForm motifRejetForm);

    void demandeModification(Principal principal, Long id, MotifRejetForm motifRejetForm);

    byte[] generateContratVentePdf(Long id) throws IOException;

    boolean existingContratLocationByDemandeAchat(DemandeAchat demandeAchat);

    boolean existingContratLocationByBienImmobilierAndEtatContrat(BienImmobilier bienImmobilier, String etatContrat);
}
