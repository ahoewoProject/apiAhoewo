package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;

import java.security.Principal;
import java.util.List;

public interface DelegationGestionService {

    List<DelegationGestion> getAllByProprietaire(Principal principal);

    List<DelegationGestion> getAllByGestionnaire(Principal principal);

    DelegationGestion findById(Long id);

    public DelegationGestion save(DelegationGestion delegationGestion, Principal principal);

    void accepterDelegationGestion(Long id);

    void supprimerDelegationGestion(Long id);

    int countDelegationGestionProprietaire(Principal principal);

    int countDelegationGestionGestionnaire(Principal principal);
}
