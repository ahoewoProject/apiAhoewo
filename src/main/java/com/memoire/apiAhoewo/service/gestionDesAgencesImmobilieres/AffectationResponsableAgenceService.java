package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;

import java.security.Principal;
import java.util.List;

public interface AffectationResponsableAgenceService {

    public List<AffectationResponsableAgence> getAll();

    AffectationResponsableAgence findById(Long id);

    public AffectationResponsableAgence save(AffectationResponsableAgence affectationResponsableAgence, Principal principal);

    public AffectationResponsableAgence update(AffectationResponsableAgence affectationResponsableAgence, Principal principal);
}
