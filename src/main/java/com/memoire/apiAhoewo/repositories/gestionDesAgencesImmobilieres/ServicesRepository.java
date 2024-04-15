package com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {
    Page<Services> findAllByEtatInOrderByCreerLeDesc(Pageable pageable, List<Integer> etats);

    Services findByNomService(String nomService);

    List<Services> findByEtat(Integer etat);
}
