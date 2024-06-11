package com.memoire.apiAhoewo.servicesImpls;

import com.memoire.apiAhoewo.models.Motif;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.MotifRepository;
import com.memoire.apiAhoewo.services.MotifService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class MotifServiceImpl implements MotifService {
    @Autowired
    private MotifRepository motifRepository;
    @Autowired
    private PersonneService personneService;

    @Override
    public List<Motif> getMotifsByCodeAndCreerPar(String code, Long creerPar) {
        return motifRepository.findByCodeAndCreerPar(code, creerPar);
    }

    @Override
    public Motif findByCode(String code) {
        return motifRepository.findByCode(code);
    }

    @Override
    public Motif save(Motif motif, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        motif.setCreerPar(personne.getId());
        motif.setCreerLe(new Date());
        motif.setStatut(true);
        return motifRepository.save(motif);
    }
}
