package com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contrats_locations")
@DiscriminatorValue("CONTRAT_LOCATION")
public class ContratLocation extends Contrat {
    @ManyToOne()
    @JoinColumn(name = "demande_location_id", nullable = false)
    private DemandeLocation demandeLocation;

    @Column(name = "type_contrat")
    private String typeContrat;

    @Column(name = "debut_paiement")
    private Integer debutPaiement;

    @Column(name = "jour_supplement_paiement")
    private Integer jourSupplementPaiement;

    @Column(name = "loyer")
    private Double loyer;

    @Column(name = "avance")
    private Integer avance;

    @Column(name = "caution")
    private Integer caution;

    @Column(name = "date_debut")
    private Date dateDebut;

    @Column(name = "date_fin")
    private Date dateFin;
}
