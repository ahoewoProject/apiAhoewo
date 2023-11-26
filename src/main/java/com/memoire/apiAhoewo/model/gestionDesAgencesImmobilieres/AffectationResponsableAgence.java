package com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;

import javax.persistence.*;

@Entity
@Table(name="affectations_responsables_agences")
public class AffectationResponsableAgence extends EntiteDeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "responsable_id")
    private ResponsableAgenceImmobiliere responsableAgenceImmobiliere;

    @ManyToOne()
    @JoinColumn(name = "agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResponsableAgenceImmobiliere getResponsableAgenceImmobiliere() {
        return responsableAgenceImmobiliere;
    }

    public void setResponsableAgenceImmobiliere(ResponsableAgenceImmobiliere responsableAgenceImmobiliere) {
        this.responsableAgenceImmobiliere = responsableAgenceImmobiliere;
    }

    public AgenceImmobiliere getAgenceImmobiliere() {
        return agenceImmobiliere;
    }

    public void setAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        this.agenceImmobiliere = agenceImmobiliere;
    }
}
