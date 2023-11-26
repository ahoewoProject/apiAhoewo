package com.memoire.apiAhoewo.model.gestionDesComptes;

import javax.persistence.*;

@Entity
@Table(name="demarcheurs")
@DiscriminatorValue("Demarcheur")
public class Demarcheur extends Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    public Demarcheur() {
    }

    public Demarcheur(Long id, String nom, String prenom, String matricule, String username,
                      String motDePasse, String email, String telephone, String resetToken, Boolean etatCompte,
                      Boolean estCertifie, Boolean autorisation, Role role, Long id1) {
        super(id, nom, prenom, matricule, username, motDePasse, email, telephone, resetToken,
                etatCompte, estCertifie, autorisation, role);
        this.id = id1;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Demarcheur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", matricule='" + matricule + '\'' +
                ", username='" + username + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", etatCompte=" + etatCompte +
                ", estCertifie=" + estCertifie +
                ", autorisation=" + autorisation +
                '}';
    }
}