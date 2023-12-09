package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface ServicesAgenceImmobiliereService {
    public List<ServicesAgenceImmobiliere> getServicesOfAgence(Principal principal);

    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(Principal principal, int numeroDeLaPage, int elementsParPage);

    public List<ServicesAgenceImmobiliere> getServicesOfAgence(Long id);

    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(Long id, int numeroDeLaPage, int elementsParPage);

    public ServicesAgenceImmobiliere findById(Long id);

    public ServicesAgenceImmobiliere save(ServicesAgenceImmobiliere servicesAgenceImmobiliere,
                                          Principal principal);

    public ServicesAgenceImmobiliere update(ServicesAgenceImmobiliere servicesAgenceImmobiliere,
                                            Principal principal);

    public boolean servicesAndAgenceImmobiliereExists(Services services, AgenceImmobiliere agenceImmobiliere);

    public void activerServiceAgence(Long id);

    public void desactiverServiceAgence(Long id);
}
