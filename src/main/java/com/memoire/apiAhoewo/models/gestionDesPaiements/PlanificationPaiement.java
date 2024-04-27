package com.memoire.apiAhoewo.models.gestionDesPaiements;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes.Contrat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "planifications_paiements")
public class PlanificationPaiement extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_planification", unique = true)
    private String codePlanification;

    @Column(name = "type_planification")
    private String typePlanification;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "montant")
    private Double montantDu;

    @Column(name = "montant_paye")
    private Double montantPaye;

    @Column(name = "reste_paye")
    private Double restePaye;

    @Column(name = "date_planifiee")
    private Date datePlanifiee;

    @ManyToOne
    @JoinColumn(name = "contrat_id")
    private Contrat contrat;

    @Column(name = "statut_planification")
    private String statutPlanification;
}
