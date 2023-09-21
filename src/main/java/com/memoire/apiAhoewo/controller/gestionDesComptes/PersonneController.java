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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PersonneController {
    private final PersonneService personneService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<String> savePersonne(@RequestBody RegisterForm registerForm) {
        // Vérification de l'existence du nom d'utilisateur
        if (personneService.usernameExists(registerForm.getUsername())) {
            return new ResponseEntity<>("Un utilisateur avec ce nom d'utilisateur existe déjà", HttpStatus.CONFLICT);
        }
        personneService.register(registerForm);
        return new ResponseEntity<>("Utilisateur créé avec succès", HttpStatus.OK);
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
    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email) {
        Personne personne = personneService.findByEmail(email);
        if (personne != null) {
            personneService.sendPasswordResetEmail(personne);
            return ResponseEntity.ok("Email de réinitialisation du mot de passe envoyé.");
        } else {
            return ResponseEntity.badRequest().body("Email introuvable.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        if (personneService.resetPassword(token, newPassword)) {
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Token invalide.");
        }
    }
}