package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Gerant;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesComptes.GerantRepository;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.GenererMotDePasseService;
import com.memoire.apiAhoewo.service.GenererUsernameService;
import com.memoire.apiAhoewo.service.gestionDesComptes.GerantService;
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
public class GerantServiceImpl implements GerantService {
    @Autowired
    private GerantRepository gerantRepository;
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
    public Page<Gerant> getGerants(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return gerantRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public Page<Gerant> findGerantsByProprietaire(Principal principal, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        Personne personne = personneService.findByUsername(principal.getName());
        return gerantRepository.findAllByCreerParOrderByCreerLeDesc(personne.getId(), pageRequest);
    }

    @Override
    public Gerant findById(Long id) {
        return gerantRepository.findById(id).orElse(null);
    }

    @Override
    public Gerant save(Gerant gerant, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        String username = personneService.genererUniqueUsername(gerant.getPrenom());
        String motDePasse = genererMotDePasseService.genererMotDePasse(8);
        gerant.setMatricule("GERAN" + UUID.randomUUID());
        gerant.setUsername(username);
        gerant.setMotDePasse(passwordEncoder.encode(motDePasse));
        gerant.setEtatCompte(true);
        gerant.setEstCertifie(true);
        gerant.setAutorisation(false);
        gerant.setCreerLe(new Date());
        gerant.setCreerPar(personne.getId());
        gerant.setStatut(true);
        Gerant gerantSaved = gerantRepository.save(gerant);
        gerantSaved.setMatricule("GERAN00" + gerantSaved.getId());
        Gerant newGerant = gerantRepository.save(gerantSaved);

        String contenu = "Bonjour " + gerant.getPrenom() + " " + gerant.getNom() + ",\n\n" +
                "Votre compte a été créé avec succès suite à l'enregistrement de vos informations par le propriétaire " + personne.getNom() + " " + personne.getPrenom() + ".\n" +
                "Voici vos identifiants de connexion :\n" +
                "Nom d'utilisateur : " + username + "\n" +
                "Mot de passe : " + motDePasse + "\n\n" +
                "Vous pouvez maintenant vous connecter à votre compte.\n\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), gerant.getEmail(), "Informations de connexion", contenu);
        });


        return newGerant;
    }

    @Override
    public void deleteById(Long id) {
        gerantRepository.deleteById(id);
    }

    @Override
    public int countGerants() {
        return (int) gerantRepository.count();
    }

    @Override
    public int countGerantsByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<Gerant> gerantList = gerantRepository.findByCreerPar(personne.getId());
        int count = gerantList.size();
        return count;
    }

}
