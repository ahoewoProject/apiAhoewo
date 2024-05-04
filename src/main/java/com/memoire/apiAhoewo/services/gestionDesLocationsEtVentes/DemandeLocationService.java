package com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeLocation;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.dto.MotifRejetForm;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface DemandeLocationService {
    Page<DemandeLocation> getDemandesLocations(Principal principal, int numeroDeLaPage, int elementsParPage);

    List<DemandeLocation> getDemandesLocations(Principal principal);

    List<DemandeLocation> getDemandesLocationsEnAttente();

    DemandeLocation findById(Long id);

    DemandeLocation soumettre(DemandeLocation demandeLocation, Principal principal);

    DemandeLocation modifier(DemandeLocation demandeLocation, Principal principal);

    void relancer(Long id, Principal principal);

    void valider(Long id, Principal principal);

    void annuler(Long id, MotifRejetForm motifRejetForm, Principal principal);

    void refuser(Long id, MotifRejetForm motifRejetForm, Principal principal);

    boolean clientAndPublicationExist(Client client, Publication publication);
}
