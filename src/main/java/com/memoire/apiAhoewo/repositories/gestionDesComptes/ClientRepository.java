package com.memoire.apiAhoewo.repositories.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findAllByOrderByCreerLeDesc(Pageable pageable);
}