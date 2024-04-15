package com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.TypeDeBien;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BienImmobilierRepository extends JpaRepository<BienImmobilier, Long> {
    Page<BienImmobilier> findAllByPersonneAndTypeDeBien_DesignationInOrderByCreerLeDesc(Personne personne, Pageable pageable, List<String> designations);

    Page<BienImmobilier> findAllByAgenceImmobiliereInAndTypeDeBien_DesignationInOrderByCreerLeDesc(List<AgenceImmobiliere> agenceImmobilieres, Pageable pageable, List<String> designations);

    List<BienImmobilier> findByPersonne(Personne personne);

    List<BienImmobilier> findByTypeDeBien(TypeDeBien typeDeBien);

    List<BienImmobilier> findByAgenceImmobiliereIn(List<AgenceImmobiliere> agenceImmobilieres);

    BienImmobilier findByCodeBien(String codeBien);

    boolean existsByCodeBien(String codeBien);
}
