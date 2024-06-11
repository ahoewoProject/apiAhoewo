package com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface AgenceImmobiliereService {
    public Page<AgenceImmobiliere> getAgencesActives(int numeroDeLaPage, int elementsParPage);

    public Page<AgenceImmobiliere> getAgencesPages(Principal principal, int numeroDeLaPage, int elementsParPage);

    public Page<AgenceImmobiliere> getAgencesActivesByRegionId(Long id, int numeroDeLaPage, int elementsParPage);

    public Page<AgenceImmobiliere> getAgencesActivesByVilleId(Long id, int numeroDeLaPage, int elementsParPage);

    public Page<AgenceImmobiliere> getAgencesActivesByQuartierId(Long id, int numeroDeLaPage, int elementsParPage);

    List<AgenceImmobiliere> getAll();

    List<AgenceImmobiliere> getAgencesImmobilieresListIfUserActif(Principal principal);

    List<AgenceImmobiliere> getAgencesImmobilieresList(Principal principal);

    public AgenceImmobiliere findById(Long id);

    public AgenceImmobiliere findByNomAgence(String nomAgence);

    public AgenceImmobiliere findByCodeAgence(String codeAgence);

    public AgenceImmobiliere save(AgenceImmobiliere agenceImmobiliere, Principal principal);

    public AgenceImmobiliere update(AgenceImmobiliere agenceImmobiliere, Principal principal);

    public void activerAgence(Long id);

    public void desactiverAgence(Long id);

    boolean codeAgenceExists(String codeAgence);

    public String enregistrerLogo(MultipartFile file);

    public String construireCheminFichier(AgenceImmobiliere agenceImmobiliere);
}
