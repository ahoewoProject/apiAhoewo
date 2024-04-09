package com.memoire.apiAhoewo.service.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.ContratVente;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.DemandeAchat;
import com.memoire.apiAhoewo.requestForm.MotifRejetForm;
import org.springframework.data.domain.Page;

import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;

public interface ContratVenteService {
    Page<ContratVente> getContratVentes(Principal principal, int numeroDeLaPage, int elementsParPage);

    List<ContratVente> getContratVentes(Principal principal);

    ContratVente findById(Long id);

    ContratVente save(ContratVente contratVente, Principal principal);

    ContratVente modifier(Principal principal, ContratVente contratVente);

    void valider(Principal principal, Long id);

    void refuser(Principal principal, Long id, MotifRejetForm motifRejetForm);

    void demandeModification(Principal principal, Long id, MotifRejetForm motifRejetForm);

    byte[] generateContratVentePdf(Long id) throws MalformedURLException;

    boolean existingContratLocationByDemandeAchat(DemandeAchat demandeAchat);

    boolean existingContratLocationByBienImmobilierAndEtatContrat(BienImmobilier bienImmobilier, String etatContrat);
}
