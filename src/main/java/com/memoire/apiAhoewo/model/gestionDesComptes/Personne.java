package com.memoire.apiAhoewo.model.gestionDesComptes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@Entity
@Table(name = "personnes")
@DiscriminatorColumn(name = "type_personne")
@Inheritance(strategy = InheritanceType.JOINED)
public class Personne extends EntiteDeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "nom", nullable = false)
    protected String nom;

    @Column(name = "prenom", nullable = false)
    protected String prenom;

    @Column(name = "matricule", nullable = false)
    protected String matricule;

    @Column(name = "username", unique = true, nullable = false)
    protected String username;

    @Column(name = "mot_de_passe", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String motDePasse;

    @Column(name = "email", nullable = false)
    protected String email;

    @Column(name = "telephone", nullable = false)
    protected String telephone;

    @Column(name = "reset_password_token")
    protected String resetToken;

    @Column(name = "etat_compte", nullable = false)
    protected Boolean etatCompte;

    @Column(name = "est_certifie", nullable = false)
    protected Boolean estCertifie;

    @Column(name = "autorisation", nullable = false)
    protected Boolean autorisation;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;

    public Personne() {
    }

    public Personne(Long id, String nom, String prenom, String matricule,
                    String username, String motDePasse, String email, String telephone,
                    String resetToken, Boolean etatCompte, Boolean estCertifie, Boolean autorisation, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.username = username;
        this.motDePasse = motDePasse;
        this.email = email;
        this.telephone = telephone;
        this.resetToken = resetToken;
        this.etatCompte = etatCompte;
        this.estCertifie = estCertifie;
        this.autorisation = autorisation;
        this.role = role;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Boolean getEtatCompte() {
        return etatCompte;
    }

    public void setEtatCompte(Boolean etatCompte) {
        this.etatCompte = etatCompte;
    }

    public Boolean getEstCertifie() {
        return estCertifie;
    }

    public void setEstCertifie(Boolean estCertifie) {
        this.estCertifie = estCertifie;
    }

    public Boolean getAutorisation() {
        return autorisation;
    }

    public void setAutorisation(Boolean autorisation) {
        this.autorisation = autorisation;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
