package com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface BienImmobilierService {
    public List<BienImmobilier> getBiensImmobiliers(Principal principal);

    public List<BienImmobilier> getBiensSupports(Principal principal);

    Page<BienImmobilier> getBiensSupportsPagines(Principal principal, int numeroDeLaPage, int elementsParPage);

    public List<BienImmobilier> getBiensByProprietaire(Principal principal);

    public List<BienImmobilier> getBiensPropresAndBiensDelegues(Principal principal);

    public BienImmobilier findById(Long id);

    public BienImmobilier findByCodeBien(String codeBien);

    public BienImmobilier save(BienImmobilier bienImmobilier, Principal principal);

    public BienImmobilier update(BienImmobilier bienImmobilier, Principal principal);

    public BienImmobilier setBienImmobilier(BienImmobilier bienImmobilier);

    public boolean existsByCodeBien(String codeBien);

    public void activerBienImmobilier(Long id);

    public void desactiverBienImmobilier(Long id);

    public void deleteById(Long id);
}