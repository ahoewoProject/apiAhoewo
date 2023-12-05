package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "delegations_gestions")
public class DelegationGestion extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gestionnaire_id")
    private Personne gestionnaire;

    @ManyToOne
    @JoinColumn(name = "agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;

    @ManyToOne
    @JoinColumn(name = "bien_immobilier_id")
    private BienImmobilier bienImmobilier;

    /*@ManyToMany
    @JoinTable(
            name = "delegations_gestions_biens",
            joinColumns = @JoinColumn(name = "delegation_gestion_id"),
            inverseJoinColumns = @JoinColumn(name = "bien_immobilier_id")
    )
    private List<BienImmobilier> biensImmobiliers;*/

    @Column(name = "date_delegation")
    private Date dateDelegation;

    @Column(name = "statut_delegation")
    private Integer statutDelegation;

    public DelegationGestion() {
    }

    public DelegationGestion(Long id, Personne gestionnaire, BienImmobilier bienImmobilier, Date dateDelegation, Integer statutDelegation) {
        this.id = id;
        this.gestionnaire = gestionnaire;
        this.bienImmobilier = bienImmobilier;
        this.dateDelegation = dateDelegation;
        this.statutDelegation = statutDelegation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Personne getGestionnaire() {
        return gestionnaire;
    }

    public void setGestionnaire(Personne gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public AgenceImmobiliere getAgenceImmobiliere() {
        return agenceImmobiliere;
    }

    public void setAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        this.agenceImmobiliere = agenceImmobiliere;
    }

    public BienImmobilier getBienImmobilier() {
        return bienImmobilier;
    }

    public void setBienImmobilier(BienImmobilier bienImmobilier) {
        this.bienImmobilier = bienImmobilier;
    }

    public Date getDateDelegation() {
        return dateDelegation;
    }

    public void setDateDelegation(Date dateDelegation) {
        this.dateDelegation = dateDelegation;
    }

    public Integer getStatutDelegation() {
        return statutDelegation;
    }

    public void setStatutDelegation(Integer statutDelegation) {
        this.statutDelegation = statutDelegation;
    }

    @Override
    public String toString() {
        return "DelegationGestion{" +
                "id=" + id +
                ", gestionnaire=" + gestionnaire +
                ", agenceImmobiliere=" + agenceImmobiliere +
                ", bienImmobilier=" + bienImmobilier +
                ", dateDelegation=" + dateDelegation +
                ", statutDelegation=" + statutDelegation +
                '}';
    }
}
