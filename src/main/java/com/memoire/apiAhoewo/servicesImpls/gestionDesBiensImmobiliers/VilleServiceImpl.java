package com.memoire.apiAhoewo.servicesImpls.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Region;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Ville;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers.VilleRepository;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.VilleService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public Page<Ville> getVillesPaginees(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return villeRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public List<Ville> getRegionsActifs(Boolean etat) {
        return villeRepository.findByEtat(etat);
    }

    @Override
    public List<Ville> villesByRegionId(Long id) {
        return villeRepository.findByRegion_Id(id);
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
        ville.setCodeVille("VILLE" + UUID.randomUUID());
        ville.setEtat(true);
        ville.setCreerLe(new Date());
        ville.setCreerPar(personne.getId());
        ville.setStatut(true);
        Ville villeInseree = villeRepository.save(ville);
        villeInseree.setCodeVille("VILLE00" + villeInseree.getId());
        return villeRepository.save(villeInseree);
    }

    @Override
    public Ville update(Ville ville, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        ville.setModifierLe(new Date());
        ville.setModifierPar(personne.getId());
        return villeRepository.save(ville);
    }

    @Override
    public boolean libelleAndRegionExists(String libelle, Region region) {
        return villeRepository.existsByLibelleAndRegion(libelle, region);
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
