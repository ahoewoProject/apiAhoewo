package com.memoire.apiAhoewo.serviceImpl;

import com.memoire.apiAhoewo.model.MotifRejet;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.MotifRejetRepository;
import com.memoire.apiAhoewo.service.MotifRejetService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class MotifRejetServiceImpl implements MotifRejetService {
    @Autowired
    private MotifRejetRepository motifRejetRepository;
    @Autowired
    private PersonneService personneService;

    @Override
    public List<MotifRejet> getMotifsByCodeAndCreerPar(String code, Long creerPar) {
        return motifRejetRepository.findByCodeAndCreerPar(code, creerPar);
    }

    @Override
    public MotifRejet findByCode(String code) {
        return motifRejetRepository.findByCode(code);
    }

    @Override
    public MotifRejet save(MotifRejet motifRejet, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        motifRejet.setCreerPar(personne.getId());
        motifRejet.setCreerLe(new Date());
        motifRejet.setStatut(true);
        return motifRejetRepository.save(motifRejet);
    }
}
