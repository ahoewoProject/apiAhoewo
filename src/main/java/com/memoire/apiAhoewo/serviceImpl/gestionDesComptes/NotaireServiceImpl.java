package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Notaire;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesComptes.NotaireRepository;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.GenererMotDePasseService;
import com.memoire.apiAhoewo.service.GenererUsernameService;
import com.memoire.apiAhoewo.service.gestionDesComptes.NotaireService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<Notaire> getAll() {
        return notaireRepository.findAll();
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

        String contenu = "Bonjour " + notaire.getPrenom() + ",\n\n" +
                "Votre compte a été créé avec succès suite à l'enregistrement de vos informations par l'administrateur " + personne.getNom() + " " + personne.getPrenom() + ".\n" +
                "Voici vos identifiants de connexion :\n" +
                "Nom d'utilisateur : " + username + "\n" +
                "Mot de passe : " + motDePasse + "\n\n" +
                "Vous pouvez maintenant vous connecter à votre compte.\n\n" +
                "Cordialement,\n\n" +
                "L'équipe de support technique - ahoewo !";
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(notaire.getEmail(), "Informations de connexion", contenu);
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
