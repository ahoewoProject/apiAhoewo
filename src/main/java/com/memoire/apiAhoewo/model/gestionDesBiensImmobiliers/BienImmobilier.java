package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
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

    @Column(name = "surface", nullable = false)
    private Integer surface;

    @ManyToOne()
    @JoinColumn(name = "type_de_bien_id")
    private TypeDeBien typeDeBien;

    @Column(name = "statut_bien", nullable = false)
    private String statutBien;

    @Column(name = "etat_bien")
    private Boolean etatBien;

    @ManyToOne
    @JoinColumn(name = "pays_id")
    private Pays pays;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne
    @JoinColumn(name = "ville_id")
    private Ville ville;

    @ManyToOne
    @JoinColumn(name = "quartier_id")
    private Quartier quartier;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private Personne personne;

    @ManyToOne
    @JoinColumn(name = "agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;

    public BienImmobilier() {
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

    public Integer getSurface() {
        return surface;
    }

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public TypeDeBien getTypeDeBien() {
        return typeDeBien;
    }

    public void setTypeDeBien(TypeDeBien typeDeBien) {
        this.typeDeBien = typeDeBien;
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

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Quartier getQuartier() {
        return quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public AgenceImmobiliere getAgenceImmobiliere() {
        return agenceImmobiliere;
    }

    public void setAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        this.agenceImmobiliere = agenceImmobiliere;
    }

    @Override
    public String toString() {
        return "BienImmobilier{" +
                "id=" + id +
                ", numeroIdentifiant='" + numeroIdentifiant + '\'' +
                ", description='" + description + '\'' +
                ", adresse='" + adresse + '\'' +
                ", surface=" + surface +
                ", typeDeBien=" + typeDeBien +
                ", statutBien='" + statutBien + '\'' +
                ", etatBien=" + etatBien +
                ", pays=" + pays +
                ", region=" + region +
                ", ville=" + ville +
                ", quartier=" + quartier +
                ", personne=" + personne +
                ", agenceImmobiliere=" + agenceImmobiliere +
                '}';
    }
}
