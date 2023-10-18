package com.memoire.apiAhoewo.repository.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {
    Personne findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Personne findByEmail(String email);
    Personne findByResetToken(String token);
}
