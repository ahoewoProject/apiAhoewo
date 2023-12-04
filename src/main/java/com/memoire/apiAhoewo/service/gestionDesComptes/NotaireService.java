package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Notaire;

import java.security.Principal;
import java.util.List;

public interface NotaireService {
    public List<Notaire> getAll();
    public Notaire findById(Long id);
    public Notaire save(Notaire notaire, Principal principal);
    public void deleteById(Long id);
    public int countNotaires();
}
