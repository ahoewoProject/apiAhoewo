package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;

import javax.persistence.*;

@Entity
@Table(name = "biens_immobiliers")
@DiscriminatorColumn(name = "type_bien")
@Inheritance(strategy = InheritanceType.JOINED)
public class BienImmobilier extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "code_bien", nullable = false, unique = true)
    protected String codeBien;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    protected String description;

    @Column(name = "adresse", nullable = false)
    protected String adresse;

    @Column(name = "surface", nullable = false)
    protected Integer surface;

    @ManyToOne()
    @JoinColumn(name = "type_de_bien_id")
    protected TypeDeBien typeDeBien;

    @Column(name = "categorie")
    protected String categorie;

    @Column(name = "statut_bien", nullable = false)
    protected String statutBien;

    @Column(name = "etat_bien")
    protected Boolean etatBien;

    @ManyToOne
    @JoinColumn(name = "pays_id")
    protected Pays pays;

    @ManyToOne
    @JoinColumn(name = "region_id")
    protected Region region;

    @ManyToOne
    @JoinColumn(name = "ville_id")
    protected Ville ville;

    @ManyToOne
    @JoinColumn(name = "quartier_id")
    protected Quartier quartier;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    protected Personne personne;

    @ManyToOne
    @JoinColumn(name = "agence_immobiliere_id")
    protected AgenceImmobiliere agenceImmobiliere;

    public BienImmobilier() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeBien() {
        return codeBien;
    }

    public void setCodeBien(String codeBien) {
        this.codeBien = codeBien;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public TypeDeBien getTypeDeBien() {
        return typeDeBien;
    }

    public void setTypeDeBien(TypeDeBien typeDeBien) {
        this.typeDeBien = typeDeBien;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getStatutBien() {
        return statutBien;
    }

    public void setStatutBien(String statutBien) {
        this.statutBien = statutBien;
    }

    public Boolean getEtatBien() {
        return etatBien;
    }

    public void setEtatBien(Boolean etatBien) {
        this.etatBien = etatBien;
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

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public AgenceImmobiliere getAgenceImmobiliere() {
        return agenceImmobiliere;
    }

    public void setAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        this.agenceImmobiliere = agenceImmobiliere;
    }

    @Override
    public String toString() {
        return "BienImmobilier{" +
                "id=" + id +
                ", codeBien='" + codeBien + '\'' +
                ", description='" + description + '\'' +
                ", adresse='" + adresse + '\'' +
                ", surface=" + surface +
                ", typeDeBien=" + typeDeBien +
                ", categorie='" + categorie + '\'' +
                ", statutBien='" + statutBien + '\'' +
                ", etatBien=" + etatBien +
                ", pays=" + pays +
                ", region=" + region +
                ", ville=" + ville +
                ", quartier=" + quartier +
                ", personne=" + personne +
                ", agenceImmobiliere=" + agenceImmobiliere +
                '}';
    }
}
