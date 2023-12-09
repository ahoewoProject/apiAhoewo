package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResponsableAgenceImmobiliereService {
    public List<ResponsableAgenceImmobiliere> getAll();

    Page<ResponsableAgenceImmobiliere> getResponsables(int numeroDeLaPage, int elementsParPage);


    public ResponsableAgenceImmobiliere findById(Long id);

    public void deleteById(Long id);

    public int countResponsablesAgenceImmobiliere();
}
