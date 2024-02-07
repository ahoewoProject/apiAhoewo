package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.*;
import com.memoire.apiAhoewo.repository.gestionDesComptes.PersonneRepository;
import com.memoire.apiAhoewo.requestForm.RegisterForm;
import com.memoire.apiAhoewo.service.EmailSenderService;
import com.memoire.apiAhoewo.service.GenererUsernameService;
import com.memoire.apiAhoewo.service.gestionDesComptes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

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
    private ResponsableAgenceImmobiliereService responsableAgenceImmobiliereService;
    @Autowired
    private DemarcheurService demarcheurService;
    @Autowired
    private GerantService gerantService;
    @Autowired
    private ProprietaireService proprietaireService;
    @Autowired
    private AgentImmobilierService agentImmobilierService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private GenererUsernameService genererUsernameService;
    @Autowired
    private Environment env;

    public PersonneServiceImpl() {
    }

    public Personne findByUsername(String username){
        return this.personneRepository.findByUsername(username);
    }

    @Override
    public Personne findByMatricule(String matricule) {
        return this.personneRepository.findByMatricule(matricule);
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

    private String genererMatricule(String roleCode, Long id) {
        switch (roleCode) {
            case "ROLE_ADMINISTRATEUR":
                return "ADMIN00" + id;
            case "ROLE_PROPRIETAIRE":
                return "PROPR00" + id;
            case "ROLE_RESPONSABLE":
                return "RESPO00" + id;
            case "ROLE_DEMARCHEUR":
                return "DEMAR00" + id;
            default:
                return "CLIEN00" + id;
        }
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
            case "ROLE_RESPONSABLE":
                personne = new ResponsableAgenceImmobiliere();
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
        personne.setAutorisation(true);
        personne.setRole(role);
        personne.setId(personne.getId());
        personne.setMatricule(genererMatricule(roleCode, personne.getId()));
        personne.setCreerPar(1L);
        personne.setCreerLe(new Date());
        personne.setStatut(true);

        Personne personneInseree = personneRepository.save(personne);
        personneInseree.setMatricule(genererMatricule(roleCode, personneInseree.getId()));
        return personneRepository.save(personneInseree);
    }

    @Override
    public void sendPasswordResetEmail(Personne personne) {
        String token = generateToken();
        personne.setResetToken(token);
        personneRepository.save(personne);

        String resetLink = "http://localhost:4200/#/reset-password?token=" + token;

        String contenu = "Bonjour M/Mlle " + personne.getPrenom() + " " + personne.getNom() + ",\n\n" +
                "Pour réinitialiser votre mot de passe, veuillez cliquer sur le lien ci-dessous :\n" +
                resetLink + "\n\n" +
                "Si vous n'avez pas demandé de réinitialisation de mot de passe, veuillez ignorer cet email.\n\n" +
                "Cordialement,\n" +
                "\nL'équipe support technique - ahoewo !";

        // Envoyer l'email
        emailSenderService.sendMail(env.getProperty("spring.mail.username"), personne.getEmail(), "Réinitialisation du mot de passe", contenu);
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        Personne personne = personneRepository.findByResetToken(token);
        if (personne != null) {
            // Réinitialiser le mot de passe et effacer le token
            personne.setMotDePasse(passwordEncoder.encode(newPassword));
            personne.setResetToken(null);
            if (!personne.getAutorisation()) {
                personne.setAutorisation(true);
                personneRepository.save(personne);
            } else {
                personneRepository.save(personne);
            }
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
                break;

            case "ROLE_RESPONSABLE":
                ResponsableAgenceImmobiliere responsableAgenceImmobiliere = responsableAgenceImmobiliereService.findById(id);
                personneToUpdate = responsableAgenceImmobiliere;
                break;

            case "ROLE_AGENTIMMOBILIER":
                AgentImmobilier agentImmobilier = agentImmobilierService.findById(id);
                personneToUpdate = agentImmobilier;
                break;


            case "ROLE_PROPRIETAIRE":
                Proprietaire proprietaire = proprietaireService.findById(id);
                personneToUpdate = proprietaire;
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
        personneToUpdate.setModifierLe(new Date());
        personneToUpdate.setModifierPar(personne.getId());

        return personneRepository.save(personneToUpdate);
    }

    @Override
    public String genererUniqueUsername(String prenoms) {
        String baseUsername = genererUsernameService.genererUsername(prenoms);
        String username = baseUsername;
        int suffix = 1;

        while (usernameExists(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        return username;

    }

    public boolean usernameExists(String username) {
        return personneRepository.existsByUsername(username);
    }

    @Override
    public boolean matriculeExists(String matricule) {
        return personneRepository.existsByMatricule(matricule);
    }

    public boolean emailExists(String email) {
        return personneRepository.existsByEmail(email);
    }

    private String generateToken() {
        // Générer un token unique, par exemple en utilisant UUID.randomUUID().toString()
        return UUID.randomUUID().toString();
    }
}
