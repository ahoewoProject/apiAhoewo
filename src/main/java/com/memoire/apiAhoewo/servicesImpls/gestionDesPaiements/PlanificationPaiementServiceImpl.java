package com.memoire.apiAhoewo.servicesImpls.gestionDesPaiements;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.Contrat;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratVente;
import com.memoire.apiAhoewo.models.gestionDesPaiements.PlanificationPaiement;
import com.memoire.apiAhoewo.repositories.gestionDesPaiements.PlanificationPaiementRepository;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.ContratLocationService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.ContratVenteService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.PlanificationPaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PlanificationPaiementServiceImpl implements PlanificationPaiementService {
    @Autowired
    private PlanificationPaiementRepository planificationPaiementRepository;
    @Autowired
    private ContratLocationService contratLocationService;
    @Autowired
    private ContratVenteService contratVenteService;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public Page<PlanificationPaiement> getPlanificationsPaiement(Principal principal, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);

        List<Contrat> contratList = new ArrayList<>();

        List<ContratLocation> contratLocationList = contratLocationService.getContratLocations(principal);
        List<ContratVente> contratVenteList = contratVenteService.getContratVentes(principal);

        contratList.addAll(contratLocationList);
        contratList.addAll(contratVenteList);

        return planificationPaiementRepository.findByContratInOrderByIdDesc(contratList, pageRequest);
    }

    @Override
    public List<PlanificationPaiement> getPlanificationsPaiement(Principal principal) {
        List<Contrat> contratList = new ArrayList<>();

        List<ContratLocation> contratLocationList = contratLocationService.getContratLocations(principal);
        List<ContratVente> contratVenteList = contratVenteService.getContratVentes(principal);

        contratList.addAll(contratLocationList);
        contratList.addAll(contratVenteList);

        return planificationPaiementRepository.findByContratIn(contratList);
    }

    @Override
    public PlanificationPaiement findById(Long id) {
        return planificationPaiementRepository.findById(id).orElse(null);
    }

    @Override
    public PlanificationPaiement savePlanificationPaiementLocation(Principal principal, PlanificationPaiement planificationPaiement) {
        Personne personne = personneService.findByUsername(principal.getName());

        planificationPaiement.setCodePlanification("PLNPAI" + UUID.randomUUID());
        planificationPaiement.setCreerLe(new Date());
        planificationPaiement.setCreerPar(personne.getId());
        planificationPaiement.setStatutPlanification("En attente");
        planificationPaiement.setStatut(true);

        planificationPaiement = planificationPaiementRepository.save(planificationPaiement);

        Notification notification = new Notification();
        notification.setTitre("Nouvelle planification de paiement");
        notification.setMessage("Vous avez une reçu une nouvelle planification de paiement de loyer pour le contrat de location " + planificationPaiement.getContrat().getCodeContrat());
        notification.setSendTo(String.valueOf(planificationPaiement.getContrat().getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/planifications-paiements/" + planificationPaiement.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (planificationPaiement.getContrat().getBienImmobilier().getEstDelegue()) {
            if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                    personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {
                Notification notification2 = new Notification();
                notification2.setTitre("Nouvelle planification de paiement");
                notification2.setMessage("Une nouvelle planification de paiement de loyer a été faite sur le contrat de location " + planificationPaiement.getContrat().getCodeContrat() + " du bien " + planificationPaiement.getContrat().getBienImmobilier().getCodeBien() + " que vous avez délégué");
                notification2.setSendTo(String.valueOf(planificationPaiement.getContrat().getBienImmobilier().getPersonne().getId()));
                notification2.setDateNotification(new Date());
                notification2.setLu(false);
                notification2.setUrl("/planifications-paiements/" + planificationPaiement.getId());
                notification2.setCreerPar(personne.getId());
                notification2.setCreerLe(new Date());
                notificationService.save(notification2);
            }
        }

        planificationPaiement.setCodePlanification("PLNPAI00" + planificationPaiement.getId());

        return planificationPaiementRepository.save(planificationPaiement);
    }

    @Override
    public PlanificationPaiement savePlanificationPaiementAchat(Principal principal, PlanificationPaiement planificationPaiement) {
        Personne personne = personneService.findByUsername(principal.getName());

        planificationPaiement.setCodePlanification("PLNPAI" + UUID.randomUUID());
        planificationPaiement.setCreerLe(new Date());
        planificationPaiement.setCreerPar(personne.getId());
        planificationPaiement.setStatutPlanification("En attente");
        planificationPaiement.setStatut(true);

        planificationPaiement = planificationPaiementRepository.save(planificationPaiement);

        Notification notification = new Notification();
        notification.setTitre("Nouvelle planification de paiement");
        notification.setMessage("Vous avez une reçu une nouvelle planification de paiement d'achat pour le contrat de vente " + planificationPaiement.getContrat().getCodeContrat());
        notification.setSendTo(String.valueOf(planificationPaiement.getContrat().getClient().getId()));
        notification.setDateNotification(new Date());
        notification.setLu(false);
        notification.setUrl("/planifications-paiements/" + planificationPaiement.getId());
        notification.setCreerPar(personne.getId());
        notification.setCreerLe(new Date());
        notificationService.save(notification);

        if (planificationPaiement.getContrat().getBienImmobilier().getEstDelegue()) {
            if (personne.getRole().getCode().equals("ROLE_RESPONSABLE") || personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER") ||
                    personne.getRole().getCode().equals("ROLE_DEMARCHEUR") || personne.getRole().getCode().equals("ROLE_GERANT")) {
                Notification notification2 = new Notification();
                notification2.setTitre("Nouvelle planification de paiement");
                notification2.setMessage("Une nouvelle planification de paiement d'achat a été faite sur le contrat de vente " + planificationPaiement.getContrat().getCodeContrat() + " du bien " + planificationPaiement.getContrat().getBienImmobilier().getCodeBien() + " que vous avez délégué");
                notification2.setSendTo(String.valueOf(planificationPaiement.getContrat().getBienImmobilier().getPersonne().getId()));
                notification2.setDateNotification(new Date());
                notification2.setLu(false);
                notification2.setUrl("/planifications-paiements/" + planificationPaiement.getId());
                notification2.setCreerPar(personne.getId());
                notification2.setCreerLe(new Date());
                notificationService.save(notification2);
            }
        }

        planificationPaiement.setCodePlanification("PLNPAI00" + planificationPaiement.getId());

        return planificationPaiementRepository.save(planificationPaiement);
    }

    @Override
    public PlanificationPaiement dernierePlanificationPaiementAchat(String codeContrat) {
        return planificationPaiementRepository.findByContrat_CodeContratOrderByCreerLeDesc(codeContrat);
    }

    @Override
    public void setStatutPlanification(PlanificationPaiement planificationPaiement) {
        planificationPaiementRepository.save(planificationPaiement);
    }

    @Override
    public boolean existsByContratAndDatePlanifiee(Contrat contrat, Date datePlanifiee) {
        return planificationPaiementRepository.existsByContratAndDatePlanifiee(contrat, datePlanifiee);
    }

}
