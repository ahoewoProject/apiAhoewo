package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.requestForm.RegisterForm;

import java.security.Principal;
import java.util.List;

public interface PersonneService {
    public Personne findByUsername(String username);

    public Personne findByEmail(String email);

    public Personne findById(Long id);

    public Personne modifierProfil(RegisterForm registerForm, Principal principal, Long id);

    public boolean usernameExists(String username);

    public boolean emailExists(String email);

    public int countUsers();

    public void certifierCompte(Long id);

    public void desactiverCompte(Long id);

    public void activerCompte(Long id);

    public Personne register(RegisterForm registerForm);

    public void sendPasswordResetEmail(Personne personne);

    public boolean resetPassword(String token, String newPassword);

    public List<Personne> getAllResponsablesAndDemarcheurs();
}
