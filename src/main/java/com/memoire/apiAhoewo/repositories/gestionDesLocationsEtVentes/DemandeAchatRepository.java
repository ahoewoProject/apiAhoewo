package com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeAchat;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeAchatRepository extends JpaRepository<DemandeAchat, Long> {
    Page<DemandeAchat> findByClientOrderByIdDesc(Client client, Pageable pageable);

    Page<DemandeAchat> findByPublicationInOrderByIdDesc(List<Publication> publicationList, Pageable pageable);

    List<DemandeAchat> findByPublicationInOrderByIdDesc(List<Publication> publicationList);

    List<DemandeAchat> findByClientOrderByIdDesc(Client client);

    List<DemandeAchat> findByEtatDemande(Integer etatDemande);

    boolean existsByClientAndPublication(Client client, Publication publication);
}
