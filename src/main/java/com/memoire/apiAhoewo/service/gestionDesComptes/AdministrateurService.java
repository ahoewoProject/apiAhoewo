package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Administrateur;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface AdministrateurService {
    public List<Administrateur> getAll();

    Page<Administrateur> getAdministrateurs(int numeroDeLaPage, int elementsParPage);

    public Administrateur findById(Long id);

    public Administrateur save(Administrateur administrateur, Principal principal);

    public void deleteById(Long id);

    public int countAdministrateurs();
}
