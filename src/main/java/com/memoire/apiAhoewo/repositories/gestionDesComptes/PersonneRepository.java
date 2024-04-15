package com.memoire.apiAhoewo.repositories.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {
    Personne findByUsername(String username);

    Personne findByMatricule(String matricule);

    boolean existsByUsername(String username);

    boolean existsByMatricule(String matricule);

    boolean existsByEmail(String email);

    Personne findByEmail(String email);

    Personne findByResetToken(String token);
}
