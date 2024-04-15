package com.memoire.apiAhoewo.repositories.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.ResponsableAgenceImmobiliere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffectationResponsableAgenceRepository extends JpaRepository<AffectationResponsableAgence, Long> {
    Page<AffectationResponsableAgence> findAllByOrderByCreerLeDesc(Pageable pageable);

    Page<AffectationResponsableAgence> findAllByAgenceImmobiliereInOrderByCreerLeDesc(List<AgenceImmobiliere> agenceImmobiliereList, Pageable pageable);

    List<AffectationResponsableAgence> findByResponsableAgenceImmobiliere(ResponsableAgenceImmobiliere responsableAgenceImmobiliere);

    List<AffectationResponsableAgence> findByAgenceImmobiliereIn(List<AgenceImmobiliere> agenceImmobiliereList);

    List<AffectationResponsableAgence> findByAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere);

    boolean existsByAgenceImmobiliereAndResponsableAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere, ResponsableAgenceImmobiliere responsableAgenceImmobiliere);

    boolean existsByAgenceImmobiliereAndResponsableAgenceImmobiliere_Matricule(AgenceImmobiliere agenceImmobiliere, String matricule);
}
