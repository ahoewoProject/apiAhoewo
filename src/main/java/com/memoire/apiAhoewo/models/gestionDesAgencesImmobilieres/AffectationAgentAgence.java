package com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="affectations_agents_agences")
public class AffectationAgentAgence extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;

    @ManyToOne()
    @JoinColumn(name = "agent_immobilier_id")
    private AgentImmobilier agentImmobilier;

    @Column(name = "date_affectation", nullable = false)
    private Date dateAffectation;

    @Column(name = "date_fin")
    private Date dateFin;

    @Column(name = "actif", nullable = false)
    private Boolean actif;
}
