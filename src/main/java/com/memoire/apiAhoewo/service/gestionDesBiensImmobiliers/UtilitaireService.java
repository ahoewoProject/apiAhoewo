package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Utilitaire;

import java.security.Principal;

public interface UtilitaireService {
    Utilitaire getByBienImmobilier(Long id);
    Utilitaire save(BienImmobilier bienImmobilier, Utilitaire utilitaire, Principal principal);
    Utilitaire update(BienImmobilier bienImmobilier, Utilitaire utilitaire, Principal principal);
}
