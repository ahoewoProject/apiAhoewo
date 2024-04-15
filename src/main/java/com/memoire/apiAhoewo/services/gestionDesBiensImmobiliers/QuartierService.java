package com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Quartier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Ville;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface QuartierService {
    List<Quartier> getAll();

    Page<Quartier> getQuartiersPagines(int numeroDeLaPage, int elementsParPage);

    List<Quartier> getQuartiersActifs(Boolean etat);

    List<Quartier> quartiersByVilleId(Long id);

    Quartier findById(Long id);

    Quartier findByLibelle(String libelle);

    Quartier save(Quartier quartier, Principal principal);

    Quartier update(Quartier quartier, Principal principal);

    boolean libelleAndVilleExists(String libelle, Ville ville);

    void activerQuartier(Long id);

    void desactiverQuartier(Long id);
}
