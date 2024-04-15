package com.memoire.apiAhoewo.servicesImpls.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Pays;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers.PaysRepository;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.PaysService;
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
        return paysRepository.findAllByOrderByCreerLeDesc(pageRequest);
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
        pays.setCodePays("PAYS" + UUID.randomUUID());
        pays.setEtat(true);
        pays.setCreerLe(new Date());
        pays.setCreerPar(personne.getId());
        pays.setStatut(true);
        Pays paysInsere = paysRepository.save(pays);
        paysInsere.setCodePays("PAYS00" + paysInsere.getId());
        return paysRepository.save(paysInsere);
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
