package com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@Entity
@Table(name="services")
public class Services extends EntiteDeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_service")
    private String nomService;

    @Column(name = "description")
    private String description;

    @ManyToOne()
    @JoinColumn(name="agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;

    public Services() {
    }

    public Services(Long id, String nomService, String description, AgenceImmobiliere agenceImmobiliere) {
        this.id = id;
        this.nomService = nomService;
        this.description = description;
        this.agenceImmobiliere = agenceImmobiliere;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AgenceImmobiliere getAgenceImmobiliere() {
        return agenceImmobiliere;
    }

    public void setAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        this.agenceImmobiliere = agenceImmobiliere;
    }

    @Override
    public String toString() {
        return "Services{" +
                "id=" + id +
                ", nomService='" + nomService + '\'' +
                ", description='" + description + '\'' +
                ", agenceImmobiliere=" + agenceImmobiliere +
                '}';
    }
}
