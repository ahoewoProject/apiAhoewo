package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface BienImmobilierService {
    public List<BienImmobilier> getAll();

    Page<BienImmobilier> getAllByProprietairePagines(Principal principal, int numeroDeLaPage, int elementsParPage);

    Page<BienImmobilier> getBiensOfAgencesByResponsablePagines(Principal principal, int numeroDeLaPage, int elementsParPage);

    Page<BienImmobilier> getBiensOfAgencesByAgentPagines(Principal principal, int numeroDeLaPage, int elementsParPage);

    public List<BienImmobilier> getAllByProprietaire(Principal principal);

    public List<BienImmobilier> getBiensOfAgencesByResponsable(Principal principal);

    public List<BienImmobilier> getBiensOfAgencesByAgent(Principal principal);

    public BienImmobilier findById(Long id);

    public BienImmobilier save(BienImmobilier bienImmobilier, Principal principal);

    public BienImmobilier update(BienImmobilier bienImmobilier, Principal principal);

    public void activerBienImmobilier(Long id);

    public void desactiverBienImmobilier(Long id);

    public void deleteById(Long id);
}