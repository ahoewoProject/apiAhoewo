package com.memoire.apiAhoewo.repository.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Client;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.DemandeLocation;
import com.memoire.apiAhoewo.model.gestionDesPublications.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeLocationRepository extends JpaRepository<DemandeLocation, Long> {
    Page<DemandeLocation> findByClientOrderByIdDesc(Client client, Pageable pageable);

    Page<DemandeLocation> findByPublicationInOrderByIdDesc(List<Publication> publicationList, Pageable pageable);

    List<DemandeLocation> findByPublicationInOrderByIdDesc(List<Publication> publicationList);

    List<DemandeLocation> findByClientOrderByIdDesc(Client client);

    boolean existsByClientAndPublication(Client client, Publication publication);
}
