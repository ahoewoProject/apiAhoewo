package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@Entity
@Table(name = "villes")
public class Ville extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_vile", unique = true, nullable = false)
    private String codeVille;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @ManyToOne()
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "etat")
    private boolean etat;

    public Ville() {

    }

    public Ville(Long id, String codeVille, String libelle, Region region) {
        this.id = id;
        this.codeVille = codeVille;
        this.libelle = libelle;
        this.region = region;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeVille() {
        return codeVille;
    }

    public void setCodeVille(String codeVille) {
        this.codeVille = codeVille;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "id=" + id +
                ", codeVille='" + codeVille + '\'' +
                ", libelle='" + libelle + '\'' +
                ", region=" + region +
                ", etat=" + etat +
                '}';
    }
}
