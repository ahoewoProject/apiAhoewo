package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;

import javax.persistence.*;

@Entity
@Table(name = "biens_immobiliers")
public class BienImmobilier extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_identifiant", nullable = false, unique = true)
    private String numeroIdentifiant;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "ville", nullable = false)
    private String ville;

    @Column(name = "surface", nullable = false)
    private Integer surface;

    @ManyToOne()
    @JoinColumn(name = "proprietaire_id")
    private Personne personne;

    @ManyToOne()
    @JoinColumn(name = "type_de_bien_id")
    private TypeDeBien typeDeBien;

    @Column(name = "image_principale")
    private String imagePrincipale;

    @Column(name = "statut_bien", nullable = false)
    private String statutBien;

    @Column(name = "etat_bien")
    private Boolean etatBien;

    public BienImmobilier() {
    }

    public BienImmobilier(Long id, String numeroIdentifiant, String description, String adresse,
                          String ville, Integer surface, Personne personne, TypeDeBien typeDeBien, String imagePrincipale,
                          String statutBien, Boolean etatBien) {
        this.id = id;
        this.numeroIdentifiant = numeroIdentifiant;
        this.description = description;
        this.adresse = adresse;
        this.ville = ville;
        this.surface = surface;
        this.personne = personne;
        this.typeDeBien = typeDeBien;
        this.imagePrincipale = imagePrincipale;
        this.statutBien = statutBien;
        this.etatBien = etatBien;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroIdentifiant() {
        return numeroIdentifiant;
    }

    public void setNumeroIdentifiant(String numeroIdentifiant) {
        this.numeroIdentifiant = numeroIdentifiant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getSurface() {
        return surface;
    }

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public TypeDeBien getTypeDeBien() {
        return typeDeBien;
    }

    public void setTypeDeBien(TypeDeBien typeDeBien) {
        this.typeDeBien = typeDeBien;
    }

    public String getImagePrincipale() {
        return imagePrincipale;
    }

    public void setImagePrincipale(String imagePrincipale) {
        this.imagePrincipale = imagePrincipale;
    }

    public String getStatutBien() {
        return statutBien;
    }

    public void setStatutBien(String statutBien) {
        this.statutBien = statutBien;
    }

    public Boolean getEtatBien() {
        return etatBien;
    }

    public void setEtatBien(Boolean etatBien) {
        this.etatBien = etatBien;
    }

    @Override
    public String toString() {
        return "BienImmobilier{" +
                "id=" + id +
                ", numeroIdentifiant='" + numeroIdentifiant + '\'' +
                ", description='" + description + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", surface=" + surface +
                ", personne=" + personne +
                ", typeDeBien=" + typeDeBien +
                ", imagePrincipale='" + imagePrincipale + '\'' +
                ", statutBien='" + statutBien + '\'' +
                ", etatBien=" + etatBien +
                '}';
    }
}
