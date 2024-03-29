package com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenceImmobiliereRepository extends JpaRepository<AgenceImmobiliere, Long> {
    AgenceImmobiliere findByNomAgence(String nomAgence);

    Page<AgenceImmobiliere> findByIdInOrderByCreerLeDesc(List<Long> idsAgences, Pageable pageable);

    AgenceImmobiliere findByCodeAgence(String codeAgence);

    boolean existsByCodeAgence(String codeAgence);
}
