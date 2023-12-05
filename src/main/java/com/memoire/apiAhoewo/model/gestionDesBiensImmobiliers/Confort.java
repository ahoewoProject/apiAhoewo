package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import javax.persistence.*;

@Entity
@Table(name = "conforts")
public class Confort extends Equipements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chauffage")
    private Boolean chauffage;

    @Column(name = "climatisation")
    private Boolean climatisation;

    @Column(name = "nombre_fauteuils")
    private Integer nombreFauteuils;

    @Column(name = "nombre_lits")
    private Integer nombreLits;

    @Column(name = "seche_cheveux")
    private Boolean secheCheveux;

    public Confort(Long id, Boolean chauffage, Boolean climatisation, Integer nombreFauteuils,
                   Integer nombreLits, Boolean secheCheveux) {
        this.id = id;
        this.chauffage = chauffage;
        this.climatisation = climatisation;
        this.nombreFauteuils = nombreFauteuils;
        this.nombreLits = nombreLits;
        this.secheCheveux = secheCheveux;
    }

    public Confort() {

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getChauffage() {
        return chauffage;
    }

    public void setChauffage(Boolean chauffage) {
        this.chauffage = chauffage;
    }

    public Boolean getClimatisation() {
        return climatisation;
    }

    public void setClimatisation(Boolean climatisation) {
        this.climatisation = climatisation;
    }

    public Integer getNombreFauteuils() {
        return nombreFauteuils;
    }

    public void setNombreFauteuils(Integer nombreFauteuils) {
        this.nombreFauteuils = nombreFauteuils;
    }

    public Integer getNombreLits() {
        return nombreLits;
    }

    public void setNombreLits(Integer nombreLits) {
        this.nombreLits = nombreLits;
    }

    public Boolean getSecheCheveux() {
        return secheCheveux;
    }

    public void setSecheCheveux(Boolean secheCheveux) {
        this.secheCheveux = secheCheveux;
    }

    @Override
    public String toString() {
        return "Confort{" +
                "id=" + id +
                ", chauffage=" + chauffage +
                ", climatisation=" + climatisation +
                ", nombreFauteuils=" + nombreFauteuils +
                ", nombreLits=" + nombreLits +
                ", secheCheveux=" + secheCheveux +
                '}';
    }
}
