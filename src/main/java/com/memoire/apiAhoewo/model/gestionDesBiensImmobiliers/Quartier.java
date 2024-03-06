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
@Table(name = "quartiers")
public class Quartier extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_quartier", unique = true,nullable = false)
    private String codeQuartier;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @ManyToOne()
    @JoinColumn(name = "ville_id")
    private Ville ville;

    @Column(name = "etat")
    private Boolean etat;

    public boolean estNull() {
        return id == null || codeQuartier == null || libelle == null;
    }
}
