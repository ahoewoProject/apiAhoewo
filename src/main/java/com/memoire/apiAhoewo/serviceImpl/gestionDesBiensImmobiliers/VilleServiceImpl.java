package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Ville;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.VilleRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.VilleService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class VilleServiceImpl implements VilleService {
    @Autowired
    private PersonneService personneService;
    @Autowired
    private VilleRepository villeRepository;

    @Override
    public List<Ville> getAll() {
        return villeRepository.findAll();
    }

    @Override
    public List<Ville> getRegionsActifs(Boolean etat) {
        return villeRepository.findByEtat(etat);
    }

    @Override
    public Ville findById(Long id) {
        return villeRepository.findById(id).orElse(null);
    }

    @Override
    public Ville findByLibelle(String libelle) {
        return villeRepository.findByLibelle(libelle);
    }

    @Override
    public Ville save(Ville ville, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        ville.setEtat(true);
        ville.setCreerLe(new Date());
        ville.setCreerPar(personne.getId());
        ville.setStatut(true);
        return villeRepository.save(ville);
    }

    @Override
    public Ville update(Ville ville, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        ville.setModifierLe(new Date());
        ville.setModifierPar(personne.getId());
        return villeRepository.save(ville);
    }

    @Override
    public void activerVille(Long id) {
        Ville ville = villeRepository.findById(id).orElse(null);
        ville.setEtat(true);
        villeRepository.save(ville);
    }

    @Override
    public void desactiverVille(Long id) {
        Ville ville = villeRepository.findById(id).orElse(null);
        ville.setEtat(false);
        villeRepository.save(ville);
    }
}
