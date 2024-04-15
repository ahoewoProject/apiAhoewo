package com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Region;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Ville;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface VilleService {
    List<Ville> getAll();

    Page<Ville> getVillesPaginees(int numeroDeLaPage, int elementsParPage);

    List<Ville> getRegionsActifs(Boolean etat);

    List<Ville> villesByRegionId(Long id);

    Ville findById(Long id);

    Ville findByLibelle(String libelle);

    Ville save(Ville ville, Principal principal);

    Ville update(Ville ville, Principal principal);

    boolean libelleAndRegionExists(String libelle, Region region);

    void activerVille(Long id);

    void desactiverVille(Long id);
}
