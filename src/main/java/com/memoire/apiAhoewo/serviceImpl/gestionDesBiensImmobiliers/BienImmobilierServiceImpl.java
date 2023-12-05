package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.BienImmobilierRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class BienImmobilierServiceImpl implements BienImmobilierService {
    @Autowired
    private BienImmobilierRepository bienImmobilierRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;

    @Override
    public List<BienImmobilier> getAll() {
        return bienImmobilierRepository.findAll();
    }

    @Override
    public List<BienImmobilier> getAllByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return bienImmobilierRepository.findByPersonne(personne);
    }

    @Override
    public List<BienImmobilier> getBiensOfAgencesByResponsable(Principal principal) {

        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByResponsable(principal);

        return bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public List<BienImmobilier> getBiensOfAgencesByAgent(Principal principal) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByAgent(principal);

        return bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public BienImmobilier findById(Long id) {
        return bienImmobilierRepository.findById(id).orElse(null);
    }

    @Override
    public BienImmobilier save(BienImmobilier bienImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        bienImmobilier.setPersonne(personne);
        bienImmobilier.setStatutBien("Disponible");
        bienImmobilier.setEtatBien(true);
        bienImmobilier.setCreerLe(new Date());
        bienImmobilier.setCreerPar(personne.getId());
        bienImmobilier.setStatut(true);
        BienImmobilier bienImmobilierInsere = bienImmobilierRepository.save(bienImmobilier);
        bienImmobilierInsere.setNumeroIdentifiant("BIEN - 00"+bienImmobilierInsere.getId());
        return bienImmobilierRepository.save(bienImmobilierInsere);
    }

    @Override
    public BienImmobilier update(BienImmobilier bienImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        bienImmobilier.setModifierLe(new Date());
        bienImmobilier.setModifierPar(personne.getId());
        return bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void activerBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierRepository.findById(id).orElse(null);
        bienImmobilier.setEtatBien(true);
        bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void desactiverBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierRepository.findById(id).orElse(null);
        bienImmobilier.setEtatBien(false);
        bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void deleteById(Long id) {
        bienImmobilierRepository.deleteById(id);
    }
}
