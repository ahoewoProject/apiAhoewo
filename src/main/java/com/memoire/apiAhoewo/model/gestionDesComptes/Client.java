package com.memoire.apiAhoewo.model.gestionDesComptes;

import javax.persistence.*;

@Entity
@Table(name="clients")
@DiscriminatorValue("Client")
public class Client extends Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    public Client() {
    }

    public Client(Long id, String nom, String prenom, String username, String email, String motDePasse, String telephone, Boolean etatCompte, Boolean estCertifie, Role role, Long id1) {
        super(id, nom, prenom, username, email, motDePasse, telephone, etatCompte, estCertifie, role);
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
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", etatCompte=" + etatCompte +
                ", estCertifie=" + estCertifie +
                ", resetToken='" + resetToken + '\'' +
                '}';
    }
}