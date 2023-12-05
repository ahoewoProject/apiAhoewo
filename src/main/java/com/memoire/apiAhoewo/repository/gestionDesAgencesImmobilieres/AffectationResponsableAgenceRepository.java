package com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffectationResponsableAgenceRepository extends JpaRepository<AffectationResponsableAgence, Long> {
    List<AffectationResponsableAgence> findByResponsableAgenceImmobiliere(ResponsableAgenceImmobiliere responsableAgenceImmobiliere);

    List<AffectationResponsableAgence> findByAgenceImmobiliereIn(List<AgenceImmobiliere> agenceImmobiliereList);

    List<AffectationResponsableAgence> findByAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere);
}
