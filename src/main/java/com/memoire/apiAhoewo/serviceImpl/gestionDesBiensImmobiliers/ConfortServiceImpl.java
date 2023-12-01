package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Confort;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.ConfortRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.ConfortService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
public class ConfortServiceImpl implements ConfortService {
    @Autowired
    private ConfortRepository confortRepository;
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private PersonneService personneService;

    @Override
    public Confort getByBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierService.findById(id);
        return confortRepository.findByBienImmobilier(bienImmobilier);
    }

    @Override
    public Confort save(BienImmobilier bienImmobilier, Confort confort, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        confort.setBienImmobilier(bienImmobilier);
        confort.setCreerLe(new Date());
        confort.setCreerPar(personne.getId());
        confort.setStatut(true);
        return confortRepository.save(confort);
    }

    @Override
    public Confort update(BienImmobilier bienImmobilier, Confort confort, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        confort.setModifierLe(new Date());
        confort.setModifierPar(personne.getId());
        return confortRepository.save(confort);
    }
}
