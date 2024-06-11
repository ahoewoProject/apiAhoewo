package com.memoire.apiAhoewo.servicesImpls.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesComptes.AgentImmobilierRepository;
import com.memoire.apiAhoewo.services.EmailSenderService;
import com.memoire.apiAhoewo.services.GenererMotDePasseService;
import com.memoire.apiAhoewo.services.gestionDesComptes.AgentImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AgentImmobilierServiceImpl implements AgentImmobilierService {
    @Autowired
    private AgentImmobilierRepository agentImmobilierRepository;
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
    public List<AgentImmobilier> getAll() {
        return this.agentImmobilierRepository.findAllByOrderByCreerLeDesc();
    }

    @Override
    public AgentImmobilier findById(Long id) {
        return agentImmobilierRepository.findById(id).orElse(null);
    }

    @Override
    public AgentImmobilier findByMatricule(String matricule) {
        return agentImmobilierRepository.findByMatricule(matricule);
    }

    @Override
    public AgentImmobilier save(AgentImmobilier agentImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        String username = personneService.genererUniqueUsername(agentImmobilier.getPrenom());
        String motDePasse = genererMotDePasseService.genererMotDePasse(8);
        agentImmobilier.setMatricule("AGENT" + UUID.randomUUID());
        agentImmobilier.setUsername(username);
        agentImmobilier.setMotDePasse(passwordEncoder.encode(motDePasse));
        agentImmobilier.setEtatCompte(true);
        if (personne.getEstCertifie()) {
            agentImmobilier.setEstCertifie(true);
        } else {
            agentImmobilier.setEstCertifie(false);
        }
        agentImmobilier.setAutorisation(false);
        agentImmobilier.setCreerLe(new Date());
        agentImmobilier.setCreerPar(personne.getId());
        agentImmobilier.setStatut(true);
        AgentImmobilier savedAgent = agentImmobilierRepository.save(agentImmobilier);
        savedAgent.setMatricule("AGENT00" + savedAgent.getId());
        AgentImmobilier newAgent = agentImmobilierRepository.save(savedAgent);

        String contenu = "Bonjour " + agentImmobilier.getPrenom() + " " + agentImmobilier.getNom() + ",\n\n" +
                "Votre compte a été créé avec succès suite à l'enregistrement de vos informations auprès de l'agence immobilière pour laquelle vous travaillez.\n" +
                "Voici vos identifiants de connexion :\n" +
                "Nom d'utilisateur : " + username + "\n" +
                "Mot de passe : " + motDePasse + "\n\n" +
                "Vous pouvez maintenant vous connecter à votre compte.\n\n" +
                "Cordialement,\n" +
                "L'équipe Ahoewo";
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(env.getProperty("spring.mail.username"), agentImmobilier.getEmail(), "Informations de connexion", contenu);
        });


        return newAgent;
    }

    @Override
    public boolean matriculeExists(String matricule) {
        return agentImmobilierRepository.existsByMatricule(matricule);
    }
}
