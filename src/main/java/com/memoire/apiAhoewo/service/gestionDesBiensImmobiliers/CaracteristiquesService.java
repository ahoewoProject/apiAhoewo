package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Caracteristiques;

import java.security.Principal;

public interface CaracteristiquesService {

    public Caracteristiques findByBienImmobilier(Long idBienImmobilier);

    public Caracteristiques save(BienImmobilier bienImmobilier, Caracteristiques caracteristiques, Principal principal);

    public Caracteristiques update(Long id, Caracteristiques caracteristiques, Principal principal);
}
