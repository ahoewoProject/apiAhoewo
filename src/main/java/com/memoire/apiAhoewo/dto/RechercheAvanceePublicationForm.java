package com.memoire.apiAhoewo.dto;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.Quartier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.TypeDeBien;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechercheAvanceePublicationForm {
    private String typeDeTransaction;
    private TypeDeBien typeDeBien;
    private Quartier quartier;
    private Double prixMin;
    private Double prixMax;
    private Double fraisDeVisite;
    private String categorie;
    private Integer avance;
    private Integer caution;
    private Integer commission;
    private Integer nombreChambres;
    private Integer nombreChambresSalon;
    private Integer nombreBureaux;
    private Integer nombreBoutiques;
    private Integer nombreMagasins;
    private Integer nombreAppartements;
    private Integer nombreEtages;
    private Integer nombreGarages;
    private Integer nombreSalons;
    private Integer nombrePlacards;
    private Integer nombreCuisineInterne;
    private Integer nombreCuisineExterne;
    private Integer nombreWCDoucheInterne;
    private Integer nombreWCDoucheExterne;
    private Boolean eauTde;
    private Boolean eauForage;
    private Boolean electriciteCeet;
    private Boolean wifi;
    private Boolean cuisineInterne;
    private Boolean cuisineExterne;
    private Boolean wcDoucheInterne;
    private Boolean wcDoucheExterne;
    private Boolean balcon;
    private Boolean climatisation;
    private Boolean piscine;
    private Boolean parking;
    private Boolean jardin;
    private Boolean terrasse;
    private Boolean ascenseur;
    private Boolean garage;
    private Boolean baieVitree;
    private Boolean solCarelle;
    private Boolean cashPowerPersonnel;
    private Boolean compteurAdditionnel;
    private Boolean compteurEau;
    private Boolean plafonne;
    private Boolean dalle;
    private Boolean placard;
    private Boolean aLetage;
    private Boolean toiletteVisiteur;
}
