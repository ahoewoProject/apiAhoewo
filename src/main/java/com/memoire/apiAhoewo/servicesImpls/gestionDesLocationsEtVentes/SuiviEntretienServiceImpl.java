package com.memoire.apiAhoewo.servicesImpls.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.Notification;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.ContratLocation;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.SuiviEntretien;
import com.memoire.apiAhoewo.repositories.gestionDesLocationsEtVentes.SuiviEntretienRepository;
import com.memoire.apiAhoewo.services.NotificationService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.ContratLocationService;
import com.memoire.apiAhoewo.services.gestionDesLocationsEtVentes.SuiviEntretienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SuiviEntretienServiceImpl implements SuiviEntretienService {
    @Autowired
    private PersonneService personneService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SuiviEntretienRepository suiviEntretienRepository;
    @Autowired
    private ContratLocationService contratLocationService;

    @Override
    public Page<SuiviEntretien> getSuivisEntretiens(Principal principal, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);

        List<ContratLocation> contratLocations = contratLocationService.getContratLocations(principal);

        return suiviEntretienRepository.findByContratLocationInOrderByIdDesc(contratLocations, pageRequest);
    }

    @Override
    public Page<SuiviEntretien> getSuivisEntretiensByCodeContratLocation(String codeContrat, int numeroDeLaPage, int elementsParPage) {

        List<SuiviEntretien> suiviEntretienList = suiviEntretienRepository.findByContratLocation_CodeContratOrderByIdDesc(codeContrat);

        List<SuiviEntretien> suiviEntretienList1 =  suiviEntretienList.stream()
                .filter(suiviEntretien -> "Terminé".equals(suiviEntretien.getEtatSuiviEntretien()))
                .collect(Collectors.toList());

        List<SuiviEntretien> suiviEntretienList2 = suiviEntretienList.stream()
                .filter(suiviEntretien -> "En cours".equals(suiviEntretien.getEtatSuiviEntretien()))
                .collect(Collectors.toList());

        List<SuiviEntretien> suiviEntretienList3 = suiviEntretienList.stream()
                .filter(suiviEntretien -> "En attente".equals(suiviEntretien.getEtatSuiviEntretien()))
                .collect(Collectors.toList());

        List<SuiviEntretien> suiviEntretienArrayList = new ArrayList<>();
        suiviEntretienArrayList.addAll(suiviEntretienList1);
        suiviEntretienArrayList.addAll(suiviEntretienList2);
        suiviEntretienArrayList.addAll(suiviEntretienList3);

        int start = numeroDeLaPage * elementsParPage;
        int end = Math.min(start + elementsParPage, suiviEntretienArrayList.size());
        List<SuiviEntretien> paginedSuiviPaiement = suiviEntretienArrayList.subList(start, end);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);

        return new PageImpl<>(paginedSuiviPaiement, pageRequest, suiviEntretienArrayList.size());
    }

    @Override
    public List<SuiviEntretien> getSuivisEntretiens(Principal principal) {
        List<ContratLocation> contratLocations = contratLocationService.getContratLocations(principal);

        return suiviEntretienRepository.findByContratLocationInOrderByIdDesc(contratLocations);
    }

    @Override
    public SuiviEntretien findById(Long id) {
        return suiviEntretienRepository.findById(id).orElse(null);
    }

    @Override
    public SuiviEntretien save(SuiviEntretien suiviEntretien, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        suiviEntretien.setCodeSuiviEntretien("SUIEN" + UUID.randomUUID());
        suiviEntretien.setCreerLe(new Date());
        suiviEntretien.setCreerPar(personne.getId());
        suiviEntretien.setStatut(true);

        LocalDate datePrevue = suiviEntretien.getDatePrevue();
        LocalDate today = LocalDate.now();

        if (datePrevue.isEqual(today) || datePrevue.isAfter(today)) {
            suiviEntretien.setEtatSuiviEntretien("En attente");
        } else {
            suiviEntretien.setEtatSuiviEntretien("Terminé");
        }

        suiviEntretien = suiviEntretienRepository.save(suiviEntretien);

        Notification notification2 = new Notification();
        notification2.setTitre("Nouvel entretien pour un contrat de location.");
        notification2.setMessage("Un nouvel entretien a été soumis pour le contrat " + suiviEntretien.getContratLocation().getCodeContrat());
        notification2.setSendTo(String.valueOf(suiviEntretien.getContratLocation().getBienImmobilier().getPersonne().getId()));
        notification2.setLu(false);
        notification2.setUrl("/suivi-entretien/" + suiviEntretien.getId());
        notification2.setDateNotification(new Date());
        notification2.setCreerLe(new Date());
        notification2.setCreerPar(personne.getId());
        notificationService.save(notification2);

        suiviEntretien.setCodeSuiviEntretien("SUIEN00" + suiviEntretien.getId());
        return suiviEntretienRepository.save(suiviEntretien);
    }

    @Override
    public SuiviEntretien update(SuiviEntretien suiviEntretien, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        LocalDate datePrevue = suiviEntretien.getDatePrevue();
        LocalDate today = LocalDate.now();

        if (datePrevue.isEqual(today) || datePrevue.isAfter(today)) {
            suiviEntretien.setEtatSuiviEntretien("En attente");
        } else {
            suiviEntretien.setEtatSuiviEntretien("Terminé");
        }

        Notification notification2 = new Notification();
        notification2.setTitre("Nouvel entretien pour un contrat de location.");
        notification2.setMessage("Un nouvel entretien a été soumis pour le contrat " + suiviEntretien.getContratLocation().getCodeContrat());
        notification2.setSendTo(String.valueOf(suiviEntretien.getContratLocation().getBienImmobilier().getPersonne().getId()));
        notification2.setLu(false);
        notification2.setDateNotification(new Date());
        notification2.setUrl("/suivi-entretien/" + suiviEntretien.getId());
        notification2.setDateNotification(new Date());
        notification2.setCreerLe(new Date());
        notification2.setCreerPar(personne.getId());
        notificationService.save(notification2);

        suiviEntretien.setModifierLe(new Date());
        suiviEntretien.setModifierPar(personne.getId());
        return suiviEntretienRepository.save(suiviEntretien);
    }

    @Override
    public void debuterEntretien(Long id, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        SuiviEntretien suiviEntretien = suiviEntretienRepository.findById(id).orElse(null);
        suiviEntretien.setEtatSuiviEntretien("En cours");

        if (verifyRoleCode(personne.getRole().getCode())) {
            Notification notification1 = new Notification();
            notification1.setTitre("Début d'un entretien");
            notification1.setMessage("L'entretien " + suiviEntretien.getCodeSuiviEntretien() + " avec le libelle " + suiviEntretien.getLibelle() + " a débuté.");
            notification1.setSendTo(String.valueOf(suiviEntretien.getContratLocation().getClient().getId()));
            notification1.setLu(false);
            notification1.setDateNotification(new Date());
            notification1.setUrl("/suivi-entretien/" + suiviEntretien.getId());
            notification1.setCreerLe(new Date());
            notification1.setCreerPar(personne.getId());

            notificationService.save(notification1);
        } else {
            Notification notification1 = new Notification();
            notification1.setTitre("Début d'un entretien");
            notification1.setMessage("L'entretien " + suiviEntretien.getCodeSuiviEntretien() + " avec le libelle " + suiviEntretien.getLibelle() + " a débuté.");
            notification1.setSendTo(String.valueOf(suiviEntretien.getContratLocation().getCreerPar()));
            notification1.setLu(false);
            notification1.setDateNotification(new Date());
            notification1.setUrl("/suivi-entretien/" + suiviEntretien.getId());
            notification1.setCreerLe(new Date());
            notification1.setCreerPar(personne.getId());

            notificationService.save(notification1);
        }

        suiviEntretienRepository.save(suiviEntretien);
    }

    @Override
    public void terminerEntretien(Long id, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        SuiviEntretien suiviEntretien = suiviEntretienRepository.findById(id).orElse(null);
        suiviEntretien.setEtatSuiviEntretien("Terminé");

        if (verifyRoleCode(personne.getRole().getCode())) {
            Notification notification1 = new Notification();
            notification1.setTitre("Début d'un entretien");
            notification1.setMessage("L'entretien " + suiviEntretien.getCodeSuiviEntretien() + " avec le libelle " + suiviEntretien.getLibelle() + " vient d'être terminé.");
            notification1.setSendTo(String.valueOf(suiviEntretien.getContratLocation().getClient().getId()));
            notification1.setLu(false);
            notification1.setDateNotification(new Date());
            notification1.setUrl("/suivi-entretien/" + suiviEntretien.getId());
            notification1.setCreerLe(new Date());
            notification1.setCreerPar(personne.getId());

            notificationService.save(notification1);
        } else {
            Notification notification1 = new Notification();
            notification1.setTitre("Début d'un entretien");
            notification1.setMessage("L'entretien " + suiviEntretien.getCodeSuiviEntretien() + " avec le libelle " + suiviEntretien.getLibelle() + " vient d'être terminé.");
            notification1.setSendTo(String.valueOf(suiviEntretien.getContratLocation().getCreerPar()));
            notification1.setLu(false);
            notification1.setDateNotification(new Date());
            notification1.setUrl("/suivi-entretien/" + suiviEntretien.getId());
            notification1.setCreerLe(new Date());
            notification1.setCreerPar(personne.getId());

            notificationService.save(notification1);
        }

        suiviEntretienRepository.save(suiviEntretien);
    }

    private boolean verifyRoleCode(String roleCode) {
        return roleCode.equals("ROLE_PROPRIETAIRE") || roleCode.equals("ROLE_RESPONSABLE") ||
                roleCode.equals("ROLE_AGENTIMMOBILIER") || roleCode.equals("ROLE_DEMARCHEUR") ||
                roleCode.equals("ROLE_GERANT");
    }
}
