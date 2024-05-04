package com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratVente;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.DemandeAchat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratVenteRepository extends JpaRepository<ContratVente, Long> {
    Page<ContratVente> findByDemandeAchatInOrderByIdDesc(List<DemandeAchat> demandeAchats, Pageable pageable);

    List<ContratVente> findByBienImmobilier_CodeBienOrderByIdDesc(String codeBien);

    List<ContratVente> findByDemandeAchatIn(List<DemandeAchat> demandeAchatList);

    List<ContratVente> findByEtatContrat(String etatContrat);

    boolean existsByDemandeAchat(DemandeAchat demandeAchat);

    boolean existsByBienImmobilierAndEtatContrat(BienImmobilier bienImmobilier, String etatContrat);
}
