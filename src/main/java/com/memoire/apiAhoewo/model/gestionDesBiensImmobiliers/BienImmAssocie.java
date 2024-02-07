package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import javax.persistence.*;

@Entity(name = "biens_immobiliers_associes")
@DiscriminatorValue(value = "BIEN_IMMOBILIER_ASSOCIE")
public class BienImmAssocie extends BienImmobilier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "bien_immobilier_id")
    private BienImmobilier bienImmobilier;

    public BienImmAssocie() {
    }

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

    @Override
    public String toString() {
        return "BienImmAssocie{" +
                "bienImmobilier=" + bienImmobilier +
                ", id=" + id +
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
