package com.memoire.apiAhoewo.requestForm;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectationAgentAgenceForm {
    private String matricule;
    private AgenceImmobiliere agenceImmobiliere;
    private AgentImmobilier agentImmobilier;
}
