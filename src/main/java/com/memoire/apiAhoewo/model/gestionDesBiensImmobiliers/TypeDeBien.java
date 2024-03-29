package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@Entity
@Table(name = "types_de_bien")
public class TypeDeBien extends EntiteDeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "designation", unique = true, nullable = false)
    private String designation;

    @Column(name = "etat")
    protected Boolean etat;

    public TypeDeBien() {
    }

    public TypeDeBien(Long id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    public TypeDeBien(Long id, String code, String designation) {
        this.id = id;
        this.code = code;
        this.designation = designation;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "TypeDeBien{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", designation='" + designation + '\'' +
                ", etat=" + etat +
                '}';
    }
}
