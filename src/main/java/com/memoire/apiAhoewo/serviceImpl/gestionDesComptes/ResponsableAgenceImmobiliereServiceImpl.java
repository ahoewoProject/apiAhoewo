package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesComptes.ResponsableAgenceImmobiliereRepository;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.GenererMotDePasseService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.service.gestionDesComptes.ResponsableAgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ResponsableAgenceImmobiliereServiceImpl implements ResponsableAgenceImmobiliereService {
    @Autowired
    private ResponsableAgenceImmobiliereRepository responsableAgenceImmobiliereRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GenererMotDePasseService genererMotDePasseService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private Environment env;

    @Override
    public List<ResponsableAgenceImmobiliere> getAll() {
        return responsableAgenceImmobiliereRepository.findAll();
    }

    @Override
    public Page<ResponsableAgenceImmobiliere> getResponsables(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return responsableAgenceImmobiliereRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public ResponsableAgenceImmobiliere findById(Long id) {
        return responsableAgenceImmobiliereRepository.findById(id).orElse(null);
    }

    @Override
    public ResponsableAgenceImmobiliere findByMatricule(String matricule) {
        return responsableAgenceImmobiliereRepository.findByMatricule(matricule);
    }

    @Override
    public ResponsableAgenceImmobiliere save(ResponsableAgenceImmobiliere responsable, AgenceImmobiliere agenceImmobiliere, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        String username = personneService.genererUniqueUsername(responsable.getPrenom());
        String motDePasse = genererMotDePasseService.genererMotDePasse(8);
        responsable.setMatricule("RESPO00" + UUID.randomUUID());
        responsable.setUsername(username);
        responsable.setMotDePasse(passwordEncoder.encode(motDePasse));
        responsable.setEtatCompte(true);
        if (personne.getEstCertifie()) {
            responsable.setEstCertifie(true);
        } else {
            responsable.setEstCertifie(false);
        }
        responsable.setAutorisation(false);
        responsable.setCreerLe(new Date());
        responsable.setCreerPar(personne.getId());
        responsable.setStatut(true);
        ResponsableAgenceImmobiliere responsableSaved = responsableAgenceImmobiliereRepository.save(responsable);
        responsableSaved.setMatricule("RESPO00" + responsableSaved.getId());
        ResponsableAgenceImmobiliere newResponsable = responsableAgenceImmobiliereRepository.save(responsableSaved);

        String contenu = "Bonjour " + newResponsable.getPrenom() + " " + newResponsable.getNom() + ",\n\n" +
                "Votre compte a été créé avec succès suite à l'enregistrement de vos informations en tant que co - responsable au sein de notre agence immobilière " + agenceImmobiliere.getNomAgence() + ".\n" +
                "Voici vos identifiants de connexion :\n" +
                "Nom d'utilisateur : " + username + "\n" +
                "Mot de passe : " + motDePasse + "\n\n" +
                "Vous pouvez dès à présent vous connecter à votre compte.\n\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), newResponsable.getEmail(), "Informations de connexion", contenu);
        });

        return newResponsable;
    }

    @Override
    public boolean matriculeExists(String matricule) {
        return responsableAgenceImmobiliereRepository.existsByMatricule(matricule);
    }

    @Override
    public void deleteById(Long id) {
        responsableAgenceImmobiliereRepository.deleteById(id);
    }

    @Override
    public int countResponsablesAgenceImmobiliere() {
        return (int) responsableAgenceImmobiliereRepository.count();
    }
}
