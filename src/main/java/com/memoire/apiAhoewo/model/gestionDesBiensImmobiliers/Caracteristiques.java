package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@JsonIgnoreProperties("bienImmobilier")
@Entity(name = "caracteristiques")
public class Caracteristiques extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bien_immobilier_id")
    private BienImmobilier bienImmobilier;

    @Column(name = "nombre_chambres")
    private Integer nombreChambres;

    @Column(name = "nombre_chambres_salon")
    private Integer nombreChambresSalon;

    @Column(name = "nombre_bureaux")
    private Integer nombreBureaux;

    @Column(name = "nombre_boutiques")
    private Integer nombreBoutiques;

    @Column(name = "nombre_magasins")
    private Integer nombreMagasins;

    @Column(name = "nombre_appartements")
    private Integer nombreAppartements;

    @Column(name = "nombre_etages")
    private Integer nombreEtages;

    @Column(name = "nombre_garages")
    private Integer nombreGarages;

    @Column(name = "nombre_salons")
    private Integer nombreSalons;

    @Column(name = "nombre_placards")
    private Integer nombrePlacards;

    @Column(name = "nombre_cuisine_interne")
    private Integer nombreCuisineInterne;

    @Column(name = "nombre_cuisine_externe")
    private Integer nombreCuisineExterne;

    @Column(name = "nombre_WCDouche_interne")
    private Integer nombreWCDoucheInterne;

    @Column(name = "nombre_WCDouche_externe")
    private Integer nombreWCDoucheExterne;

    @Column(name = "eau_tde")
    private Boolean eauTde;

    @Column(name = "eau_forage")
    private Boolean eauForage;

    @Column(name = "electricite_Ceet")
    private Boolean electriciteCeet;

    @Column(name = "wifi")
    private Boolean wifi;

    @Column(name = "cuisine_interne")
    private Boolean cuisineInterne;

    @Column(name = "cuisine_externe")
    private Boolean cuisineExterne;

    @Column(name = "wc_douche_interne")
    private Boolean wcDoucheInterne;

    @Column(name = "wc_douche_externe")
    private Boolean wcDoucheExterne;

    @Column(name = "balcon")
    private Boolean balcon;

    @Column(name = "climatisation")
    private Boolean climatisation;

    @Column(name = "piscine")
    private Boolean piscine;

    @Column(name = "parking")
    private Boolean parking;

    @Column(name = "jardin")
    private Boolean jardin;

    @Column(name = "terrasse")
    private Boolean terrasse;

    @Column(name = "ascenseur")
    private Boolean ascenseur;

    @Column(name = "garage")
    private Boolean garage;

    @Column(name = "baie_vitree")
    private Boolean baieVitree;

    @Column(name = "sol_carelle")
    private Boolean solCarelle;

    @Column(name = "cash_power_personnel")
    private Boolean cashPowerPersonnel;

    @Column(name = "compteur_additionnel")
    private Boolean compteurAdditionnel;

    @Column(name = "compteur_eau")
    private Boolean compteurEau;

    @Column(name = "plafonne")
    private Boolean plafonne;

    @Column(name = "dalle")
    private Boolean dalle;

    @Column(name = "placard")
    private Boolean placard;

    @Column(name = "a_letage")
    private Boolean aLetage;

    @Column(name = "toilette_visiteur")
    private Boolean toiletteVisiteur;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public BienImmobilier getBienImmobilier() {
        return bienImmobilier;
    }

    public void setBienImmobilier(BienImmobilier bienImmobilier) {
        this.bienImmobilier = bienImmobilier;
    }

    public Integer getNombreChambres() {
        return nombreChambres;
    }

    public void setNombreChambres(Integer nombreChambres) {
        this.nombreChambres = nombreChambres;
    }

    public Integer getNombreChambresSalon() {
        return nombreChambresSalon;
    }

    public void setNombreChambresSalon(Integer nombreChambresSalon) {
        this.nombreChambresSalon = nombreChambresSalon;
    }

    public Integer getNombreBureaux() {
        return nombreBureaux;
    }

    public void setNombreBureaux(Integer nombreBureaux) {
        this.nombreBureaux = nombreBureaux;
    }

    public Integer getNombreBoutiques() {
        return nombreBoutiques;
    }

    public void setNombreBoutiques(Integer nombreBoutiques) {
        this.nombreBoutiques = nombreBoutiques;
    }

    public Integer getNombreMagasins() {
        return nombreMagasins;
    }

    public void setNombreMagasins(Integer nombreMagasins) {
        this.nombreMagasins = nombreMagasins;
    }

    public Integer getNombreAppartements() {
        return nombreAppartements;
    }

    public void setNombreAppartements(Integer nombreAppartements) {
        this.nombreAppartements = nombreAppartements;
    }

    public Integer getNombreEtages() {
        return nombreEtages;
    }

    public void setNombreEtages(Integer nombreEtages) {
        this.nombreEtages = nombreEtages;
    }

    public Integer getNombreGarages() {
        return nombreGarages;
    }

    public void setNombreGarages(Integer nombreGarages) {
        this.nombreGarages = nombreGarages;
    }

    public Integer getNombreSalons() {
        return nombreSalons;
    }

    public void setNombreSalons(Integer nombreSalons) {
        this.nombreSalons = nombreSalons;
    }

    public Integer getNombreCuisineInterne() {
        return nombreCuisineInterne;
    }

    public void setNombreCuisineInterne(Integer nombreCuisineInterne) {
        this.nombreCuisineInterne = nombreCuisineInterne;
    }

    public Integer getNombreCuisineExterne() {
        return nombreCuisineExterne;
    }

    public void setNombreCuisineExterne(Integer nombreCuisineExterne) {
        this.nombreCuisineExterne = nombreCuisineExterne;
    }

    public Integer getNombreWCDoucheInterne() {
        return nombreWCDoucheInterne;
    }

    public void setNombreWCDoucheInterne(Integer nombreWCDoucheInterne) {
        this.nombreWCDoucheInterne = nombreWCDoucheInterne;
    }

    public Integer getNombreWCDoucheExterne() {
        return nombreWCDoucheExterne;
    }

    public void setNombreWCDoucheExterne(Integer nombreWCDoucheExterne) {
        this.nombreWCDoucheExterne = nombreWCDoucheExterne;
    }

    public Boolean getEauTde() {
        return eauTde;
    }

    public void setEauTde(Boolean eauTde) {
        this.eauTde = eauTde;
    }

    public Boolean getEauForage() {
        return eauForage;
    }

    public void setEauForage(Boolean eauForage) {
        this.eauForage = eauForage;
    }

    public Boolean getElectriciteCeet() {
        return electriciteCeet;
    }

    public void setElectriciteCeet(Boolean electriciteCeet) {
        this.electriciteCeet = electriciteCeet;
    }

    public Boolean getWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public Boolean getCuisineInterne() {
        return cuisineInterne;
    }

    public void setCuisineInterne(Boolean cuisineInterne) {
        this.cuisineInterne = cuisineInterne;
    }

    public Boolean getCuisineExterne() {
        return cuisineExterne;
    }

    public void setCuisineExterne(Boolean cuisineExterne) {
        this.cuisineExterne = cuisineExterne;
    }

    public Boolean getWcDoucheInterne() {
        return wcDoucheInterne;
    }

    public void setWcDoucheInterne(Boolean wcDoucheInterne) {
        this.wcDoucheInterne = wcDoucheInterne;
    }

    public Boolean getWcDoucheExterne() {
        return wcDoucheExterne;
    }

    public void setWcDoucheExterne(Boolean wcDoucheExterne) {
        this.wcDoucheExterne = wcDoucheExterne;
    }

    public Boolean getBalcon() {
        return balcon;
    }

    public void setBalcon(Boolean balcon) {
        this.balcon = balcon;
    }

    public Boolean getClimatisation() {
        return climatisation;
    }

    public void setClimatisation(Boolean climatisation) {
        this.climatisation = climatisation;
    }

    public Boolean getPiscine() {
        return piscine;
    }

    public void setPiscine(Boolean piscine) {
        this.piscine = piscine;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Boolean getJardin() {
        return jardin;
    }

    public void setJardin(Boolean jardin) {
        this.jardin = jardin;
    }

    public Boolean getTerrasse() {
        return terrasse;
    }

    public void setTerrasse(Boolean terrasse) {
        this.terrasse = terrasse;
    }

    public Boolean getAscenseur() {
        return ascenseur;
    }

    public void setAscenseur(Boolean ascenseur) {
        this.ascenseur = ascenseur;
    }

    public Boolean getGarage() {
        return garage;
    }

    public void setGarage(Boolean garage) {
        this.garage = garage;
    }

    public Boolean getBaieVitree() {
        return baieVitree;
    }

    public void setBaieVitree(Boolean baieVitree) {
        this.baieVitree = baieVitree;
    }

    public Boolean getSolCarelle() {
        return solCarelle;
    }

    public void setSolCarelle(Boolean solCarelle) {
        this.solCarelle = solCarelle;
    }

    public Boolean getCashPowerPersonnel() {
        return cashPowerPersonnel;
    }

    public void setCashPowerPersonnel(Boolean cashPowerPersonnel) {
        this.cashPowerPersonnel = cashPowerPersonnel;
    }

    public Boolean getCompteurAdditionnel() {
        return compteurAdditionnel;
    }

    public void setCompteurAdditionnel(Boolean compteurAdditionnel) {
        this.compteurAdditionnel = compteurAdditionnel;
    }

    public Boolean getPlafonne() {
        return plafonne;
    }

    public void setPlafonne(Boolean plafonne) {
        this.plafonne = plafonne;
    }

    public Boolean getDalle() {
        return dalle;
    }

    public void setDalle(Boolean dalle) {
        this.dalle = dalle;
    }

    public Boolean getPlacard() {
        return placard;
    }

    public void setPlacard(Boolean placard) {
        this.placard = placard;
    }

    public Boolean getaLetage() {
        return aLetage;
    }

    public void setaLetage(Boolean aLetage) {
        this.aLetage = aLetage;
    }

    public Boolean getCompteurEau() {
        return compteurEau;
    }

    public void setCompteurEau(Boolean compteurEau) {
        this.compteurEau = compteurEau;
    }

    public Integer getNombrePlacards() {
        return nombrePlacards;
    }

    public void setNombrePlacards(Integer nombrePlacards) {
        this.nombrePlacards = nombrePlacards;
    }

    public Boolean getToiletteVisiteur() {
        return toiletteVisiteur;
    }

    public void setToiletteVisiteur(Boolean toiletteVisiteur) {
        this.toiletteVisiteur = toiletteVisiteur;
    }
}
