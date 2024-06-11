package com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.SuiviEntretien;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface SuiviEntretienService {
    Page<SuiviEntretien> getSuivisEntretiens(Principal principal, int numeroDeLaPage, int elementsParPage);

    Page<SuiviEntretien> getSuivisEntretiensByCodeContratLocation(String codeContrat, int numeroDeLaPage, int elementsParPage);

    List<SuiviEntretien> getSuivisEntretiens(Principal principal);

    SuiviEntretien findById(Long id);

    SuiviEntretien save(SuiviEntretien suiviEntretien, Principal principal);

    SuiviEntretien update(SuiviEntretien suiviEntretien, Principal principal);

    void debuterEntretien(Long id, Principal principal);

    void terminerEntretien(Long id, Principal principal);
}
