package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Quartier;

import java.security.Principal;
import java.util.List;

public interface QuartierService {
    List<Quartier> getAll();
    List<Quartier> getQuartiersActifs(Boolean etat);
    Quartier findById(Long id);
    Quartier findByLibelle(String libelle);
    Quartier save(Quartier quartier, Principal principal);
    Quartier update(Quartier quartier, Principal principal);
    void activerQuartier(Long id);
    void desactiverQuartier(Long id);
}
