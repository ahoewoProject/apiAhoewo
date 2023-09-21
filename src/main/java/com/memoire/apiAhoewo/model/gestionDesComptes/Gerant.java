package com.memoire.apiAhoewo.model.gestionDesComptes;

import javax.persistence.*;

@Entity
@Table(name="gerants")
@DiscriminatorValue("Gerant")
public class Gerant extends Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    public Gerant() {
    }

    public Gerant(Long id, String nom, String prenom, String username, String email, String motDePasse, String telephone, Boolean etatCompte, Boolean estCertifie, Role role, Long id1) {
        super(id, nom, prenom, username, email, motDePasse, telephone, etatCompte, estCertifie, role);
        this.id = id1;
    }
}