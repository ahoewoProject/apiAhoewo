package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Pays;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Region;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface RegionService {
    List<Region> getAll();

    Page<Region> getRegionsPaginees(int numeroDeLaPage, int elementsParPage);

    List<Region> getRegionsActifs(Boolean etat);

    List<Region> regionsByPaysId(Long id);

    Region findById(Long id);

    Region findByLibelle(String libelle);

    Region save(Region region, Principal principal);

    Region update(Region region, Principal principal);

    boolean libelleAndPaysExists(String libelle, Pays pays);

    void activerRegion(Long id);

    void desactiverRegion(Long id);
}
