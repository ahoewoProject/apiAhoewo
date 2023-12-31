package com.memoire.apiAhoewo.model.gestionDesComptes;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="demandes_certifications")
public class DemandeCertification extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_demande", nullable = false)
    private Date dateDemande;

    @Column(name = "document_justificatif", nullable = false)
    private String documentJustificatif;

    @Column(name = "carte_cfe")
    private String carteCfe;

    @Column(name = "statut_certification", nullable = false)
    private Integer statutDemande;

    @OneToOne
    @JoinColumn(name = "personne_id")
    private Personne personne;

    @OneToOne
    @JoinColumn(name = "agence_immobiliere_id")
    private AgenceImmobiliere agenceImmobiliere;

    public DemandeCertification(Long id, Date dateDemande, String documentJustificatif, Integer statutDemande) {
        this.id = id;
        this.dateDemande = dateDemande;
        this.documentJustificatif = documentJustificatif;
        this.statutDemande = statutDemande;
    }

    public DemandeCertification() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getDocumentJustificatif() {
        return documentJustificatif;
    }

    public void setDocumentJustificatif(String documentJustificatif) {
        this.documentJustificatif = documentJustificatif;
    }

    public String getCarteCfe() {
        return carteCfe;
    }

    public void setCarteCfe(String carteCfe) {
        this.carteCfe = carteCfe;
    }

    public Integer getStatutDemande() {
        return statutDemande;
    }

    public void setStatutDemande(Integer statutDemande) {
        this.statutDemande = statutDemande;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public AgenceImmobiliere getAgenceImmobiliere() {
        return agenceImmobiliere;
    }

    public void setAgenceImmobiliere(AgenceImmobiliere agenceImmobiliere) {
        this.agenceImmobiliere = agenceImmobiliere;
    }

    @Override
    public String toString() {
        return "DemandeCertification{" +
                "id=" + id +
                ", dateDemande=" + dateDemande +
                ", documentJustificatif='" + documentJustificatif + '\'' +
                ", statutDemande=" + statutDemande +
                ", personne=" + personne +
                '}';
    }
}
