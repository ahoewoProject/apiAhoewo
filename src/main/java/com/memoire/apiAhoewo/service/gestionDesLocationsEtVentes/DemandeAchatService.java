package com.memoire.apiAhoewo.service.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Client;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.DemandeAchat;
import com.memoire.apiAhoewo.model.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.requestForm.MotifRejetForm;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface DemandeAchatService {
    Page<DemandeAchat> getDemandesAchats(Principal principal, int numeroDeLaPage, int elementsParPage);

    DemandeAchat findById(Long id);

    DemandeAchat soumettre(DemandeAchat demandeAchat, Principal principal);

    DemandeAchat modifier(DemandeAchat demandeAchat, Principal principal);

    void valider(Long id, Principal principal);

    void relancer(Long id, Principal principal);

    void annuler(Long id, MotifRejetForm motifRejetForm, Principal principal);

    void refuser(Long id, MotifRejetForm motifRejetForm, Principal principal);

    boolean clientAndPublicationExist(Client client, Publication publication);
}
