package com.memoire.apiAhoewo.serviceImpl.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationAgentAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AffectationResponsableAgence;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AffectationResponsableAgenceRepository;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AffectationResponsableAgenceService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.service.gestionDesComptes.ResponsableAgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AffectationResponsableAgenceServiceImpl implements AffectationResponsableAgenceService {
    @Autowired
    private AffectationResponsableAgenceRepository affectationResponsableAgenceRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private ResponsableAgenceImmobiliereService responsableAgenceImmobiliereService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private Environment env;

    @Override
    public List<AffectationResponsableAgence> getAll() {
        return affectationResponsableAgenceRepository.findAll();
    }

    @Override
    public List<AffectationResponsableAgence> findByAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        return affectationResponsableAgenceRepository.findByAgenceImmobiliere(agenceImmobiliere);
    }

    @Override
    public Page<AffectationResponsableAgence> getAffectationsResponsableAgence(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return affectationResponsableAgenceRepository.findAll(pageRequest);
    }

    @Override
    public List<AffectationResponsableAgence> getResponsablesOfAgences(Principal principal) {
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = (ResponsableAgenceImmobiliere) personneService.findByUsername(principal.getName());

        List<AffectationResponsableAgence> responsableAgences = affectationResponsableAgenceRepository.findByResponsableAgenceImmobiliere(responsableAgenceImmobiliere);

        List<AgenceImmobiliere> agenceImmobilieres = responsableAgences.stream()
                .map(AffectationResponsableAgence::getAgenceImmobiliere)
                .collect(Collectors.toList());

        return affectationResponsableAgenceRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public Page<AffectationResponsableAgence> getResponsablesOfAgencesPagines(int numeroDeLaPage, int elementsParPage, Principal principal) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = (ResponsableAgenceImmobiliere) personneService.findByUsername(principal.getName());

        List<AffectationResponsableAgence> responsableAgences = affectationResponsableAgenceRepository.findByResponsableAgenceImmobiliere(responsableAgenceImmobiliere);
        List<AgenceImmobiliere> agenceImmobilieres = responsableAgences.stream()
                .map(AffectationResponsableAgence::getAgenceImmobiliere)
                .collect(Collectors.toList());
        return affectationResponsableAgenceRepository.findAllByAgenceImmobiliereInOrderByCreerLeDesc(agenceImmobilieres, pageRequest);
    }

    @Override
    public AffectationResponsableAgence findById(Long id) {
        return affectationResponsableAgenceRepository.findById(id).orElse(null);
    }

    @Override
    public AffectationResponsableAgence save(AffectationResponsableAgence affectationResponsableAgence, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        affectationResponsableAgence.setDateDebut(new Date());
        affectationResponsableAgence.setActif(true);
        affectationResponsableAgence.setCreerLe(new Date());
        affectationResponsableAgence.setCreerPar(personne.getId());
        affectationResponsableAgence.setStatut(true);
        return affectationResponsableAgenceRepository.save(affectationResponsableAgence);
    }

    public AffectationResponsableAgence save(ResponsableAgenceImmobiliere responsable, AgenceImmobiliere agenceImmobiliere, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        AffectationResponsableAgence affectationResponsableAgenceSaved = new AffectationResponsableAgence();

        if (responsable.getMatricule().isEmpty()) {
            responsable = responsableAgenceImmobiliereService.save(responsable, agenceImmobiliere, principal);
        }

        affectationResponsableAgenceSaved.setResponsableAgenceImmobiliere(responsable);
        affectationResponsableAgenceSaved.setAgenceImmobiliere(agenceImmobiliere);
        affectationResponsableAgenceSaved.setDateDebut(new Date());
        affectationResponsableAgenceSaved.setActif(true);
        affectationResponsableAgenceSaved.setCreerLe(new Date());
        affectationResponsableAgenceSaved.setCreerPar(personne.getId());
        affectationResponsableAgenceSaved.setStatut(true);

        if (responsable.getMatricule().isEmpty()) {
            String contenu = "Bonjour " + responsable.getPrenom() + " " + responsable.getNom() + ",\n\n" +
                    "Vous avez été affecté à l'agence immobilière " + agenceImmobiliere.getNomAgence() + " par le responsable " + personne.getPrenom() + " " + personne.getNom() + " en tant que co-responsable" +
                    "." +
                    "\n\nCordialement," +
                    "\n\nL'équipe support technique - ahoewo !";

            ResponsableAgenceImmobiliere finalResponsable = responsable;
            CompletableFuture.runAsync(() -> {
                emailSenderService.sendMail(env.getProperty("spring.mail.username"), finalResponsable.getEmail(), "Affectation d'agence en tant que co-responsable", contenu);
            });
        }

        return affectationResponsableAgenceRepository.save(affectationResponsableAgenceSaved);
    }

    @Override
    public AffectationResponsableAgence update(AffectationResponsableAgence affectationResponsableAgence, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        affectationResponsableAgence.setModifierLe(new Date());
        affectationResponsableAgence.setModifierPar(personne.getId());
        return affectationResponsableAgenceRepository.save(affectationResponsableAgence);
    }

    @Override
    public boolean agenceAndResponsableExists(AgenceImmobiliere agenceImmobiliere, ResponsableAgenceImmobiliere responsableAgenceImmobiliere) {
        return this.affectationResponsableAgenceRepository.existsByAgenceImmobiliereAndResponsableAgenceImmobiliere(agenceImmobiliere,
                responsableAgenceImmobiliere);
    }

    @Override
    public boolean agenceAndResponsableMatriculeExists(AgenceImmobiliere agenceImmobiliere, String matricule) {
        return this.affectationResponsableAgenceRepository.existsByAgenceImmobiliereAndResponsableAgenceImmobiliere_Matricule(agenceImmobiliere,
                matricule);
    }

    @Override
    public void activerResponsableAgence(Long id) {
        AffectationResponsableAgence affectationResponsableAgence = this.affectationResponsableAgenceRepository.findById(id).orElse(null);
        affectationResponsableAgence.setActif(true);
        affectationResponsableAgence.setDateDebut(new Date());
        affectationResponsableAgence.setDateFin(null);
        affectationResponsableAgenceRepository.save(affectationResponsableAgence);
    }

    @Override
    public void desactiverResponsableAgence(Long id) {
        AffectationResponsableAgence affectationResponsableAgence = this.affectationResponsableAgenceRepository.findById(id).orElse(null);
        affectationResponsableAgence.setActif(false);
        affectationResponsableAgence.setDateFin(new Date());
        affectationResponsableAgenceRepository.save(affectationResponsableAgence);
    }
}
