package com.memoire.apiAhoewo.dto;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.ResponsableAgenceImmobiliere;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectationResponsableAgenceForm {
    private String matricule;

    private AgenceImmobiliere agenceImmobiliere;

    private ResponsableAgenceImmobiliere responsable;
}
