package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Administrateur;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesComptes.AdministrateurRepository;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.GenererMotDePasseService;
import com.memoire.apiAhoewo.service.GenererUsernameService;
import com.memoire.apiAhoewo.service.gestionDesComptes.AdministrateurService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
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
public class AdministrateurServiceImpl implements AdministrateurService {
    @Autowired
    private AdministrateurRepository administrateurRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private GenererMotDePasseService genererMotDePasseService;
    @Autowired
    private Environment env;

    @Override
    public List<Administrateur> getAll() {
        return administrateurRepository.findAll();
    }

    @Override
    public Page<Administrateur> getAdministrateurs(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return administrateurRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public Administrateur findById(Long id) {
        return administrateurRepository.findById(id).orElse(null);
    }

    @Override
    public Administrateur save(Administrateur administrateur, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        String username = personneService.genererUniqueUsername(administrateur.getPrenom());
        String motDePasse = genererMotDePasseService.genererMotDePasse(8);
        administrateur.setMatricule("ADMIN" + UUID.randomUUID());
        administrateur.setUsername(username);
        administrateur.setMotDePasse(passwordEncoder.encode(motDePasse));
        administrateur.setEtatCompte(true);
        administrateur.setEstCertifie(true);
        administrateur.setAutorisation(false);
        administrateur.setCreerLe(new Date());
        administrateur.setCreerPar(personne.getId());
        administrateur.setStatut(true);
        Administrateur savedAdmin = administrateurRepository.save(administrateur);
        savedAdmin.setMatricule("ADMIN00" + savedAdmin.getId());
        Administrateur newAdmin = administrateurRepository.save(savedAdmin);

        String contenu = "Bonjour " + administrateur.getPrenom() + " " + administrateur.getNom() + ",\n\n" +
                "Votre compte a été créé avec succès suite à l'enregistrement de vos informations par l'administrateur " + personne.getNom() + " " + personne.getPrenom() + ".\n" +
                "Voici vos identifiants de connexion :\n" +
                "Nom d'utilisateur : " + username + "\n" +
                "Mot de passe : " + motDePasse + "\n\n" +
                "Vous pouvez maintenant vous connecter à votre compte.\n\n" +
                "Cordialement,\n\n" +
                "L'équipe support technique - ahoewo !";

        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), administrateur.getEmail(), "Informations de connexion", contenu);
        });

        return newAdmin;
    }

    @Override
    public void deleteById(Long id) {
        administrateurRepository.deleteById(id);
    }

    @Override
    public int countAdministrateurs() {
        return (int) administrateurRepository.count();
    }
}
