package com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.ImagesBienImmobilier;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface ImagesBienImmobilierService {
    public List<ImagesBienImmobilier> getAll();

    public List<ImagesBienImmobilier> findByBienImmobilier(BienImmobilier bienImmobilier);

    public ImagesBienImmobilier findById(Long id);

    public ImagesBienImmobilier save(ImagesBienImmobilier imagesBienImmobilier, Principal principal);

    public ImagesBienImmobilier update(ImagesBienImmobilier imagesBienImmobilier, Principal principal);

    public String enregistrerImageDuBien(MultipartFile file);

    public String construireCheminFichier(ImagesBienImmobilier imagesBienImmobilier);
}
