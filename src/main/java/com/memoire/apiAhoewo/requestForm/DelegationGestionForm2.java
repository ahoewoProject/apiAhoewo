package com.memoire.apiAhoewo.requestForm;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.*;

public class DelegationGestionForm2 {
    private TypeDeBien typeDeBien;
    private String matriculeProprietaire;
    private String matriculeBienImmo;
    private String categorie;
    private Pays pays;
    private Region region;
    private Ville ville;
    private Quartier quartier;
    private AgenceImmobiliere agenceImmobiliere;
    private String adresse;
    private Integer surface;
    private String description;

    public DelegationGestionForm2(TypeDeBien typeDeBien, String matriculeProprietaire,
                                  String matriculeBienImmo, String categorie, Pays pays,
                                  Region region, Ville ville, Quartier quartier, AgenceImmobiliere agenceImmobiliere,
                                  String adresse, Integer surface, String description) {
        this.typeDeBien = typeDeBien;
        this.matriculeProprietaire = matriculeProprietaire;
        this.matriculeBienImmo = matriculeBienImmo;
        this.categorie = categorie;
        this.pays = pays;
        this.region = region;
        this.ville = ville;
        this.quartier = quartier;
        this.agenceImmobiliere = agenceImmobiliere;
        this.adresse = adresse;
        this.surface = surface;
        this.description = description;
    }

    public DelegationGestionForm2() {

    }

    public TypeDeBien getTypeDeBien() {
        return typeDeBien;
    }

    public void setTypeDeBien(TypeDeBien typeDeBien) {
        this.typeDeBien = typeDeBien;
    }

    public String getMatriculeProprietaire() {
        return matriculeProprietaire;
    }

    public void setMatriculeProprietaire(String matriculeProprietaire) {
        this.matriculeProprietaire = matriculeProprietaire;
    }

    public String getMatriculeBienImmo() {
        return matriculeBienImmo;
    }

    public void setMatriculeBienImmo(String matriculeBienImmo) {
        this.matriculeBienImmo = matriculeBienImmo;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Quartier getQuartier() {
        return quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getSurface() {
        return surface;
    }

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public AgenceImmobiliere getAgenceImmobiliere() {
        return agenceImmobiliere;
    }

    public void setAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        this.agenceImmobiliere = agenceImmobiliere;
    }
}
