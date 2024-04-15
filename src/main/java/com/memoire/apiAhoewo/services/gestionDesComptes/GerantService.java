package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Gerant;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface GerantService {
    Page<Gerant> getGerants(int numeroDeLaPage, int elementsParPage);

    Page<Gerant> findGerantsByProprietaire(Principal principal, int numeroDeLaPage, int elementsParPage);

    public Gerant findById(Long id);

    public Gerant save(Gerant gerant, Principal principal);

    public void deleteById(Long id);

    public int countGerants();

    public int countGerantsByProprietaire(Principal principal);
}
