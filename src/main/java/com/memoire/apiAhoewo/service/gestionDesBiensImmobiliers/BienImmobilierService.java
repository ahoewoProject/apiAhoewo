package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface BienImmobilierService {

    public List<BienImmobilier> getAll();

    public List<BienImmobilier> getAllByProprietaire(Principal principal);

    public List<BienImmobilier> findBiensImmobiliersByAgentImmobilier(Principal principal);

    public List<BienImmobilier> getAllByGerant(Principal principal);

    public BienImmobilier findById(Long id);

    public BienImmobilier save(BienImmobilier bienImmobilier, Principal principal);

    public BienImmobilier update(BienImmobilier bienImmobilier, Principal principal);

    public void activerBienImmobilier(Long id);

    public void desactiverBienImmobilier(Long id);

    public void deleteById(Long id);

    public int countBienImmobilierByProprietaire(Principal principal);

    public int countBienImmobilierByAgentImmobilier(Principal principal);

    public int countBienImmobilierByGerant(Principal principal);
}