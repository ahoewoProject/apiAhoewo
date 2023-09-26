package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;

import java.security.Principal;
import java.util.List;

public interface GerantService {
    public List<Gerant> getAll();

    public List<Gerant> findGerantsByProprietaire(Principal principal);

    public Gerant findById(Long id);

    public Gerant save(Gerant gerant, Principal principal);

    public void deleteById(Long id);

    public int countGerants();
}
