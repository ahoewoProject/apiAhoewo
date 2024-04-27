package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.dto.RegisterForm;

import java.security.Principal;

public interface PersonneService {
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
}