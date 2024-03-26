package com.memoire.apiAhoewo.repository.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Client;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.DemandeAchat;
import com.memoire.apiAhoewo.model.gestionDesPublications.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeAchatRepository extends JpaRepository<DemandeAchat, Long> {
    Page<DemandeAchat> findByClientOrderByIdDesc(Client client, Pageable pageable);

    Page<DemandeAchat> findByPublicationInOrderByIdDesc(List<Publication> publicationList, Pageable pageable);

    boolean existsByClientAndPublication(Client client, Publication publication);
}
