package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@Entity
@Table(name = "pays")
public class Pays extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_pays", unique = true, nullable = false)
    private String codePays;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "etat")
    private boolean etat;

    public Pays() {
    }

    public Pays(Long id, String codePays, String libelle) {
        this.id = id;
        this.codePays = codePays;
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodePays() {
        return codePays;
    }

    public void setCodePays(String codePays) {
        this.codePays = codePays;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Pays{" +
                "id=" + id +
                ", codePays='" + codePays + '\'' +
                ", libelle='" + libelle + '\'' +
                ", etat=" + etat +
                '}';
    }
}
