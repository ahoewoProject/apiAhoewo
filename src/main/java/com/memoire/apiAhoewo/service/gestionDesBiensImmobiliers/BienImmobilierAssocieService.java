package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmAssocie;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface BienImmobilierAssocieService {
    Page<BienImmAssocie> getBiensAssociesPaginesByBienImmobilier(Long id, int numeroDeLaPage, int elementsParPage);

    List<BienImmAssocie> getBiensAssocies(BienImmobilier bienImmobilier);

    public BienImmAssocie findById(Long id);

    public BienImmAssocie save(BienImmAssocie magasin, Principal principal);

    public BienImmAssocie update(BienImmAssocie magasin, Principal principal);

    public boolean bienImmobilierExists(BienImmobilier bienImmobilier);
}
