package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Demarcheur;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DemarcheurService {
    public List<Demarcheur> getAll();

    Page<Demarcheur> getDemarcheurs(int numeroDeLaPage, int elementsParPage);

    public Demarcheur findById(Long id);

    public void deleteById(Long id);

    public int countDemarcheurs();
}
