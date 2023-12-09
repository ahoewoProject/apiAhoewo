package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface AffectationResponsableAgenceService {
    public List<AffectationResponsableAgence> getAll();

    public Page<AffectationResponsableAgence> getAffectationsResponsableAgence(int numeroDeLaPage, int elementsParPage);

    AffectationResponsableAgence findById(Long id);

    public AffectationResponsableAgence save(AffectationResponsableAgence affectationResponsableAgence, Principal principal);

    public AffectationResponsableAgence update(AffectationResponsableAgence affectationResponsableAgence, Principal principal);
}
