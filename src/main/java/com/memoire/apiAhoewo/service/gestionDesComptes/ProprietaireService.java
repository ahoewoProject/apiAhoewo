package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Proprietaire;

import java.util.List;

public interface ProprietaireService {
    public List<Proprietaire> getAll();

    public Proprietaire findById(Long id);

    public void deleteById(Long id);

    public int countProprietaires();
}
