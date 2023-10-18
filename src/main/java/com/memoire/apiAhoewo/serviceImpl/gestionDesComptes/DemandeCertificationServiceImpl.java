package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.DemandeCertification;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesComptes.DemandeCertificationRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.PersonneRepository;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.gestionDesComptes.DemandeCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class DemandeCertificationServiceImpl implements DemandeCertificationService {

    @Autowired
    private DemandeCertificationRepository demandeCertificationRepository;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public List<DemandeCertification> getAll() {
        return demandeCertificationRepository.findAll();
    }

    @Override
    public DemandeCertification findById(Long id) {
        return demandeCertificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<DemandeCertification> getByUser(Principal principal) {
        Personne personne = personneRepository.findByUsername(principal.getName());
        return demandeCertificationRepository.findByPersonne(personne);
    }

    @Override
    public DemandeCertification save(DemandeCertification demandeCertification, Principal principal) {
        Personne personne = personneRepository.findByUsername(principal.getName());
        demandeCertification.setDateDemande(new Date());
        demandeCertification.setStatutDemande(0);
        demandeCertification.setCreerLe(new Date());
        demandeCertification.setCreerPar(personne.getId());
        demandeCertification.setStatut(true);
        return demandeCertificationRepository.save(demandeCertification);
    }

    @Override
    public void certifierCompte(Long idPersonne, Long idDemandeCertif) {
        DemandeCertification demandeCertification = demandeCertificationRepository.findById(idDemandeCertif).orElse(null);
        demandeCertification.setStatutDemande(1);
        Personne personne = personneRepository.findById(idPersonne).orElse(null);
        personne.setEstCertifie(true);
        demandeCertificationRepository.save(demandeCertification);
        personneRepository.save(personne);
        String contenu = "Bonjour M/Mlle " + personne.getPrenom() + ",\n\n" +
                "Nous vous informons que votre compte a été certifié conformément à votre demande de certification.\n" +
                "\n\n" +
                "Cordialement,\n" +
                "\nL'équipe de support technique - ahoewo !";
        emailSenderService.sendMail(personne.getEmail(), "Certification de compte", contenu);
    }

    @Override
    public int countDemandeCertifications() {
        return (int) demandeCertificationRepository.count();
    }

    @Override
    public int countDemandeCertifValidees() {
        List<DemandeCertification> demandeCertificationList = demandeCertificationRepository.findByStatutDemande(1);
        int count = demandeCertificationList.size();
        return count;
    }

    @Override
    public int countDemandeCertifEnAttente() {
        List<DemandeCertification> demandeCertificationList = demandeCertificationRepository.findByStatutDemande(0);
        int count = demandeCertificationList.size();
        return count;
    }

}
