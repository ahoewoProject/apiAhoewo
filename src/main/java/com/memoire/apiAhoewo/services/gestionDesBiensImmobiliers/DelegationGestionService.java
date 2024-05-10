package com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.dto.DelegationGestionForm2;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Caracteristiques;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface DelegationGestionService {
    Page<DelegationGestion> getDelegationsGestions(Principal principal, int numeroDeLaPage, int elementsParPage);

    List<DelegationGestion> getDelegationsGestions(Principal principal);

    DelegationGestion getDelegationByBienImmobilier(BienImmobilier bienImmobilier);

    DelegationGestion getDelegationByBienImmobilierAndEtatDelegation(BienImmobilier bienImmobilier, Boolean etatDelegation);

    DelegationGestion findById(Long id);

    public DelegationGestion save(DelegationGestion delegationGestion, Principal principal);

    public DelegationGestion saveDelegationGestion2(DelegationGestionForm2 delegationGestionFormTwo, Caracteristiques caracteristiques, List<MultipartFile> files, Principal principal);

    public DelegationGestion updateDelegationGestionTwo(DelegationGestionForm2 delegationGestionFormTwo, Caracteristiques caracteristiques, List<MultipartFile> files, Principal principal);

    void accepterDelegationGestion(Long id);

    void refuserDelegationGestion(Long id);

    boolean bienImmobilierAndStatutDelegationAndEtatDelegationExists(BienImmobilier bienImmobilier, Integer statutDelegation, Boolean etatDelegation);

    boolean bienImmobilierAndAgenceImmobiliereExists(BienImmobilier bienImmobilier, AgenceImmobiliere agenceImmobiliere);

    boolean bienImmobilierAndGestionnaireExists(BienImmobilier bienImmobilier, Personne personne);

    void activerDelegationGestion(Long id, Principal principal);

    void desactiverDelegationGestion(Long id, Principal principal);
}
