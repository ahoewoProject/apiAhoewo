package com.memoire.apiAhoewo.services.gestionDesPublications;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Quartier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Region;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.TypeDeBien;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import com.memoire.apiAhoewo.dto.RechercheAvanceePublicationForm;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface PublicationService {
    Page<Publication> rechercheSimpleDePublicationsActives(String typeDeTransaction, TypeDeBien typeDeBien,
                                                           Quartier quartier, int numeroDeLaPage, int elementsParPage);

    Page<Publication> rechercheAvanceeDePublicationsActives(RechercheAvanceePublicationForm rechercheAvancePublicationForm,
                                                            int numeroDeLaPage, int elementsParPage);

    Page<Publication> getPublicationsActives(int numeroDeLaPage, int elementsParPage);

    Page<Publication> getPublicationsActivesByTypeDeTransaction(String typeDeTransaction, int numeroDeLaPage,
                                                                int elementsParPage);

    Page<Publication> getPublicationsActivesByTypeDeBienList(int numeroDeLaPage, int elementsParPage);

    Page<Publication> getPublicationsActivesByTypeDeBien(TypeDeBien typeDeBien, int numeroDeLaPage, int elementsParPage);

    Page<Publication> getPublicationsActivesByRegionsList(int numeroDeLaPage, int elementsParPage);

    Page<Publication> getPublicationsActivesByRegion(Region region, int numeroDeLaPage, int elementsParPage);

    Page<Publication> getPublicationsActivesByAgence(AgenceImmobiliere agenceImmobiliere, int numeroDeLaPage, int elementsParPage);

    Page<Publication> getPublicationsActivesByPersonne(Personne personne, int numeroDeLaPage, int elementsParPage);

    Page<Publication> getPublications(int numeroDeLaPage, int elementsParPage, Principal principal);

    List<Publication> getPublications(Principal principal);

    Publication save(Publication publication, Principal principal);

    Publication update(Publication publication, Principal principal);

    Publication findById(Long id);

    Publication findByCodePublication(String codePublication);

    void activerPublication(Long id);

    void desactiverPublication(Long id);

    void desactiverPublicationParBienImmobilier(Long id);

    boolean existsByBienImmobilierAndEtat(BienImmobilier bienImmobilier, Boolean etat);
}
