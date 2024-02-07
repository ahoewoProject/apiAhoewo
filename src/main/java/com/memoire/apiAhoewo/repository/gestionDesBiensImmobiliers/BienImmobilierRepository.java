package com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.TypeDeBien;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
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
