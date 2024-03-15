package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "biens_immobiliers")
@DiscriminatorColumn(name = "type_bien")
@Inheritance(strategy = InheritanceType.JOINED)
public class BienImmobilier extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "code_bien", nullable = false, unique = true)
    protected String codeBien;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    protected String description;

    @Column(name = "adresse", nullable = false)
    protected String adresse;

    @Column(name = "surface", nullable = false)
    protected Integer surface;

    @ManyToOne()
    @JoinColumn(name = "type_de_bien_id")
    protected TypeDeBien typeDeBien;

    @Column(name = "categorie")
    protected String categorie;

    @Column(name = "statut_bien", nullable = false)
    protected String statutBien;

    @Column(name = "etat_bien")
    protected Boolean etatBien;

    @ManyToOne
    @JoinColumn(name = "pays_id")
    protected Pays pays;

    @ManyToOne
    @JoinColumn(name = "region_id")
    protected Region region;

    @ManyToOne
    @JoinColumn(name = "ville_id")
    protected Ville ville;

    @ManyToOne
    @JoinColumn(name = "quartier_id")
    protected Quartier quartier;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    protected Personne personne;

    @ManyToOne
    @JoinColumn(name = "agence_immobiliere_id")
    protected AgenceImmobiliere agenceImmobiliere;

    @Column(name = "est_delegue")
    private Boolean estDelegue;
}
