package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;

import java.security.Principal;
import java.util.List;

public interface ServicesService {

    public List<Services> getAll();

    public List<Services> servicesActifs();

    public Services findById(Long id);

    public Services findByNomService(String nomService);

    public Services save(Services services, Principal principal);

    public Services update(Services services, Principal principal);

    public void activerServices(Long id);

    public void desactiverServices(Long id);
}
