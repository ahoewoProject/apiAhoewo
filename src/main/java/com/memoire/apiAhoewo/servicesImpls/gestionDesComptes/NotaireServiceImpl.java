package com.memoire.apiAhoewo.servicesImpls.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Notaire;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesComptes.NotaireRepository;
import com.memoire.apiAhoewo.services.EmailSenderService;
import com.memoire.apiAhoewo.services.GenererMotDePasseService;
import com.memoire.apiAhoewo.services.gestionDesComptes.NotaireService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
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
public class NotaireServiceImpl implements NotaireService {
    @Autowired
    private NotaireRepository notaireRepository;
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
    public List<Notaire> getAll() {
        return notaireRepository.findAll();
    }

    @Override
    public Page<Notaire> getNotaires(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return notaireRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public Notaire findById(Long id) {
        return notaireRepository.findById(id).orElse(null);
    }

    @Override
    public Notaire save(Notaire notaire, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        String username = personneService.genererUniqueUsername(notaire.getPrenom());
        String motDePasse = genererMotDePasseService.genererMotDePasse(8);
        notaire.setMatricule("NOTAI" + UUID.randomUUID());
        notaire.setUsername(username);
        notaire.setMotDePasse(passwordEncoder.encode(motDePasse));
        notaire.setEtatCompte(true);
        notaire.setEstCertifie(true);
        notaire.setAutorisation(false);
        notaire.setCreerLe(new Date());
        notaire.setCreerPar(personne.getId());
        notaire.setStatut(true);
        Notaire savedNotaire = notaireRepository.save(notaire);
        savedNotaire.setMatricule("NOTAI00" + savedNotaire.getId());
        Notaire newNotaire = notaireRepository.save(savedNotaire);

        String contenu = "Bonjour " + notaire.getPrenom() + " " + notaire.getNom() + ",\n\n" +
                "Votre compte a été créé avec succès suite à l'enregistrement de vos informations par l'administrateur " + personne.getNom() + " " + personne.getPrenom() + ".\n" +
                "Voici vos identifiants de connexion :\n" +
                "Nom d'utilisateur : " + username + "\n" +
                "Mot de passe : " + motDePasse + "\n\n" +
                "Vous pouvez maintenant vous connecter à votre compte.\n\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), notaire.getEmail(), "Informations de connexion", contenu);
        });


        return newNotaire;
    }

    @Override
    public void deleteById(Long id) {
        notaireRepository.deleteById(id);
    }

    @Override
    public int countNotaires() {
        return (int) notaireRepository.count();
    }
}
