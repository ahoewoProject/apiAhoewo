package com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.ServicesAgenceImmobiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicesAgenceImmobiliereRepository extends JpaRepository<ServicesAgenceImmobiliere, Long> {
    List<ServicesAgenceImmobiliere> findByAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere);
}
