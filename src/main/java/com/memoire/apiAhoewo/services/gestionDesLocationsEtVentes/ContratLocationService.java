package com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeLocation;
import com.memoire.apiAhoewo.dto.MotifRejetForm;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface ContratLocationService {
    Page<ContratLocation> getContratLocations(Principal principal, int numeroDeLaPage, int elementsParPage);

    List<ContratLocation> getContratLocations(Principal principal);

    ContratLocation findById(Long id);

    ContratLocation save(ContratLocation contratLocation, Principal principal);

    ContratLocation modifier(Principal principal, ContratLocation contratLocation);

    void valider(Principal principal, Long id);

    void mettreFin(Principal principal, Long id);

    void refuser(Principal principal, Long id, MotifRejetForm motifRejetForm);

    void demandeModification(Principal principal, MotifRejetForm motifRejetForm, Long id);

    byte[] generateContratLocationPdf(Long id) throws IOException;

    boolean existingContratLocationByDemandeLocation(DemandeLocation demandeLocation);

    boolean existingContratLocationByBienImmobilierAndEtatContrat(BienImmobilier bienImmobilier, String etatContrat);
}
