package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;

import java.security.Principal;
import java.util.List;

public interface ServicesService {

    public List<Services> getAllByAgence(Principal principal);

    public Services findById(Long id);

    public Services saveService(Services services, Principal principal);

    public Services updateService(Services services, Principal principal);

    public void deleteById(Long id);

    public int countServicesByAgence(Principal principal);
}
