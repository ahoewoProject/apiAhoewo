package com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelegationGestionRepository extends JpaRepository<DelegationGestion, Long> {

    List<DelegationGestion> findDelegationGestionByBienImmobilier_Personne(Personne personne);

    List<DelegationGestion> findDelegationGestionByGestionnaire(Personne personne);
}
