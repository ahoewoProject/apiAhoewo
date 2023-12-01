package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Divertissement;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.DivertissementRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.DivertissementService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
public class DivertissementServiceImpl implements DivertissementService {
    @Autowired
    private DivertissementRepository divertissementRepository;
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private PersonneService personneService;

    @Override
    public Divertissement getByBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierService.findById(id);
        return divertissementRepository.findByBienImmobilier(bienImmobilier);
    }

    @Override
    public Divertissement save(BienImmobilier bienImmobilier, Divertissement divertissement, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        divertissement.setBienImmobilier(bienImmobilier);
        divertissement.setCreerLe(new Date());
        divertissement.setCreerPar(personne.getId());
        divertissement.setStatut(true);
        return divertissementRepository.save(divertissement);
    }

    @Override
    public Divertissement update(BienImmobilier bienImmobilier, Divertissement divertissement, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        divertissement.setModifierLe(new Date());
        divertissement.setModifierPar(personne.getId());
        return divertissementRepository.save(divertissement);
    }
}
