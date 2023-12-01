package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface AgenceImmobiliereService {

    public List<AgenceImmobiliere> getAgencesByResponsable(Principal principal);

    public List<AgenceImmobiliere> getAgencesByAgent(Principal principal);

    public AgenceImmobiliere findById(Long id);

    public AgenceImmobiliere findByNomAgence(String nomAgence);

    public AgenceImmobiliere save(AgenceImmobiliere agenceImmobiliere, Principal principal);

    public AgenceImmobiliere update(AgenceImmobiliere agenceImmobiliere, Principal principal);

    public void activerAgence(Long id);

    public void desactiverAgence(Long id);

    public String enregistrerLogo(MultipartFile file);

    public String construireCheminFichier(AgenceImmobiliere agenceImmobiliere);
}
