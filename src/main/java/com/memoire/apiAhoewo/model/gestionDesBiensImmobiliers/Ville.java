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

    @Column(name = "libelle", unique = true)
    private String libelle;

    @ManyToOne()
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "etat")
    private boolean etat;

    public Ville() {

    }

    public Ville(Long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
                ", libelle='" + libelle + '\'' +
                ", region=" + region +
                ", etat=" + etat +
                '}';
    }
}
