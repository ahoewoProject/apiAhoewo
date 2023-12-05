package com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "date_affectation")
    private Date dateAffectation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgenceImmobiliere getAgenceImmobiliere() {
        return agenceImmobiliere;
    }

    public void setAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        this.agenceImmobiliere = agenceImmobiliere;
    }

    public AgentImmobilier getAgentImmobilier() {
        return agentImmobilier;
    }

    public void setAgentImmobilier(AgentImmobilier agentImmobilier) {
        this.agentImmobilier = agentImmobilier;
    }

    public Date getDateAffectation() {
        return dateAffectation;
    }

    public void setDateAffectation(Date dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    @Override
    public String toString() {
        return "AffectationAgentAgence{" +
                "id=" + id +
                ", agenceImmobiliere=" + agenceImmobiliere +
                ", agentImmobilier=" + agentImmobilier +
                ", dateAffectation=" + dateAffectation +
                '}';
    }
}
