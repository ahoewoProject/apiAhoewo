package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;

import java.security.Principal;
import java.util.List;

public interface ServicesService {

    public List<Services> getAllByAgence(Principal principal);

    public List<Services> getServicesAgenceAgentImmobilier(Principal principal);

    public Services findById(Long id);

    public Services saveService(Services services, Principal principal);

    public Services updateService(Services services, Principal principal);

    public void activerService(Long id);

    public void desactiverService(Long id);

    public void deleteById(Long id);

    public int countServicesByAgence(Principal principal);

    public int countServicesByAgentImmobilier(Principal principal);
}
