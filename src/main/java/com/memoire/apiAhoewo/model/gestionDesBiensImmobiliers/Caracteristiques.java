package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.memoire.apiAhoewo.model.EntiteDeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
