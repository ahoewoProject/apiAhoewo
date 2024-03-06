package com.memoire.apiAhoewo.requestForm;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
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
