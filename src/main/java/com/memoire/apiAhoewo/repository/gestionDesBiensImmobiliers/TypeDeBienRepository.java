package com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.TypeDeBien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeDeBienRepository extends JpaRepository<TypeDeBien, Long> {
    TypeDeBien findByDesignation(String designation);
    List<TypeDeBien> findByEtat(Boolean etat);
}
