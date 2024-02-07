package com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelegationGestionRepository extends JpaRepository<DelegationGestion, Long> {
    Page<DelegationGestion> findAllByBienImmobilier_PersonneOrderByCreerLeDesc(Personne personne, Pageable pageable);

    Page<DelegationGestion> findAllByGestionnaireOrderByCreerLeDesc(Personne personne, Pageable pageable);

    Page<DelegationGestion> findAllByAgenceImmobiliereInOrderByCreerLeDesc(List<AgenceImmobiliere> agenceImmobiliereList, Pageable pageable);

    List<DelegationGestion> findByBienImmobilier_Personne(Personne personne);

    List<DelegationGestion> findByGestionnaire(Personne personne);

    List<DelegationGestion> findByAgenceImmobiliereIn(List<AgenceImmobiliere> agenceImmobiliereList);

    List<DelegationGestion> findByBienImmobilier(BienImmobilier bienImmobilier);

    boolean existsByBienImmobilierAndAgenceImmobiliere(BienImmobilier bienImmobilier, AgenceImmobiliere agenceImmobiliere);

    boolean existsByBienImmobilierAndGestionnaire(BienImmobilier bienImmobilier, Personne personne);

    boolean existsByBienImmobilierAndStatutDelegationAndEtatDelegation(BienImmobilier bienImmobilier, Integer statutDelegation, Boolean etatDelegation);
}
