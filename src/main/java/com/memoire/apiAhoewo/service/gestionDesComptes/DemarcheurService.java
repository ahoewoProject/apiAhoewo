package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Demarcheur;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DemarcheurService {
    public List<Demarcheur> getAll();

    Page<Demarcheur> getDemarcheurs(int numeroDeLaPage, int elementsParPage);

    public Demarcheur findById(Long id);

    public void deleteById(Long id);

    public int countDemarcheurs();
}
