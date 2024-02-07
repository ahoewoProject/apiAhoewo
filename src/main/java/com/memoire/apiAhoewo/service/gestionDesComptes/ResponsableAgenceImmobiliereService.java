package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface ResponsableAgenceImmobiliereService {
    public List<ResponsableAgenceImmobiliere> getAll();

    Page<ResponsableAgenceImmobiliere> getResponsables(int numeroDeLaPage, int elementsParPage);

    public ResponsableAgenceImmobiliere findById(Long id);

    public ResponsableAgenceImmobiliere findByMatricule(String matricule);

    public ResponsableAgenceImmobiliere save(ResponsableAgenceImmobiliere responsable, AgenceImmobiliere agenceImmobiliere, Principal principal);

    boolean matriculeExists(String matricule);

    public void deleteById(Long id);

    public int countResponsablesAgenceImmobiliere();
}
