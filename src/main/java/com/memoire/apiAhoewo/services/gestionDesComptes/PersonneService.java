package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.dto.RegisterForm;

import java.security.Principal;
import java.util.List;

public interface PersonneService {
    public List<Personne> getAll();

    public Personne findByUsername(String username);

    public Personne findByMatricule(String matricule);

    public Personne findByEmail(String email);

    public Personne findById(Long id);

    public Personne modifierProfil(RegisterForm registerForm, Principal principal, Long id);

    public String genererUniqueUsername(String prenoms);

    public boolean usernameExists(String username);

    public boolean matriculeExists(String matricule);

    public boolean emailExists(String email);

    public int countUsers();

    public void certifierCompte(Long id);

    public void desactiverCompte(Long id);

    public void activerCompte(Long id);

    public Personne register(RegisterForm registerForm);

    public void sendPasswordResetEmail(Personne personne);

    public boolean resetPassword(String token, String newPassword);

    public boolean estNotaire(String code);

    public boolean estProprietaire(String code);

    public boolean estResponsable(String code);

    public boolean estAgentImmobilier(String code);

    public boolean estDemarcheur(String code);

    public boolean estGerant(String code);

    public boolean estAdministrateur(String code);

    public boolean estClient(String code);
}
