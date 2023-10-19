package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.DemandeCertification;

import java.security.Principal;
import java.util.List;
public interface DemandeCertificationService {

    public List<DemandeCertification> getAll();
    public DemandeCertification findById(Long id);
    public List<DemandeCertification> getByUser(Principal principal);
    public DemandeCertification saveDemandeCertificationCompte(DemandeCertification demandeCertification, Principal principal);
    public DemandeCertification saveDemandeCertificationAgence(DemandeCertification demandeCertification, Principal principal);
    public void certifierCompte(Long idPersonne, Long idDemandeCertif);
    public void certifierAgence(Long idAgence, Long idDemandeCertif);
    public int countDemandeCertifications();
    public int countDemandeCertifValidees();
    public int countDemandeCertifEnAttente();
}
