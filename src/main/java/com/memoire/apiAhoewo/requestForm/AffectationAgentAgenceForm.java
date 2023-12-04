package com.memoire.apiAhoewo.requestForm;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;

public class AffectationAgentAgenceForm {
    private String matricule;
    private AgenceImmobiliere agenceImmobiliere;
    private AgentImmobilier agentImmobilier;

    public AffectationAgentAgenceForm(AgenceImmobiliere agenceImmobiliere, AgentImmobilier agentImmobilier) {
        this.agenceImmobiliere = agenceImmobiliere;
        this.agentImmobilier = agentImmobilier;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
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
}
