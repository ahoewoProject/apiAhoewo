package com.memoire.apiAhoewo.service.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Client;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.DemandeVisite;
import com.memoire.apiAhoewo.model.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.requestForm.MotifRejetForm;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface DemandeVisiteService {
    Page<DemandeVisite> getDemandesVisites(Principal principal, int numeroDeLaPage, int elementsParPage);

    DemandeVisite findById(Long id);

    DemandeVisite soumettre(DemandeVisite demandeVisite,Principal principal);

    DemandeVisite modifier(DemandeVisite demandeVisite, Principal principal);

    void relancer(Long id, Principal principal);

    void valider(Long id, Principal principal);

    void annuler(Long id, MotifRejetForm motifRejetForm, Principal principal);

    void refuser(Long id, MotifRejetForm motifRejetForm, Principal principal);

    boolean clientAndPublicationExist(Client client, Publication publication);
}