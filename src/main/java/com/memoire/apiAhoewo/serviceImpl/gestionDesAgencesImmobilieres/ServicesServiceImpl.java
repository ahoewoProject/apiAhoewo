package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.ServicesRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.ServicesService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class ServicesServiceImpl implements ServicesService {

    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private PersonneService personneService;

    @Override
    public List<Services> getAll() {
        return servicesRepository.findAll();
    }

    @Override
    public List<Services> servicesActifs() {
        return servicesRepository.findByEtat(true);
    }

    @Override
    public Services findById(Long id) {
        return servicesRepository.findById(id).orElse(null);
    }

    @Override
    public Services findByNomService(String nomService) {
        return servicesRepository.findByNomService(nomService);
    }

    @Override
    public Services save(Services services, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        services.setCodeService("SERVI");
        services.setEtat(true);
        services.setCreerLe(new Date());
        services.setCreerPar(personne.getId());
        services.setStatut(true);
        Services servicesInsere = servicesRepository.save(services);
        servicesInsere.setCodeService("SERVI00" + servicesInsere.getId());
        return servicesRepository.save(servicesInsere);
    }

    @Override
    public Services update(Services services, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        services.setModifierLe(new Date());
        services.setModifierPar(personne.getId());
        return servicesRepository.save(services);
    }

    @Override
    public void activerServices(Long id) {
        Services services = servicesRepository.findById(id).orElse(null);
        services.setEtat(true);
        servicesRepository.save(services);
    }

    @Override
    public void desactiverServices(Long id) {
        Services services = servicesRepository.findById(id).orElse(null);
        services.setEtat(false);
        servicesRepository.save(services);
    }
}
