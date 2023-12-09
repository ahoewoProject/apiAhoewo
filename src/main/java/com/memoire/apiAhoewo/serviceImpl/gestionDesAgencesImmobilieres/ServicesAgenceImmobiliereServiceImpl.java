package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliereRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ServicesAgenceImmobiliereServiceImpl implements ServicesAgenceImmobiliereService {
    @Autowired
    private ServicesAgenceImmobiliereRepository servicesAgenceImmobiliereRepository;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;
    @Autowired
    private PersonneService personneService;

    @Override
    public List<ServicesAgenceImmobiliere> getServicesOfAgence(Principal principal) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByResponsable(principal);
        List<ServicesAgenceImmobiliere> servicesAgenceImmobilieres = new ArrayList<>();

        for (AgenceImmobiliere agenceImmobiliere: agenceImmobilieres) {
            servicesAgenceImmobilieres.addAll(servicesAgenceImmobiliereRepository.findByAgenceImmobiliere(agenceImmobiliere));
        }
        return servicesAgenceImmobilieres;
    }

    @Override
    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(Principal principal, int numeroDeLaPage, int elementsParPage) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByResponsable(principal);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return servicesAgenceImmobiliereRepository.findAllByAgenceImmobiliereIn(agenceImmobilieres, pageRequest);
    }

    @Override
    public List<ServicesAgenceImmobiliere> getServicesOfAgence(Long id) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findById(id);
        return servicesAgenceImmobiliereRepository.findByAgenceImmobiliere(agenceImmobiliere);
    }

    @Override
    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(Long id, int numeroDeLaPage, int elementsParPage) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findById(id);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return servicesAgenceImmobiliereRepository.findAllByAgenceImmobiliere(agenceImmobiliere, pageRequest);
    }

    @Override
    public ServicesAgenceImmobiliere findById(Long id) {
        return servicesAgenceImmobiliereRepository.findById(id).orElse(null);
    }

    @Override
    public ServicesAgenceImmobiliere save(ServicesAgenceImmobiliere servicesAgenceImmobiliere, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        servicesAgenceImmobiliere.setEtat(true);
        servicesAgenceImmobiliere.setCreerLe(new Date());
        servicesAgenceImmobiliere.setCreerPar(personne.getId());
        servicesAgenceImmobiliere.setStatut(true);
        return servicesAgenceImmobiliereRepository.save(servicesAgenceImmobiliere);
    }

    @Override
    public ServicesAgenceImmobiliere update(ServicesAgenceImmobiliere servicesAgenceImmobiliere, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        servicesAgenceImmobiliere.setModifierLe(new Date());
        servicesAgenceImmobiliere.setModifierPar(personne.getId());
        return servicesAgenceImmobiliereRepository.save(servicesAgenceImmobiliere);
    }

    @Override
    public boolean servicesAndAgenceImmobiliereExists(Services services, AgenceImmobiliere agenceImmobiliere) {
        return servicesAgenceImmobiliereRepository.existsByServicesAndAgenceImmobiliere(services, agenceImmobiliere);
    }

    @Override
    public void activerServiceAgence(Long id) {
        ServicesAgenceImmobiliere servicesAgenceImmobiliere = servicesAgenceImmobiliereRepository.findById(id).orElse(null);
        servicesAgenceImmobiliere.setEtat(true);
        servicesAgenceImmobiliereRepository.save(servicesAgenceImmobiliere);
    }

    @Override
    public void desactiverServiceAgence(Long id) {
        ServicesAgenceImmobiliere servicesAgenceImmobiliere = servicesAgenceImmobiliereRepository.findById(id).orElse(null);
        servicesAgenceImmobiliere.setEtat(false);
        servicesAgenceImmobiliereRepository.save(servicesAgenceImmobiliere);
    }
}
