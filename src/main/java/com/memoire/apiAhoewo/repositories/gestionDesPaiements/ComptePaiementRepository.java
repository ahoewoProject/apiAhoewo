package com.memoire.apiAhoewo.repositories.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesPaiements.ComptePaiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComptePaiementRepository extends JpaRepository<ComptePaiement, Long> {
    Page<ComptePaiement> findAllByAgenceImmobiliereInOrderByIdDesc(List<AgenceImmobiliere> agenceImmobiliereList, Pageable pageable);

    List<ComptePaiement> findByAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere);

    List<ComptePaiement> findByPersonne(Personne personne);

    Page<ComptePaiement> findAllByPersonneOrderByIdDesc(Personne personne, Pageable pageable);

    ComptePaiement findByTypeAndPersonneAndEtatComptePaiement(String type, Personne personne, Boolean etatComptePaiement);

    ComptePaiement findByTypeAndAgenceImmobiliereAndEtatComptePaiement(String type, AgenceImmobiliere agenceImmobiliere,
                                                                          Boolean etatComptePaiement);
}
