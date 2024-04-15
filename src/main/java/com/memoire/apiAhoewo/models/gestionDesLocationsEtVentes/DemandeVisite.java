package com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demandes_visites")
public class DemandeVisite extends Demande {
    @Column(name = "date_heure_visite")
    private LocalDateTime dateHeureVisite;
}
