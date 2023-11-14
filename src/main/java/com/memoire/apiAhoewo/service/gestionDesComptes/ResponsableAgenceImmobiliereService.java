package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;

import java.util.List;

public interface ResponsableAgenceImmobiliereService {
    public List<ResponsableAgenceImmobiliere> getAll();

    public ResponsableAgenceImmobiliere findById(Long id);

    public void deleteById(Long id);

    public int countResponsablesAgenceImmobiliere();
}
