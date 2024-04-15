package com.memoire.apiAhoewo.models.gestionDesPublications;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publications")
public class Publication extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_publication", unique = true, nullable = false)
    private String codePublication;

    @Column(name = "type_de_transaction", nullable = false)
    private String typeDeTransaction;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "date_publication", nullable = false)
    private Date datePublication;

    @ManyToOne()
    @JoinColumn(name = "bien_immobilier_id", nullable = false)
    private BienImmobilier bienImmobilier;

    @Column(name ="prixDuBien")
    private Double prixDuBien;

    @Column(name = "frais_de_visite")
    private Double fraisDeVisite;

    @Column(name = "avance")
    private Integer avance;

    @Column(name = "caution")
    private Integer caution;

    @Column(name = "commission")
    private Integer commission;

    @ManyToOne()
    @JoinColumn(name = "personne_id")
    private Personne personne;

    @ManyToOne()
    @JoinColumn(name = "agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;

    @Column(name = "etat")
    private Boolean etat;
}
