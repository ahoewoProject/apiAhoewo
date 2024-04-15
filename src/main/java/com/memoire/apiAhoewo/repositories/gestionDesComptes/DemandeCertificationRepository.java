package com.memoire.apiAhoewo.repositories.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.DemandeCertification;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeCertificationRepository extends JpaRepository<DemandeCertification, Long> {
    public List<DemandeCertification> findByPersonne(Personne personne);

    public List<DemandeCertification> findByStatutDemande(Integer statutDemande);
}
