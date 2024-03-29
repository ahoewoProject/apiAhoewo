package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@Entity
@Table(name = "regions")
public class Region extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_region", unique = true, nullable = false)
    private String codeRegion;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @ManyToOne()
    @JoinColumn(name = "pays_id")
    private Pays pays;

    @Column(name = "etat")
    private boolean etat;

    public Region() {

    }

    public Region(Long id, String codeRegion, String libelle, Pays pays) {
        this.id = id;
        this.codeRegion = codeRegion;
        this.libelle = libelle;
        this.pays = pays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRegion() {
        return codeRegion;
    }

    public void setCodeRegion(String codeRegion) {
        this.codeRegion = codeRegion;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", codeRegion='" + codeRegion + '\'' +
                ", libelle='" + libelle + '\'' +
                ", pays=" + pays +
                ", etat=" + etat +
                '}';
    }
}
