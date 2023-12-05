package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;

import java.security.Principal;
import java.util.List;

public interface ServicesAgenceImmobiliereService {
    public List<ServicesAgenceImmobiliere> getServicesOfAgence(Principal principal);

    public List<ServicesAgenceImmobiliere> getServicesOfAgence(Long id);

    public ServicesAgenceImmobiliere findById(Long id);

    public ServicesAgenceImmobiliere save(ServicesAgenceImmobiliere servicesAgenceImmobiliere,
                                          Principal principal);

    public ServicesAgenceImmobiliere update(ServicesAgenceImmobiliere servicesAgenceImmobiliere,
                                            Principal principal);

    public boolean servicesAndAgenceImmobiliereExists(Services services, AgenceImmobiliere agenceImmobiliere);

    public void activerServiceAgence(Long id);

    public void desactiverServiceAgence(Long id);
}
