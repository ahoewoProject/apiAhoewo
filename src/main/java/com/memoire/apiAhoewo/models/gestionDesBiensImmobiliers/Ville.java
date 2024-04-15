package com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "villes")
public class Ville extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_vile", unique = true, nullable = false)
    private String codeVille;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @ManyToOne()
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "etat")
    private Boolean etat;
}
