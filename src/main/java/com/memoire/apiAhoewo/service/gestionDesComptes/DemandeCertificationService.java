package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.DemandeCertification;

import java.security.Principal;
import java.util.List;
public interface DemandeCertificationService {

    public List<DemandeCertification> getAll();
    public DemandeCertification findById(Long id);
    public List<DemandeCertification> getByUser(Principal principal);
    public DemandeCertification save(DemandeCertification demandeCertification, Principal principal);
    public void certifierCompte(Long id);
}
