package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "regions")
public class Region extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_region", unique = true, nullable = false)
    private String codeRegion;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @ManyToOne()
    @JoinColumn(name = "pays_id")
    private Pays pays;

    @Column(name = "etat")
    private Boolean etat;
}
