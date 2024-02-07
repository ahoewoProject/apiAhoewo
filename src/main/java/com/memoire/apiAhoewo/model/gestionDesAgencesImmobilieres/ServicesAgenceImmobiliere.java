package com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@Entity
@Table(name="services_agences_immobilieres")
public class ServicesAgenceImmobiliere extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "services_id")
    private Services services;

    @ManyToOne()
    @JoinColumn(name = "agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;

    @Column(name = "etat")
    private Integer etat;

    public ServicesAgenceImmobiliere() {
    }

    public ServicesAgenceImmobiliere(Long id, Services services, AgenceImmobiliere agenceImmobiliere, Integer etat) {
        this.id = id;
        this.services = services;
        this.agenceImmobiliere = agenceImmobiliere;
        this.etat = etat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public AgenceImmobiliere getAgenceImmobiliere() {
        return agenceImmobiliere;
    }

    public void setAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        this.agenceImmobiliere = agenceImmobiliere;
    }

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "ServicesAgenceImmobiliere{" +
                "id=" + id +
                ", services=" + services +
                ", agenceImmobiliere=" + agenceImmobiliere +
                ", etat=" + etat +
                '}';
    }
}
