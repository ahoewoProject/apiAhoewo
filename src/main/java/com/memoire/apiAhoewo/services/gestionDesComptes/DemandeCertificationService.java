package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.DemandeCertification;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
public interface DemandeCertificationService {
    public Page<DemandeCertification> getDemandesCertifications(int numeroDeLaPage, int elementsParPage, Principal principal);

    public DemandeCertification findById(Long id);

    public List<DemandeCertification> getByUser(Principal principal);

    public DemandeCertification saveDemandeCertificationCompte(DemandeCertification demandeCertification, Principal principal);

    public DemandeCertification saveDemandeCertificationAgence(DemandeCertification demandeCertification, Principal principal);

    public void certifierCompte(Long idPersonne, Long idDemandeCertif);

    public void certifierAgence(Long idAgence, Long idDemandeCertif);

    public int countDemandeCertifications();

    public int countDemandeCertifValidees();

    public int countDemandeCertifEnAttente();

    public String enregistrerDocumentJustificatif(MultipartFile file);

    public String construireCheminFichier(String nomFichier);
}
