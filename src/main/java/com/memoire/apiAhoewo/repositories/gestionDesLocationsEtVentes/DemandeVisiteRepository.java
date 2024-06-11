package com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeVisite;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeVisiteRepository extends JpaRepository<DemandeVisite, Long> {
    Page<DemandeVisite> findByClientOrderByIdDesc(Client client, Pageable pageable);

    Page<DemandeVisite> findByPublicationInOrderByIdDesc(List<Publication> publicationList, Pageable pageable);

    List<DemandeVisite> findByClientOrderByIdDesc(Client client);

    List<DemandeVisite> findByPublicationInOrderByIdDesc(List<Publication> publicationList);

    List<DemandeVisite> findByEtatDemande(Integer etatDemande);

    boolean existsByClientAndPublication(Client client, Publication publication);
}
