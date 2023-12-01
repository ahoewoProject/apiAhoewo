package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import javax.persistence.*;

@Entity
@Table(name = "utilitaires")
public class Utilitaire extends Equipements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wifi")
    private Boolean wifi;

    @Column(name = "lave_linge")
    private Boolean laveLinge;

    @Column(name = "cuisine")
    private Boolean cuisine;

    @Column(name = "refrigirateur")
    private Boolean refrigirateur;

    @Column(name = "fer_a_repasser")
    private Boolean ferARepasser;

    @Column(name = "espace_de_travail")
    private Boolean espaceDeTravail;

    @Column(name = "eau")
    private Boolean eau;

    @Column(name = "electricite")
    private Boolean electricite;

    @Column(name = "toilette")
    private Boolean toilette;

    @Column(name = "parking")
    private Boolean parking;

    @Column(name = "nombre_garages")
    private Integer nombreGarages;

    @Column(name = "nombre_salles_de_bains")
    private Integer nombreSallesDeBains;

    @Column(name = "nombre_chambres")
    private Integer nombreChambres;

    @Column(name = "nombre_pieces")
    private Integer nombrePieces;

    @Column(name = "nombre_appartements")
    private Integer nombreAppartements;

    public Utilitaire(Long id, Boolean wifi, Boolean laveLinge, Boolean cuisine, Boolean refrigirateur,
                      Boolean ferARepasser, Boolean espaceDeTravail, Boolean parking, Integer nombreGarages,
                      Integer nombreSallesDeBains, Integer nombreChambres, Integer nombrePieces,
                      Integer nombreAppartements) {
        this.id = id;
        this.wifi = wifi;
        this.laveLinge = laveLinge;
        this.cuisine = cuisine;
        this.refrigirateur = refrigirateur;
        this.ferARepasser = ferARepasser;
        this.espaceDeTravail = espaceDeTravail;
        this.parking = parking;
        this.nombreGarages = nombreGarages;
        this.nombreSallesDeBains = nombreSallesDeBains;
        this.nombreChambres = nombreChambres;
        this.nombrePieces = nombrePieces;
        this.nombreAppartements = nombreAppartements;
    }

    public Utilitaire() {

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public Boolean getLaveLinge() {
        return laveLinge;
    }

    public void setLaveLinge(Boolean laveLinge) {
        this.laveLinge = laveLinge;
    }

    public Boolean getCuisine() {
        return cuisine;
    }

    public void setCuisine(Boolean cuisine) {
        this.cuisine = cuisine;
    }

    public Boolean getRefrigirateur() {
        return refrigirateur;
    }

    public void setRefrigirateur(Boolean refrigirateur) {
        this.refrigirateur = refrigirateur;
    }

    public Boolean getFerARepasser() {
        return ferARepasser;
    }

    public void setFerARepasser(Boolean ferARepasser) {
        this.ferARepasser = ferARepasser;
    }

    public Boolean getEspaceDeTravail() {
        return espaceDeTravail;
    }

    public void setEspaceDeTravail(Boolean espaceDeTravail) {
        this.espaceDeTravail = espaceDeTravail;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Boolean getEau() {
        return eau;
    }

    public void setEau(Boolean eau) {
        this.eau = eau;
    }
    public Integer getNombreGarages() {
        return nombreGarages;
    }
    public void setNombreGarages(Integer nombreGarages) {
        this.nombreGarages = nombreGarages;
    }

    public Integer getNombreSallesDeBains() {
        return nombreSallesDeBains;
    }

    public void setNombreSallesDeBains(Integer nombreSallesDeBains) {
        this.nombreSallesDeBains = nombreSallesDeBains;
    }

    public Boolean getElectricite() {
        return electricite;
    }

    public void setElectricite(Boolean electricite) {
        this.electricite = electricite;
    }

    public Boolean getToilette() {
        return toilette;
    }

    public void setToilette(Boolean toilette) {
        this.toilette = toilette;
    }

    public Integer getNombreChambres() {
        return nombreChambres;
    }

    public void setNombreChambres(Integer nombreChambres) {
        this.nombreChambres = nombreChambres;
    }

    public Integer getNombrePieces() {
        return nombrePieces;
    }

    public void setNombrePieces(Integer nombrePieces) {
        this.nombrePieces = nombrePieces;
    }

    public Integer getNombreAppartements() {
        return nombreAppartements;
    }

    public void setNombreAppartements(Integer nombreAppartements) {
        this.nombreAppartements = nombreAppartements;
    }
}
