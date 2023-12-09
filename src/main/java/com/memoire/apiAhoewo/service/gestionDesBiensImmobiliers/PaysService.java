package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Pays;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface PaysService {
    List<Pays> getAll();

    Page<Pays> getPaysPagines(int numeroDeLaPage, int elementsParPage);

    List<Pays> getPaysActifs(Boolean etat);

    Pays findById(Long id);

    Pays findByLibelle(String libelle);

    Pays save(Pays pays, Principal principal);

    Pays update(Pays pays, Principal principal);

    void activerPays(Long id);

    void desactiverPays(Long id);
}
