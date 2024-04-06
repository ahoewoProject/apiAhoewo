package com.memoire.apiAhoewo.repository.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.ContratVente;
import com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes.DemandeAchat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratVenteRepository extends JpaRepository<ContratVente, Long> {
    Page<ContratVente> findByDemandeAchatInOrderByIdDesc(List<DemandeAchat> demandeAchats, Pageable pageable);

    boolean existsByDemandeAchat(DemandeAchat demandeAchat);

    boolean existsByBienImmobilierAndEtatContrat(BienImmobilier bienImmobilier, String etatContrat);
}
