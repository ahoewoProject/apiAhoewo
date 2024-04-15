package com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
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
@Table(name = "delegations_gestions")
public class DelegationGestion extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_delegation_gestion")
    private String codeDelegationGestion;

    @ManyToOne
    @JoinColumn(name = "gestionnaire_id")
    private Personne gestionnaire;

    @ManyToOne
    @JoinColumn(name = "agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;

    @ManyToOne
    @JoinColumn(name = "bien_immobilier_id")
    private BienImmobilier bienImmobilier;

    @Column(name = "date_delegation")
    private Date dateDelegation;

    @Column(name = "statut_delegation")
    private Integer statutDelegation;

    @Column(name = "etat_delegation")
    private Boolean etatDelegation;

    @Column(name = "portee_gestion")
    private Boolean porteeGestion;
}
