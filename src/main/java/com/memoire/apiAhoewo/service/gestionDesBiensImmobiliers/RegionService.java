package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Region;

import java.security.Principal;
import java.util.List;

public interface RegionService {
    List<Region> getAll();

    List<Region> getRegionsActifs(Boolean etat);

    Region findById(Long id);

    Region findByLibelle(String libelle);

    Region save(Region region, Principal principal);

    Region update(Region region, Principal principal);

    void activerRegion(Long id);

    void desactiverRegion(Long id);
}
