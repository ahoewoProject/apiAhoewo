package com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesComptes.Demarcheur;
import com.memoire.apiAhoewo.models.gestionDesComptes.Gerant;
import com.memoire.apiAhoewo.models.gestionDesComptes.Proprietaire;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contrats")
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.JOINED)
public class Contrat extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "code_contrat", unique = true, nullable = false)
    protected String codeContrat;

    @ManyToOne()
    @JoinColumn(name = "client_id", nullable = false)
    protected Client client;

    @ManyToOne()
    @JoinColumn(name = "proprietaire_id")
    protected Proprietaire proprietaire;

    @ManyToOne()
    @JoinColumn(name = "demarcheur_id")
    protected Demarcheur demarcheur;

    @ManyToOne()
    @JoinColumn(name = "gerant_id")
    protected Gerant gerant;

    @ManyToOne()
    @JoinColumn(name = "agence_immobiliere_id")
    protected AgenceImmobiliere agenceImmobiliere;

    @ManyToOne()
    @JoinColumn(name = "bien_immobilier_id", nullable = false)
    protected BienImmobilier bienImmobilier;

    @Column(name = "commission")
    protected Integer commission;

    @Column(name = "frais_de_visite")
    protected Double fraisDeVisite;

    @Column(name = "date_signature")
    protected Date dateSignature;

    @Column(name = "etat_contrat")
    protected String etatContrat;
}
