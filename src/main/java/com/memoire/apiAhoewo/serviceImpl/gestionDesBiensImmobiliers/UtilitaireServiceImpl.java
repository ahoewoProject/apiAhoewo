package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Utilitaire;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.UtilitaireRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.UtilitaireService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
public class UtilitaireServiceImpl implements UtilitaireService {
    @Autowired
    private UtilitaireRepository utilitaireRepository;
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private PersonneService personneService;

    @Override
    public Utilitaire getByBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierService.findById(id);
        return utilitaireRepository.findByBienImmobilier(bienImmobilier);
    }

    @Override
    public Utilitaire save(BienImmobilier bienImmobilier, Utilitaire utilitaire, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        utilitaire.setBienImmobilier(bienImmobilier);
        utilitaire.setCreerLe(new Date());
        utilitaire.setCreerPar(personne.getId());
        utilitaire.setStatut(true);
        return utilitaireRepository.save(utilitaire);
    }

    @Override
    public Utilitaire update(BienImmobilier bienImmobilier, Utilitaire utilitaire, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        utilitaire.setModifierLe(new Date());
        utilitaire.setModifierPar(personne.getId());
        return utilitaireRepository.save(utilitaire);
    }
}
