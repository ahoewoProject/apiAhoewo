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

    @Column(name = "designation", unique = true, nullable = false)
    private String designation;

    public TypeDeBien() {
    }

    public TypeDeBien(Long id, String designation) {
        this.id = id;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "TypeDeBien{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                '}';
    }
}
