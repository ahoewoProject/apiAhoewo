package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Pays;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.TypeDeBien;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.PaysRepository;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.PaysService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class PaysServiceImpl implements PaysService {
    @Autowired
    private PersonneService personneService;
    @Autowired
    private PaysRepository paysRepository;

    @Override
    public List<Pays> getAll() {
        return paysRepository.findAll();
    }

    @Override
    public Page<Pays> getPaysPagines(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return paysRepository.findAll(pageRequest);
    }

    @Override
    public List<Pays> getPaysActifs(Boolean etat) {
        return paysRepository.findByEtat(etat);
    }

    @Override
    public Pays findById(Long id) {
        return paysRepository.findById(id).orElse(null);
    }

    @Override
    public Pays findByLibelle(String libelle) {
        return paysRepository.findByLibelle(libelle);
    }

    @Override
    public Pays save(Pays pays, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        pays.setEtat(true);
        pays.setCreerLe(new Date());
        pays.setCreerPar(personne.getId());
        pays.setStatut(true);
        return paysRepository.save(pays);
    }

    @Override
    public Pays update(Pays pays, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        pays.setModifierLe(new Date());
        pays.setModifierPar(personne.getId());
        return paysRepository.save(pays);
    }

    @Override
    public void activerPays(Long id) {
        Pays pays = paysRepository.findById(id).orElse(null);
        pays.setEtat(true);
        paysRepository.save(pays);
    }

    @Override
    public void desactiverPays(Long id) {
        Pays pays = paysRepository.findById(id).orElse(null);
        pays.setEtat(false);
        paysRepository.save(pays);
    }
}
