package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Notaire;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesComptes.NotaireRepository;
import com.memoire.apiAhoewo.service.gestionDesComptes.NotaireService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class NotaireServiceImpl implements NotaireService {
    @Autowired
    private NotaireRepository notaireRepository;

    @Autowired
    private PersonneService personneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Notaire> getAll() {
        return notaireRepository.findAll();
    }

    @Override
    public Notaire findById(Long id) {
        return notaireRepository.findById(id).orElse(null);
    }

    @Override
    public Notaire save(Notaire notaire, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        notaire.setEtatCompte(true);
        notaire.setEstCertifie(true);
        notaire.setCreerLe(new Date());
        notaire.setCreerPar(personne.getId());
        notaire.setStatut(true);
        notaire.setMotDePasse(passwordEncoder.encode(notaire.getMotDePasse()));
        return notaireRepository.save(notaire);
    }

    @Override
    public void deleteById(Long id) {
        notaireRepository.deleteById(id);
    }

    @Override
    public int countNotaires() {
        return (int) notaireRepository.count();
    }
}
