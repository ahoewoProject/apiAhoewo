package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Quartier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Ville;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.QuartierRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.QuartierService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class QuartierServiceImpl implements QuartierService {
    @Autowired
    private PersonneService personneService;
    @Autowired
    private QuartierRepository quartierRepository;

    @Override
    public List<Quartier> getAll() {
        return quartierRepository.findAll();
    }

    @Override
    public Page<Quartier> getQuartiersPagines(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return quartierRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public List<Quartier> getQuartiersActifs(Boolean etat) {
        return quartierRepository.findByEtat(etat);
    }

    @Override
    public List<Quartier> quartiersByVilleId(Long id) {
        return quartierRepository.findByVille_Id(id);
    }

    @Override
    public Quartier findById(Long id) {
        return quartierRepository.findById(id).orElse(null);
    }

    @Override
    public Quartier findByLibelle(String libelle) {
        return quartierRepository.findByLibelle(libelle);
    }

    @Override
    public Quartier save(Quartier quartier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        quartier.setCodeQuartier("QUART" + UUID.randomUUID());
        quartier.setEtat(true);
        quartier.setCreerLe(new Date());
        quartier.setCreerPar(personne.getId());
        quartier.setStatut(true);
        Quartier quartierInsere = quartierRepository.save(quartier);
        quartierInsere.setCodeQuartier("QUART00" + quartierInsere.getId());
        return quartierRepository.save(quartierInsere);
    }

    @Override
    public Quartier update(Quartier quartier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        quartier.setModifierLe(new Date());
        quartier.setModifierPar(personne.getId());
        return quartierRepository.save(quartier);
    }

    @Override
    public boolean libelleAndVilleExists(String libelle, Ville ville) {
        return quartierRepository.existsByLibelleAndVille(libelle, ville);
    }

    @Override
    public void activerQuartier(Long id) {
        Quartier quartier = quartierRepository.findById(id).orElse(null);
        quartier.setEtat(true);
        quartierRepository.save(quartier);
    }

    @Override
    public void desactiverQuartier(Long id) {
        Quartier quartier = quartierRepository.findById(id).orElse(null);
        quartier.setEtat(false);
        quartierRepository.save(quartier);
    }
}
