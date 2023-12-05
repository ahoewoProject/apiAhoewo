package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Administrateur;

import java.security.Principal;
import java.util.List;

public interface AdministrateurService {
    public List<Administrateur> getAll();

    public Administrateur findById(Long id);

    public Administrateur save(Administrateur administrateur, Principal principal);

    public void deleteById(Long id);

    public int countAdministrateurs();
}
