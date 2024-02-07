package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Caracteristiques;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.requestForm.DelegationGestionForm2;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface DelegationGestionService {
    Page<DelegationGestion> getDelegationsByProprietairePaginees(Principal principal, int numeroDeLaPage, int elementsParPage);

    Page<DelegationGestion> getDelegationsByGestionnairePaginees(Principal principal, int numeroDeLaPage, int elementsParPage);

    Page<DelegationGestion> getDelegationsOfAgencesByResponsablePaginees(Principal principal, int numeroDeLaPage, int elementsParPage);

    Page<DelegationGestion> getDelegationsOfAgencesByAgentPaginees(Principal principal, int numeroDeLaPage, int elementsParPage);

    List<DelegationGestion> getDelegationsByProprietaire(Principal principal);

    List<DelegationGestion> getDelegationsByGestionnaire(Principal principal);

    List<DelegationGestion> getDelegationsOfAgencesByResponsable(Principal principal);

    List<DelegationGestion> getDelegationsOfAgencesByAgent(Principal principal);

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
