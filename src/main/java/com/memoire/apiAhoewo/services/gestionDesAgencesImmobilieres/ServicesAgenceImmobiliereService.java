package com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;
import com.memoire.apiAhoewo.dto.ServiceNonTrouveForm;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface ServicesAgenceImmobiliereService {
    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(Principal principal, int numeroDeLaPage, int elementsParPage);

    public Page<ServicesAgenceImmobiliere> getServicesOfAgencePagines(Long id, int numeroDeLaPage, int elementsParPage);

    public Page<ServicesAgenceImmobiliere> getServicesByNomAgence(String nomAgence, int numeroDeLaPage, int elementsParPage);

    public ServicesAgenceImmobiliere findById(Long id);

    public ServicesAgenceImmobiliere findByServices(Services services);

    public ServicesAgenceImmobiliere save(ServicesAgenceImmobiliere servicesAgenceImmobiliere,
                                          Principal principal);

    public void demandeAjoutServiceNonTrouve(Principal principal, ServiceNonTrouveForm serviceNonTrouveForm);

    public ServicesAgenceImmobiliere update(ServicesAgenceImmobiliere servicesAgenceImmobiliere,
                                            Principal principal);

    public boolean servicesAndAgenceImmobiliereExists(Services services, AgenceImmobiliere agenceImmobiliere);

    public void activerServiceAgence(Long id);

    public void desactiverServiceAgence(Long id);
}