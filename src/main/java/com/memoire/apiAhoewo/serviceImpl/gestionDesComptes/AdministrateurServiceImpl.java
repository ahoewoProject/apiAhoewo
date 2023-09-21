package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Administrateur;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesComptes.AdministrateurRepository;
import com.memoire.apiAhoewo.service.gestionDesComptes.AdministrateurService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class AdministrateurServiceImpl implements AdministrateurService {
    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private PersonneService personneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Administrateur> getAll() {
        return administrateurRepository.findAll();
    }

    @Override
    public Administrateur findById(Long id) {
        return administrateurRepository.findById(id).orElse(null);
    }

    @Override
    public Administrateur save(Administrateur administrateur, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        administrateur.setEtatCompte(true);
        administrateur.setEstCertifie(true);
        administrateur.setCreerLe(new Date());
        administrateur.setCreerPar(personne.getId());
        administrateur.setStatut(true);
        administrateur.setMotDePasse(passwordEncoder.encode(administrateur.getMotDePasse()));
        return administrateurRepository.save(administrateur);
    }

    @Override
    public void deleteById(Long id) {
        administrateurRepository.deleteById(id);
    }

    @Override
    public int countAdministrateurs() {
        return (int) administrateurRepository.count();
    }
}
