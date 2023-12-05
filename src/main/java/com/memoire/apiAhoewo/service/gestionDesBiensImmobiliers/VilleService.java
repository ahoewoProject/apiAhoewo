package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Ville;

import java.security.Principal;
import java.util.List;

public interface VilleService {
    List<Ville> getAll();

    List<Ville> getRegionsActifs(Boolean etat);

    Ville findById(Long id);

    Ville findByLibelle(String libelle);

    Ville save(Ville ville, Principal principal);

    Ville update(Ville ville, Principal principal);

    void activerVille(Long id);

    void desactiverVille(Long id);
}
