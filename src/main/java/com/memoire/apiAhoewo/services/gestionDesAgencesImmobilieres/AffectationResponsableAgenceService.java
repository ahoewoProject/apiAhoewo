package com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.ResponsableAgenceImmobiliere;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface AffectationResponsableAgenceService {
    public List<AffectationResponsableAgence> findByAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere);

    public List<AffectationResponsableAgence> getAffectationsResponsableAgenceList(Principal principal);

    public Page<AffectationResponsableAgence> getAffectationsReponsableAgencePage(int numeroDeLaPage, int elementsParPage, Principal principal);

    AffectationResponsableAgence findById(Long id);

    public AffectationResponsableAgence save(AffectationResponsableAgence affectationResponsableAgence, Principal principal);

    public AffectationResponsableAgence save(ResponsableAgenceImmobiliere responsable,
                                             AgenceImmobiliere agenceImmobiliere, Principal principal);

    public AffectationResponsableAgence update(AffectationResponsableAgence affectationResponsableAgence, Principal principal);

    boolean agenceAndResponsableExists(AgenceImmobiliere agenceImmobiliere, ResponsableAgenceImmobiliere responsableAgenceImmobiliere);

    boolean agenceAndResponsableMatriculeExists(AgenceImmobiliere agenceImmobiliere, String matricule);

    void activerResponsableAgence(Long id);

    void desactiverResponsableAgence(Long id);
}
