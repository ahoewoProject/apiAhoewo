package com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenceImmobiliereRepository extends JpaRepository<AgenceImmobiliere, Long> {
    AgenceImmobiliere findByNomAgence(String nomAgence);

    Page<AgenceImmobiliere> findAllByOrderByIdDesc(Pageable pageable);

    Page<AgenceImmobiliere> findByEtatAgenceOrderByIdDesc(boolean etatAgence, Pageable pageable);

    Page<AgenceImmobiliere> findByQuartier_IdAndEtatAgenceOrderByIdDesc(Long idQuartier, boolean etatAgence, Pageable pageable);

    Page<AgenceImmobiliere> findByQuartier_Ville_IdAndEtatAgenceOrderByIdDesc(Long idVille, boolean etatAgence, Pageable pageable);

    Page<AgenceImmobiliere> findByQuartier_Ville_Region_IdAndEtatAgenceOrderByIdDesc(Long idRegion, boolean etatAgence, Pageable pageable);

    Page<AgenceImmobiliere> findByIdInOrderByCreerLeDesc(List<Long> idsAgences, Pageable pageable);

    AgenceImmobiliere findByCodeAgence(String codeAgence);

    boolean existsByCodeAgence(String codeAgence);
}
