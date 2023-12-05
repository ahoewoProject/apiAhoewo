package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Divertissement;

import java.security.Principal;

public interface DivertissementService {
    Divertissement getByBienImmobilier(Long id);

    Divertissement save(BienImmobilier bienImmobilier, Divertissement divertissement, Principal principal);

    Divertissement update(BienImmobilier bienImmobilier, Divertissement divertissement, Principal principal);
}
