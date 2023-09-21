package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Demarcheur;

import java.util.List;

public interface DemarcheurService {
    public List<Demarcheur> getAll();

    public Demarcheur findById(Long id);

    public void deleteById(Long id);

    public int countDemarcheurs();
}
