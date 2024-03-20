package com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demandes_achats")
public class DemandeAchat extends Demande {
    @Column(name = "prix_achat")
    private Double prixAchat;

    @Column(name = "nombre_de_tranche")
    private Integer nombreDeTranche;
}
