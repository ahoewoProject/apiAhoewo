package com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Pays;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Quartier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Region;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Ville;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="agences_immobilieres")
public class AgenceImmobiliere extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "logo_agence")
    private String logoAgence;

    @Column(name = "code_agence")
    private String codeAgence;

    @Column(name = "nom_agence", nullable = false)
    private String nomAgence;

    @ManyToOne
    @JoinColumn(name = "quartier_id")
    protected Quartier quartier;

    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "adresse_email", nullable = false)
    private String adresseEmail;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "heure_ouverture", nullable = false)
    private String heureOuverture;

    @Column(name = "heure_fermeture", nullable = false)
    private String heureFermeture;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    protected String description;

    @Column(name = "est_certifie", nullable = false)
    private Boolean estCertifie;

    @Column(name = "etat_agence", nullable = false)
    private Boolean etatAgence;
}
