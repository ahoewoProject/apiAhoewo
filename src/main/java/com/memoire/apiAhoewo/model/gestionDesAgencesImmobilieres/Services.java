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

    @Column(name = "code_service", nullable = false, unique = true)
    private String codeService;

    @Column(name = "nom_service")
    private String nomService;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "etat")
    protected Integer etat;

    public Services() {
    }

    public Services(Long id, String codeService, String nomService, String description, Integer etat) {
        this.id = id;
        this.codeService = codeService;
        this.nomService = nomService;
        this.description = description;
        this.etat = etat;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeService() {
        return codeService;
    }

    public void setCodeService(String codeService) {
        this.codeService = codeService;
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

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Services{" +
                "id=" + id +
                ", codeService='" + codeService + '\'' +
                ", nomService='" + nomService + '\'' +
                ", description='" + description + '\'' +
                ", etat=" + etat +
                '}';
    }
}
