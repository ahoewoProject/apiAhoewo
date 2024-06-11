package com.memoire.apiAhoewo.repositories;

import com.memoire.apiAhoewo.models.Motif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotifRepository extends JpaRepository<Motif, Long> {
    Motif findByCode(String code);

    List<Motif> findByCodeAndCreerPar(String code, Long creerPar);
}
