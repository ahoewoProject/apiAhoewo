package com.memoire.apiAhoewo.model;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class  EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creer_par", nullable = false)
    protected Long creerPar;

    @Column(name = "creer_le", nullable = false)
    protected Date creerLe;

    @Column(name = "modifier_par")
    protected Long modifierPar;

    @Column(name = "modifier_le")
    protected Date modifierLe;

    @Column(name = "statut")
    protected Boolean statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreerPar() {
        return creerPar;
    }

    public void setCreerPar(Long creerPar) {
        this.creerPar = creerPar;
    }

    public Date getCreerLe() {
        return creerLe;
    }

    public void setCreerLe(Date creerLe) {
        this.creerLe = creerLe;
    }

    public Long getModifierPar() {
        return modifierPar;
    }

    public void setModifierPar(Long modifierPar) {
        this.modifierPar = modifierPar;
    }

    public Date getModifierLe() {
        return modifierLe;
    }

    public void setModifierLe(Date modifierLe) {
        this.modifierLe = modifierLe;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }
}
