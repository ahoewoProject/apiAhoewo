package com.memoire.apiAhoewo.requestForm;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;

public class AffectationResponsableAgenceForm {
    private String matricule;

    private AgenceImmobiliere agenceImmobiliere;

    private ResponsableAgenceImmobiliere responsable;

    public AffectationResponsableAgenceForm(String matricule, AgenceImmobiliere agenceImmobiliere, ResponsableAgenceImmobiliere responsable) {
        this.matricule = matricule;
        this.agenceImmobiliere = agenceImmobiliere;
        this.responsable = responsable;
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

    public ResponsableAgenceImmobiliere getResponsable() {
        return responsable;
    }

    public void setResponsable(ResponsableAgenceImmobiliere responsable) {
        this.responsable = responsable;
    }
}
