package com.memoire.apiAhoewo.repository.gestionDesPublications;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Region;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.TypeDeBien;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesPublications.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    Page<Publication> findByEtatOrderByIdDesc(Boolean etatPublication, Pageable pageable);

    Page<Publication> findByAgenceImmobiliereAndEtatOrderByIdDesc(AgenceImmobiliere agenceImmobiliere, Boolean etatPublication,
                                                                 Pageable pageable);

    Page<Publication> findByPersonneAndEtatOrderByIdDesc(Personne personne, Boolean etatPublication, Pageable pageable);

    Page<Publication> findByBienImmobilier_Quartier_Ville_RegionInAndEtatOrderByIdDesc(List<Region> regions, Boolean etatPublication,
                                                                                       Pageable pageable);

    Page<Publication> findByBienImmobilier_Quartier_Ville_RegionAndEtatOrderByIdDesc(Region region, Boolean etatPublication,
                                                                                       Pageable pageable);

    Page<Publication> findByBienImmobilier_TypeDeBienInAndEtatOrderByIdDesc(List<TypeDeBien> typeDeBiens, Boolean etatPublication,
                                                                            Pageable pageable);

    Page<Publication> findByBienImmobilier_TypeDeBienAndEtatOrderByIdDesc(TypeDeBien typeDeBien, Boolean etatPublication,
                                                                            Pageable pageable);

    Page<Publication> findByTypeDeTransactionAndEtatOrderByIdDesc(String typeDeTransaction, Boolean etatPublication,
                                                                  Pageable pageable);

    Publication findByCodePublication(String codePublication);

    Page<Publication> findByBienImmobilierInOrderByIdDesc(List<BienImmobilier> bienImmobilierList, Pageable pageable);

    List<Publication> findByBienImmobilierInOrderByIdDesc(List<BienImmobilier> bienImmobilierList);

    boolean existsByBienImmobilierAndEtat(BienImmobilier bienImmobilier, Boolean etat);
}
