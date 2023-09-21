package com.memoire.apiAhoewo.repository.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Proprietaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long> {

}
