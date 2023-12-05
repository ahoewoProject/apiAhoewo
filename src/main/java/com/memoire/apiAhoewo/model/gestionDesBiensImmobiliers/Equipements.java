package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Equipements extends EntiteDeBase {
    @ManyToOne
    @JoinColumn(name = "bien_immobilier_id")
    private BienImmobilier bienImmobilier;

    public BienImmobilier getBienImmobilier() {
        return bienImmobilier;
    }

    public void setBienImmobilier(BienImmobilier bienImmobilier) {
        this.bienImmobilier = bienImmobilier;
    }

    @Override
    public String toString() {
        return "Equipements{" +
                "bienImmobilier=" + bienImmobilier +
                '}';
    }
}
