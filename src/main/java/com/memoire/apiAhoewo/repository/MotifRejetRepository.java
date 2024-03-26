package com.memoire.apiAhoewo.repository;

import com.memoire.apiAhoewo.model.MotifRejet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotifRejetRepository extends JpaRepository<MotifRejet, Long> {
    MotifRejet findByCode(String code);

    List<MotifRejet> findByCodeAndCreerPar(String code, Long creerPar);
}
