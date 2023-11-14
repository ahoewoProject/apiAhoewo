package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.filter.RefreshTokenFilter;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.requestForm.RegisterForm;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PersonneController {
    private final PersonneService personneService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> savePersonne(@RequestBody RegisterForm registerForm) {
        // Vérification de l'existence du nom d'utilisateur
        if (personneService.usernameExists(registerForm.getUsername())) {
            return new ResponseEntity<>("Un utilisateur avec ce nom d'utilisateur existe déjà", HttpStatus.CONFLICT);
        } else if (personneService.emailExists(registerForm.getEmail())) {
            return new ResponseEntity<>("Un utilisateur avec cette adresse e-mail existe déjà", HttpStatus.CONFLICT);
        }
        Personne personne = personneService.register(registerForm);
        String message = "Utilisateur créé avec succès";

        // Créer une structure pour la réponse contenant l'objet personne et le message de succès
        Map<String, Object> response = new HashMap<>();
        response.put("personne", personne);
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/modifier/profil/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierProfil(@RequestBody RegisterForm registerForm, Principal principal, @PathVariable Long id) {
        // Vérification de l'existence du nom d'utilisateur
        if (personneService.usernameExists(registerForm.getUsername())) {
            return new ResponseEntity<>("Un utilisateur avec nom d'utilisateur existe déjà", HttpStatus.CONFLICT);
        }
        Personne personne = personneService.modifierProfil(registerForm, principal, id);
        return ResponseEntity.ok(personne);
    }

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public Personne infoPersonne(Principal principal){
        return personneService.findByUsername(principal.getName());
    }

    @RequestMapping(value = "/refresh-token", method = RequestMethod.GET)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RefreshTokenFilter refreshTokenFilter = new RefreshTokenFilter(personneService);
        refreshTokenFilter.processRefreshToken(request, response);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Personne findById(@PathVariable Long id) {

        Personne personne = new Personne();
        try {
            personne = this.personneService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return personne;
    }

    @RequestMapping(value = "/certifier/compte/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void certifierCompte(@PathVariable Long id){
        this.personneService.certifierCompte(id);
    }

    @RequestMapping(value = "/activer/compte/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void activerCompte(@PathVariable Long id){
        this.personneService.activerCompte(id);
    }

    @RequestMapping(value = "/desactiver/compte/{id}", method = RequestMethod.GET, headers = "accept=Application/json")
    public void desactiverCompte(@PathVariable Long id){
        this.personneService.desactiverCompte(id);
    }

    @RequestMapping(value = "/count/users", method = RequestMethod.GET)
    public int countUsers(){
        int nombres = this.personneService.countUsers();
        return nombres;
    }

    @RequestMapping(value = "/request-reset-password", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> requestPasswordReset(@RequestParam("email") String email) {
        Personne personne = personneService.findByEmail(email);
        if (personne != null) {
            personneService.sendPasswordResetEmail(personne);
            Map<String, Object> response = new HashMap<>();
            String message = "Email de réinitialisation du mot de passe envoyé.";
            response.put("message", message);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Email introuvable.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        if (personneService.resetPassword(token, newPassword)) {
            Map<String, Object> response = new HashMap<>();
            String message = "Mot de passe réinitialisé avec succès.";
            response.put("message", message);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Token invalide.");
        }
    }

    @RequestMapping(value = "/responsables/demarcheurs", method = RequestMethod.GET)
    public List<Personne> listeResponsablesEtDemarcheurs() {

        List<Personne> personnes = new ArrayList<>();
        try {
            personnes = this.personneService.getAllResponsablesAndDemarcheurs();
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return personnes;
    }
}