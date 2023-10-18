package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.*;
import com.memoire.apiAhoewo.repository.gestionDesComptes.PersonneRepository;
import com.memoire.apiAhoewo.requestForm.RegisterForm;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.gestionDesComptes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Service
public class PersonneServiceImpl implements PersonneService, UserDetailsService {
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdministrateurService administrateurService;
    @Autowired
    private NotaireService notaireService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AgentImmobilierService agentImmobilierService;
    @Autowired
    private DemarcheurService demarcheurService;
    @Autowired
    private GerantService gerantService;
    @Autowired
    private ProprietaireService proprietaireService;
    @Autowired
    private EmailSenderService emailSenderService;

    public PersonneServiceImpl() {
    }

    public Personne findByUsername(String username){
        return this.personneRepository.findByUsername(username);
    }

    @Override
    public Personne findByEmail(String email) {
        return personneRepository.findByEmail(email);
    }

    @Override
    public Personne findById(Long id) {
        return this.personneRepository.findById(id).orElse(null);
    }

    @Override
    public int countUsers(){
        return  (int) personneRepository.count();
    }

    @Override
    public void certifierCompte(Long id) {
        Personne personne = this.personneRepository.findById(id).orElse(null);
        personne.setEstCertifie(true);
        personneRepository.save(personne);
    }

    @Override
    public void activerCompte(Long id) {
        Personne personne = this.personneRepository.findById(id).orElse(null);
        personne.setEtatCompte(true);
        personneRepository.save(personne);
    }

    @Override
    public void desactiverCompte(Long id) {
        Personne personne = this.personneRepository.findById(id).orElse(null);
        personne.setEtatCompte(false);
        personneRepository.save(personne);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Personne personne = this.personneRepository.findByUsername(username);
        if(personne==null){
            throw new UsernameNotFoundException("User not found");
        }else {
            System.out.println("User found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(personne.getRole().getLibelle()));
        return new org.springframework.security.core.userdetails.User(personne.getUsername(), personne.getMotDePasse(), authorities );
    }

    @Override
    public Personne register(RegisterForm registerForm) {
        Role role = registerForm.getRole();
        String roleCode = role.getCode();

        Personne personne;

        switch (roleCode) {
            case "ROLE_ADMINISTRATEUR":
                personne = new Administrateur();
                break;
            case "ROLE_PROPRIETAIRE":
                personne = new Proprietaire();
                break;
            case "ROLE_AGENTIMMOBILIER":
                personne = new AgentImmobilier();
                break;
            case "ROLE_DEMARCHEUR":
                personne = new Demarcheur();
                break;
            default:
                personne = new Client();
                break;
        }

        personne.setNom(registerForm.getNom());
        personne.setPrenom(registerForm.getPrenom());
        personne.setEmail(registerForm.getEmail());
        personne.setUsername(registerForm.getUsername());
        personne.setMotDePasse(passwordEncoder.encode(registerForm.getMotDePasse()));
        personne.setTelephone(registerForm.getTelephone());
        personne.setEtatCompte(true);
        personne.setEstCertifie(false);
        personne.setRole(role);
        personne.setId(personne.getId());
        personne.setCreerPar(1L);
        personne.setCreerLe(new Date());
        personne.setStatut(true);

        personneRepository.save(personne);
        return personne;
    }

    @Override
    public void sendPasswordResetEmail(Personne personne) {
        String token = generateToken();
        personne.setResetToken(token);
        personneRepository.save(personne);

        String resetLink = "http://localhost:4200/reset-password?token=" + token;

        String contenu = "Bonjour M/Mlle " + personne.getPrenom() + ",\n\n" +
                "Pour réinitialiser votre mot de passe, veuillez cliquer sur le lien ci-dessous :\n" +
                resetLink + "\n\n" +
                "Si vous n'avez pas demandé de réinitialisation de mot de passe, veuillez ignorer cet email.\n\n" +
                "Cordialement,\n" +
                "\nL'équipe de support technique - ahoewo !";

        // Envoyer l'email
        emailSenderService.sendMail(personne.getEmail(), "Réinitialisation du mot de passe", contenu);
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        Personne personne = personneRepository.findByResetToken(token);
        if (personne != null) {
            // Réinitialiser le mot de passe et effacer le token
            personne.setMotDePasse(passwordEncoder.encode(newPassword));
            personne.setResetToken(null);
            personneRepository.save(personne);
            return true;
        }
        return false;
    }

    @Override
    public Personne modifierProfil(RegisterForm registerForm, Principal principal, Long id) {
        Personne personne = personneRepository.findByUsername(principal.getName());
        Role role = registerForm.getRole();
        String roleCode = role.getCode();

        Personne personneToUpdate;
        switch (roleCode) {
            case "ROLE_ADMINISTRATEUR":
                Administrateur administrateur = administrateurService.findById(id);
                personneToUpdate = administrateur;
                System.out.println(personneToUpdate);
                break;

            case "ROLE_PROPRIETAIRE":
                Proprietaire proprietaire = proprietaireService.findById(id);
                personneToUpdate = proprietaire;
                break;

            case "ROLE_AGENTIMMOBILIER":
                AgentImmobilier agentImmobilier = agentImmobilierService.findById(id);
                personneToUpdate = agentImmobilier;
                break;

            case "ROLE_DEMARCHEUR":
                Demarcheur demarcheur = demarcheurService.findById(id);
                personneToUpdate = demarcheur;
                break;

            case "ROLE_GERANT":
                Gerant gerant = gerantService.findById(id);
                personneToUpdate = gerant;
                break;

            case "ROLE_NOTAIRE":
                Notaire notaire = notaireService.findById(id);
                personneToUpdate = notaire;
                break;

            default:
                Client client = clientService.findById(id);
                personneToUpdate = client;
                break;
        }
        personneToUpdate.setNom(registerForm.getNom());
        personneToUpdate.setPrenom(registerForm.getPrenom());
        personneToUpdate.setEmail(registerForm.getEmail());
        personneToUpdate.setUsername(registerForm.getUsername());
        personneToUpdate.setMotDePasse(passwordEncoder.encode(registerForm.getMotDePasse()));
        personneToUpdate.setTelephone(registerForm.getTelephone());
        personneToUpdate.setId(id);
        personneToUpdate.setModifierLe(new Date());
        personneToUpdate.setModifierPar(personne.getId());

        return personneRepository.save(personneToUpdate);
    }

    public boolean usernameExists(String username) {
        return personneRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return personneRepository.existsByEmail(email);
    }

    private String generateToken() {
        // Générer un token unique, par exemple en utilisant UUID.randomUUID().toString()
        return UUID.randomUUID().toString();
    }
}
