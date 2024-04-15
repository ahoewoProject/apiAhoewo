package com.memoire.apiAhoewo.models.gestionDesComptes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.memoire.apiAhoewo.models.EntiteDeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
