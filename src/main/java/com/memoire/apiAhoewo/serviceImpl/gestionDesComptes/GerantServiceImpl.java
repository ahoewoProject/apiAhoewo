package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesComptes.GerantRepository;
import com.memoire.apiAhoewo.service.gestionDesComptes.GerantService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class GerantServiceImpl implements GerantService {
    @Autowired
    private GerantRepository gerantRepository;

    @Autowired
    private PersonneService personneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Gerant> getAll() {
        return gerantRepository.findAll();
    }

    @Override
    public List<Gerant> findGerantsByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return gerantRepository.findByCreerPar(personne.getId());
    }

    @Override
    public Gerant findById(Long id) {
        return gerantRepository.findById(id).orElse(null);
    }

    @Override
    public Gerant save(Gerant gerant, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        gerant.setEtatCompte(true);
        gerant.setEstCertifie(true);gerant.setCreerLe(new Date());
        gerant.setCreerPar(personne.getId());
        gerant.setStatut(true);

        gerant.setMotDePasse(passwordEncoder.encode(gerant.getMotDePasse()));
        return gerantRepository.save(gerant);
    }

    @Override
    public void deleteById(Long id) {
        gerantRepository.deleteById(id);
    }

    @Override
    public int countGerants() {
        return (int) gerantRepository.count();
    }

    @Override
    public int countGerantsByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<Gerant> gerantList = gerantRepository.findByCreerPar(personne.getId());
        int count = gerantList.size();
        return count;
    }

}
