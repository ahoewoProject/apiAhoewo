package com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes;

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
@Table(name = "demandes_locations")
public class DemandeLocation extends Demande {
    @Column(name = "prix_de_location")
    private Double prixDeLocation;

    @Column(name = "avance")
    private Integer avance;

    @Column(name = "caution")
    private Integer caution;
}
