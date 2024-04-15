package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Notaire;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface NotaireService {
    public List<Notaire> getAll();

    Page<Notaire> getNotaires(int numeroDeLaPage, int elementsParPage);

    public Notaire findById(Long id);

    public Notaire save(Notaire notaire, Principal principal);

    public void deleteById(Long id);

    public int countNotaires();
}
