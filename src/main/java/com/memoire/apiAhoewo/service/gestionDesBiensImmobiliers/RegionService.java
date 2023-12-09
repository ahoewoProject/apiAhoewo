package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Region;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface RegionService {
    List<Region> getAll();

    Page<Region> getRegionsPaginees(int numeroDeLaPage, int elementsParPage);

    List<Region> getRegionsActifs(Boolean etat);

    Region findById(Long id);

    Region findByLibelle(String libelle);

    Region save(Region region, Principal principal);

    Region update(Region region, Principal principal);

    void activerRegion(Long id);

    void desactiverRegion(Long id);
}
