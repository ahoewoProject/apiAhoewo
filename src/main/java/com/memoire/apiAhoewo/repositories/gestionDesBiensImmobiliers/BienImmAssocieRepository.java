package com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmAssocie;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BienImmAssocieRepository extends JpaRepository<BienImmAssocie, Long> {
    Page<BienImmAssocie> findAllByBienImmobilierOrderByCreerLeDesc(BienImmobilier bienImmobilier, Pageable pageable);

    List<BienImmAssocie> findAllByBienImmobilier(BienImmobilier bienImmobilier);

    List<BienImmAssocie> findAllByPersonneOrderByCreerLeDesc(Personne personne);

    List<BienImmAssocie> findAllByAgenceImmobiliereInOrderByCreerLeDesc(List<AgenceImmobiliere> agenceImmobilieres);


    boolean existsByBienImmobilier(BienImmobilier bienImmobilier);
}
