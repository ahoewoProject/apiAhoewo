package com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeAchat;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.dto.MotifRejetForm;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface DemandeAchatService {
    Page<DemandeAchat> getDemandesAchats(Principal principal, int numeroDeLaPage, int elementsParPage);

    List<DemandeAchat> getDemandesAchats(Principal principal);

    List<DemandeAchat> getDemandesAchatsEnAttente();

    DemandeAchat findById(Long id);

    DemandeAchat soumettre(DemandeAchat demandeAchat, Principal principal);

    DemandeAchat modifier(DemandeAchat demandeAchat, Principal principal);

    void valider(Long id, Principal principal);

    void relancer(Long id, Principal principal);

    void annuler(Long id, MotifRejetForm motifRejetForm, Principal principal);

    void refuser(Long id, MotifRejetForm motifRejetForm, Principal principal);

    boolean clientAndPublicationExist(Client client, Publication publication);
}
