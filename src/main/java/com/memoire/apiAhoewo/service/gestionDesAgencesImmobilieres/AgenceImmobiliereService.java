package com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface AgenceImmobiliereService {

    public List<AgenceImmobiliere> getAll();

    public List<AgenceImmobiliere> getAllByAgentImmobilier(Principal principal);

    public AgenceImmobiliere findById(Long id);

    public AgenceImmobiliere findByNomAgence(String nomAgence);

    public AgenceImmobiliere save(AgenceImmobiliere agenceImmobiliere, Principal principal);

    public AgenceImmobiliere update(AgenceImmobiliere agenceImmobiliere, Principal principal);

    public void activerAgence(Long id);

    public void desactiverAgence(Long id);

    public void deleteById(Long id);

    public int countAgencesImmobilieres();

    public int countAgencesByAgentImmobilier(Principal principal);

    public String enregistrerLogo(MultipartFile file);

    public String construireCheminFichier(AgenceImmobiliere agenceImmobiliere);
}
