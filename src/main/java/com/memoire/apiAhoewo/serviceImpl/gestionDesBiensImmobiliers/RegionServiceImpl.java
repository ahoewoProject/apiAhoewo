package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Region;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.RegionRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.RegionService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Repository
public class RegionServiceImpl implements RegionService {
    @Autowired
    private PersonneService personneService;
    @Autowired
    private RegionRepository regionRepository;

    @Override
    public List<Region> getAll() {
        return regionRepository.findAll();
    }

    @Override
    public List<Region> getRegionsActifs(Boolean etat) {
        return regionRepository.findByEtat(etat);
    }

    @Override
    public Region findById(Long id) {
        return regionRepository.findById(id).orElse(null);
    }

    @Override
    public Region findByLibelle(String libelle) {
        return regionRepository.findByLibelle(libelle);
    }

    @Override
    public Region save(Region region, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        region.setEtat(true);
        region.setCreerLe(new Date());
        region.setCreerPar(personne.getId());
        region.setStatut(true);
        return regionRepository.save(region);
    }

    @Override
    public Region update(Region region, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        region.setModifierLe(new Date());
        region.setModifierPar(personne.getId());
        return regionRepository.save(region);
    }

    @Override
    public void activerRegion(Long id) {
        Region region = regionRepository.findById(id).orElse(null);
        region.setEtat(true);
        regionRepository.save(region);
    }

    @Override
    public void desactiverRegion(Long id) {
        Region region = regionRepository.findById(id).orElse(null);
        region.setEtat(false);
        regionRepository.save(region);
    }
}
