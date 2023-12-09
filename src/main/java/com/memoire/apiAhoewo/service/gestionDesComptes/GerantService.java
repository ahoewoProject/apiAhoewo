package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface GerantService {
    Page<Gerant> getGerants(int numeroDeLaPage, int elementsParPage);

    Page<Gerant> findGerantsByProprietaire(Principal principal, int numeroDeLaPage, int elementsParPage);

    public Gerant findById(Long id);

    public Gerant save(Gerant gerant, Principal principal);

    public void deleteById(Long id);

    public int countGerants();

    public int countGerantsByProprietaire(Principal principal);
}
