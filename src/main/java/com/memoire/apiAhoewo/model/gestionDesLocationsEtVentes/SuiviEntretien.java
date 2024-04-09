package com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suivis_entretiens")
public class SuiviEntretien extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_suivi_entretien", unique = true, nullable = false)
    private String codeSuiviEntretien;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description", columnDefinition = "TEXT")
    protected String description;

    @ManyToOne
    @JoinColumn(name = "contrat_location_id")
    private ContratLocation contratLocation;

    @Column(name = "date_prevue")
    private LocalDate datePrevue;

    @Column(name = "etat_suivi_entretien")
    private String etatSuiviEntretien;
}
