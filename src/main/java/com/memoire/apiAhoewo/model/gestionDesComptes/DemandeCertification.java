package com.memoire.apiAhoewo.model.gestionDesComptes;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="demandes_certifications")
public class DemandeCertification extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_demande", nullable = false)
    private Date dateDemande;

    @Column(name = "document_justificatif", nullable = false)
    private String documentJustificatif;

    @Column(name = "carte_cfe")
    private String carteCfe;

    @Column(name = "statut_certification", nullable = false)
    private Integer statutDemande;

    @OneToOne
    @JoinColumn(name = "personne_id")
    private Personne personne;

    @OneToOne
    @JoinColumn(name = "agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;
}
