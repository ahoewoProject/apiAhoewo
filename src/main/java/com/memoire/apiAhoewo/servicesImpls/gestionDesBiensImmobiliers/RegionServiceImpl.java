package com.memoire.apiAhoewo.servicesImpls.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Pays;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Region;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers.RegionRepository;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.RegionService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public Page<Region> getRegionsPaginees(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return regionRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public List<Region> getRegionsActifs(Boolean etat) {
        return regionRepository.findByEtat(etat);
    }

    @Override
    public List<Region> regionsByPaysId(Long id) {
        return regionRepository.findByPays_id(id);
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
    public Region findByCode(String code) {
        return regionRepository.findByCodeRegion(code);
    }

    @Override
    public Region save(Region region, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        region.setCodeRegion("REGIO" + UUID.randomUUID());
        region.setEtat(true);
        region.setCreerLe(new Date());
        region.setCreerPar(personne.getId());
        region.setStatut(true);
        Region regionInseree = regionRepository.save(region);
        regionInseree.setCodeRegion("REGIO00" + regionInseree.getId());
        return regionRepository.save(regionInseree);
    }

    @Override
    public Region update(Region region, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        region.setModifierLe(new Date());
        region.setModifierPar(personne.getId());
        return regionRepository.save(region);
    }

    @Override
    public boolean libelleAndPaysExists(String libelle, Pays pays) {
        return regionRepository.existsByLibelleAndPays(libelle, pays);
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
