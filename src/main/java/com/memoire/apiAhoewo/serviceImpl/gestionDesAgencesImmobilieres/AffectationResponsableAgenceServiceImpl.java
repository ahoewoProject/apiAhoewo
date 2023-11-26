package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AffectationResponsableAgenceRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AffectationResponsableAgenceService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class AffectationResponsableAgenceServiceImpl implements AffectationResponsableAgenceService {
    @Autowired
    private AffectationResponsableAgenceRepository affectationResponsableAgenceRepository;
    @Autowired
    private PersonneService personneService;

    @Override
    public List<AffectationResponsableAgence> getAll() {
        return affectationResponsableAgenceRepository.findAll();
    }

    @Override
    public AffectationResponsableAgence findById(Long id) {
        return affectationResponsableAgenceRepository.findById(id).orElse(null);
    }

    @Override
    public AffectationResponsableAgence save(AffectationResponsableAgence affectationResponsableAgence, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        affectationResponsableAgence.setCreerLe(new Date());
        affectationResponsableAgence.setCreerPar(personne.getId());
        affectationResponsableAgence.setStatut(true);
        return affectationResponsableAgenceRepository.save(affectationResponsableAgence);
    }

    @Override
    public AffectationResponsableAgence update(AffectationResponsableAgence affectationResponsableAgence, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        affectationResponsableAgence.setModifierLe(new Date());
        affectationResponsableAgence.setModifierPar(personne.getId());
        return affectationResponsableAgenceRepository.save(affectationResponsableAgence);
    }
}
