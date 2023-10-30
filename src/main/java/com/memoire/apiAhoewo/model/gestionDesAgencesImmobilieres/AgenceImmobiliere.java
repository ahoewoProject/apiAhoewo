package com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;

import javax.persistence.*;

@Entity
@Table(name="agences_immobilieres")
public class AgenceImmobiliere extends EntiteDeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "logo_agence")
    private String logoAgence;

    @Column(name = "nom_agence", nullable = false)
    private String nomAgence;

    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "adresse_email", nullable = false)
    private String adresseEmail;

    @Column(name = "heure_ouverture", nullable = false)
    private String heureOuverture;

    @Column(name = "heure_fermeture", nullable = false)
    private String heureFermeture;

    @Column(name = "est_certifie", nullable = false)
    private Boolean estCertifie;

    @Column(name = "etat_agence", nullable = false)
    private Boolean etatAgence;

    @ManyToOne
    @JoinColumn(name = "agent_immobilier_id")
    private AgentImmobilier agentImmobilier;

    public AgenceImmobiliere() {
    }

    public AgenceImmobiliere(Long id, String logoAgence, String nomAgence, String adresse, String telephone, String adresseEmail, String heureOuverture, String heureFermeture, Boolean estCertifie, Boolean etatAgence, AgentImmobilier agentImmobilier) {
        this.id = id;
        this.logoAgence = logoAgence;
        this.nomAgence = nomAgence;
        this.adresse = adresse;
        this.telephone = telephone;
        this.adresseEmail = adresseEmail;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
        this.estCertifie = estCertifie;
        this.etatAgence = etatAgence;
        this.agentImmobilier = agentImmobilier;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getLogoAgence() {
        return logoAgence;
    }

    public void setLogoAgence(String logoAgence) {
        this.logoAgence = logoAgence;
    }

    public String getNomAgence() {
        return nomAgence;
    }

    public void setNomAgence(String nomAgence) {
        this.nomAgence = nomAgence;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHeureOuverture() {
        return heureOuverture;
    }

    public void setHeureOuverture(String heureOuverture) {
        this.heureOuverture = heureOuverture;
    }

    public String getHeureFermeture() {
        return heureFermeture;
    }

    public void setHeureFermeture(String heureFermeture) {
        this.heureFermeture = heureFermeture;
    }

    public Boolean getEstCertifie() {
        return estCertifie;
    }

    public void setEstCertifie(Boolean estCertifie) {
        this.estCertifie = estCertifie;
    }

    public Boolean getEtatAgence() {
        return etatAgence;
    }

    public void setEtatAgence(Boolean etatAgence) {
        this.etatAgence = etatAgence;
    }

    public AgentImmobilier getAgentImmobilier() {
        return agentImmobilier;
    }

    public void setAgentImmobilier(AgentImmobilier agentImmobilier) {
        this.agentImmobilier = agentImmobilier;
    }

    public String getAdresseEmail() {
        return adresseEmail;
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    @Override
    public String toString() {
        return "AgenceImmobiliere{" +
                "id=" + id +
                ", nomAgence='" + nomAgence + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", adresseEmail='" + adresseEmail + '\'' +
                ", heureOuverture='" + heureOuverture + '\'' +
                ", heureFermeture='" + heureFermeture + '\'' +
                ", estCertifie=" + estCertifie +
                ", etatAgence=" + etatAgence +
                ", agentImmobilier=" + agentImmobilier +
                '}';
    }
}
