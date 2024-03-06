package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface BienImmobilierService {
    public List<BienImmobilier> getAll();

    Page<BienImmobilier> getBiensPaginesByProprietaire(Principal principal, int numeroDeLaPage, int elementsParPage);

    Page<BienImmobilier> getBiensPaginesOfAgencesByResponsable(Principal principal, int numeroDeLaPage, int elementsParPage);

    Page<BienImmobilier> getBiensPaginesOfAgencesByAgent(Principal principal, int numeroDeLaPage, int elementsParPage);

    public List<BienImmobilier> getBiensByProprietaire(Principal principal);

    public List<BienImmobilier> getBiensOfAgencesByResponsable(Principal principal);

    public List<BienImmobilier> getBiensOfAgencesByAgent(Principal principal);

    public List<BienImmobilier> getBiensPropresAndBiensDelegues(Principal principal);

    public BienImmobilier findById(Long id);

    public BienImmobilier findByCodeBien(String codeBien);

    public BienImmobilier save(BienImmobilier bienImmobilier, Principal principal);

    public BienImmobilier update(BienImmobilier bienImmobilier, Principal principal);

    public boolean existsByCodeBien(String codeBien);

    public void activerBienImmobilier(Long id);

    public void desactiverBienImmobilier(Long id);

    public void deleteById(Long id);
}