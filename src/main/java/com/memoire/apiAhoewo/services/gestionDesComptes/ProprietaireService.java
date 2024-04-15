package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Proprietaire;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProprietaireService {
    public List<Proprietaire> getAll();

    Page<Proprietaire> getProprietaires(int numeroDeLaPage, int elementsParPage);

    public Proprietaire findById(Long id);

    public void deleteById(Long id);

    public int countProprietaires();
}
