package com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contrats_ventes")
public class ContratVente extends Contrat {
    @ManyToOne()
    @JoinColumn(name = "demande_achat_id", nullable = false)
    private DemandeAchat demandeAchat;

    @Column(name = "date_acquisition")
    private Date dateAcquisition;

    @Column(name = "prix_vente")
    private Double prixVente;

    @Column(name = "nombre_de_tranche")
    private Integer nombreDeTranche;

    @Column(name = "nom_prenom_temoin1_vendeur")
    private String nomPrenomTemoin1Vendeur;

    @Column(name = "contact_temoin1_vendeur")
    private String contactTemoin1Vendeur;

    @Column(name = "nom_prenom_temoin2_vendeur")
    private String nomPrenomTemoin2Vendeur;

    @Column(name = "contact_temoin2_vendeur")
    private String contactTemoin2Vendeur;

    @Column(name = "nom_prenom_temoin3_vendeur")
    private String nomPrenomTemoin3Vendeur;

    @Column(name = "contact_temoin3_vendeur")
    private String contactTemoin3Vendeur;

    @Column(name = "nom_prenom_temoin1_acheteur")
    private String nomPrenomTemoin1Acheteur;

    @Column(name = "contact_temoin1_acheteur")
    private String contactTemoin1Acheteur;

    @Column(name = "nom_prenom_temoin2_acheteur")
    private String nomPrenomTemoin2Acheteur;

    @Column(name = "contact_temoin2_acheteur")
    private String contactTemoin2Acheteur;

    @Column(name = "nom_prenom_temoin3_acheteur")
    private String nomPrenomTemoin3Acheteur;

    @Column(name = "contact_temoin3_acheteur")
    private String contactTemoin3Acheteur;
}
