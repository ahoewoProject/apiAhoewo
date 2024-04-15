package com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
