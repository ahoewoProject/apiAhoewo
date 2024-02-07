package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@Entity
@Table(name = "quartiers")
public class Quartier extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_quartier", unique = true,nullable = false)
    private String codeQuartier;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @ManyToOne()
    @JoinColumn(name = "ville_id")
    private Ville ville;

    @Column(name = "etat")
    private boolean etat;

    public Quartier() {

    }

    public Quartier(Long id, String codeQuartier, String libelle, Ville ville) {
        this.id = id;
        this.codeQuartier = codeQuartier;
        this.libelle = libelle;
        this.ville = ville;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getCodeQuartier() {
        return codeQuartier;
    }

    public void setCodeQuartier(String codeQuartier) {
        this.codeQuartier = codeQuartier;
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

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Quartier{" +
                "id=" + id +
                ", codeQuartier='" + codeQuartier + '\'' +
                ", libelle='" + libelle + '\'' +
                ", ville=" + ville +
                ", etat=" + etat +
                '}';
    }
}
