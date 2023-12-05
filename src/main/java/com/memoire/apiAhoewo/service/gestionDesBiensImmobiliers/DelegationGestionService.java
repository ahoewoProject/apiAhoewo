package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;

import java.security.Principal;
import java.util.List;

public interface DelegationGestionService {
    List<DelegationGestion> getDelegationsByProprietaire(Principal principal);

    List<DelegationGestion> getDelegationsByGestionnaire(Principal principal);

    List<DelegationGestion> getDelegationsOfAgencesByResponsable(Principal principal);

    List<DelegationGestion> getDelegationsOfAgencesByAgent(Principal principal);

    DelegationGestion findById(Long id);

    public DelegationGestion save(DelegationGestion delegationGestion, Principal principal);

    void accepterDelegationGestion(Long id);

    void refuserDelegationGestion(Long id);

    boolean bienImmobilierAndStatutDelegationExists(BienImmobilier bienImmobilier, Integer statutDelegation);

    void supprimerDelegationGestion(Long id);
}
