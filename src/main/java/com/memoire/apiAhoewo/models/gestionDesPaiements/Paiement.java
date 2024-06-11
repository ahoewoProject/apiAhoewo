package com.memoire.apiAhoewo.models.gestionDesPaiements;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paiements")
public class Paiement extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_paiement", unique = true)
    private String codePaiement;

    @Column(name = "mode_paiement")
    private String modePaiement;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "date_paiement")
    private Date datePaiement;

    @Column(name = "preuve")
    private String preuve;

    @Column(name = "payout_id")
    private String payoutBatchId;

    @OneToOne
    @JoinColumn(name = "planification_id")
    private PlanificationPaiement planificationPaiement;

    @Column(name = "statut_paiement")
    private String statutPaiement;
}
