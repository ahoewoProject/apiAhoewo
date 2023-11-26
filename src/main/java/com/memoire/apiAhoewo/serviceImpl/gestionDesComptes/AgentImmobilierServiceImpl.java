package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AgenceImmobiliereRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.AgentImmobilierRepository;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.GenererMotDePasseService;
import com.memoire.apiAhoewo.service.GenererUsernameService;
import com.memoire.apiAhoewo.service.gestionDesComptes.AgentImmobilierService;
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

    @Override
    public List<AgentImmobilier> getAll() {
        return this.agentImmobilierRepository.findAll();
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

        String contenu = "Bonjour " + agentImmobilier.getPrenom() + ",\n\n" +
                "Votre compte a été créé avec succès suite à l'enregistrement de vos informations auprès de l'agence immobilière pour laquelle vous travaillez.\n" +
                "Voici vos identifiants de connexion :\n" +
                "Nom d'utilisateur : " + username + "\n" +
                "Mot de passe : " + motDePasse + "\n\n" +
                "Vous pouvez maintenant vous connecter à votre compte.\n\n" +
                "Cordialement,\n" +
                "\nL'équipe de support technique - ahoewo !";
        CompletableFuture.runAsync(() -> {
            emailSenderService.sendMail(agentImmobilier.getEmail(), "Informations de connexion", contenu);
        });


        return newAgent;
    }

    @Override
    public boolean matriculeExists(String matricule) {
        return agentImmobilierRepository.existsByMatricule(matricule);
    }
}
