package com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.dto.MotifForm;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface ServicesService {
    public List<Services> getAll();

    Page<Services> getServices(int numeroDeLaPage, int elementsParPage);

    Page<Services> getAutresServices(int numeroDeLaPage, int elementsParPage);

    public List<Services> servicesActifs();

    public Services findById(Long id);

    public Services findByNomService(String nomService);

    public Services save(Services services, Principal principal);

    public Services update(Services services, Principal principal);

    public void activerServices(Long id);

    public void desactiverServices(Long id);

    public void validerServices(Long id, Principal principal);

    public void rejeterServices(MotifForm motifRejetForm, Principal principal);
}
