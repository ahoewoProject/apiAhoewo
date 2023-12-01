package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Confort;

import java.security.Principal;

public interface ConfortService {
    Confort getByBienImmobilier(Long id);
    Confort save(BienImmobilier bienImmobilier, Confort confort, Principal principal);
    Confort update(BienImmobilier bienImmobilier, Confort confort, Principal principal);
}
