package com.memoire.apiAhoewo.services.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesPaiements.ComptePaiement;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface ComptePaiementService {
    Page<ComptePaiement> getComptesPaiements(Principal principal, int numeroDeLaPage, int elementsParPage);

    List<ComptePaiement> findByAgence(AgenceImmobiliere agenceImmobiliere);

    List<ComptePaiement> findByPersonne(Personne personne);

    ComptePaiement findByTypeAndPersonneAndEtat(String type, Personne personne, Boolean etat);

    ComptePaiement findByTypeAndAgenceAndEtat(String type, AgenceImmobiliere agenceImmobiliere, Boolean etat);

    ComptePaiement findById(Long comptePaiementId);

    ComptePaiement save(ComptePaiement comptePaiement, Principal principal);

    ComptePaiement update(ComptePaiement comptePaiement, Principal principal);

    void activerComptePaiementByPersonne(Principal principal, Long comptePaiementId);

    void activerComptePaiementByAgence(Long comptePaiementId, Long agenceId);

    void desactiverComptePaiementByPersonne(Principal principal, Long comptePaiementId);

    void desactiverComptePaiementByAgence(Long comptePaiementId, Long agenceId);
}
