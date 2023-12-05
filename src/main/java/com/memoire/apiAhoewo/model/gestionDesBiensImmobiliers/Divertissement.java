package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import javax.persistence.*;

@Entity
@Table(name = "divertissements")
public class Divertissement extends Equipements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "television")
    private Boolean television;

    @Column(name = "piscine")
    private Boolean piscine;

    @Column(name = "jardin")
    private Boolean jardin;

    @Column(name = "salle_de_sport")
    private Boolean salleDeSport;

    public Divertissement(Long id, Boolean television, Boolean piscine, Boolean jardin, Boolean salleDeSport) {
        this.id = id;
        this.television = television;
        this.piscine = piscine;
        this.jardin = jardin;
        this.salleDeSport = salleDeSport;
    }

    public Divertissement() {

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getTelevision() {
        return television;
    }

    public void setTelevision(Boolean television) {
        this.television = television;
    }

    public Boolean getPiscine() {
        return piscine;
    }

    public void setPiscine(Boolean piscine) {
        this.piscine = piscine;
    }

    public Boolean getJardin() {
        return jardin;
    }

    public void setJardin(Boolean jardin) {
        this.jardin = jardin;
    }

    public Boolean getSalleDeSport() {
        return salleDeSport;
    }

    public void setSalleDeSport(Boolean salleDeSport) {
        this.salleDeSport = salleDeSport;
    }

    @Override
    public String toString() {
        return "Divertissement{" +
                "id=" + id +
                ", television=" + television +
                ", piscine=" + piscine +
                ", jardin=" + jardin +
                ", salleDeSport=" + salleDeSport +
                '}';
    }
}
