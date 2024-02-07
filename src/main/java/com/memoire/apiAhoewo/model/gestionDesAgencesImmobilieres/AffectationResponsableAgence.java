package com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "date_debut", nullable = false)
    private Date dateDebut;

    @Column(name = "date_fin")
    private Date dateFin;

    @Column(name = "actif", nullable = false)
    private Boolean actif;

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

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "AffectationResponsableAgence{" +
                "id=" + id +
                ", responsableAgenceImmobiliere=" + responsableAgenceImmobiliere +
                ", agenceImmobiliere=" + agenceImmobiliere +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", actif=" + actif +
                '}';
    }
}
