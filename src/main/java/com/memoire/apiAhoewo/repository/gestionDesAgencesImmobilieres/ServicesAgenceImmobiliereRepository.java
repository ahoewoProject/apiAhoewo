package com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.Services;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicesAgenceImmobiliereRepository extends JpaRepository<ServicesAgenceImmobiliere, Long> {
    Page<ServicesAgenceImmobiliere> findByEtatInOrderByCreerLeDesc(Pageable pageable, List<Integer> etatList);

    Page<ServicesAgenceImmobiliere> findAllByAgenceImmobiliereInAndEtatInOrderByCreerLeDesc(List<AgenceImmobiliere> agenceImmobiliereList, Pageable pageable, List<Integer> etatList);

    Page<ServicesAgenceImmobiliere> findAllByAgenceImmobiliereAndEtatInOrderByCreerLeDesc(AgenceImmobiliere agenceImmobiliere, Pageable pageable, List<Integer> etatList);

    List<ServicesAgenceImmobiliere> findByAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere);

    ServicesAgenceImmobiliere findByServices(Services services);

    boolean existsByServicesAndAgenceImmobiliere(Services services, AgenceImmobiliere agenceImmobiliere);
}
