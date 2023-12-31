package com.memoire.apiAhoewo.requestForm;

import com.memoire.apiAhoewo.model.gestionDesComptes.Role;

public class RegisterForm {
    private Long id;
    private String nom;
    private String prenom;
    private String username;
    private String email;
    private String motDePasse;
    private String telephone;
    private Role role;

    public RegisterForm() {
        this.id = 0L;
        this.nom = "";
        this.prenom = "";
        this.username = "";
        this.email = "";
        this.motDePasse = "";
        this.telephone = "";
        this.role = new Role();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

