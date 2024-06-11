package com.memoire.apiAhoewo.models.gestionDesPaiements;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comptes_paiements")
public class ComptePaiement extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_compte_paiement", unique = true)
    private String codeComptePaiement;

    @Column(name = "type")
    private String type;

    @Column(name = "contact")
    private String contact;

    @ManyToOne
    @JoinColumn(name = "personne_id")
    private Personne personne;

    @ManyToOne
    @JoinColumn(name = "agence_id")
    private AgenceImmobiliere agenceImmobiliere;

    @Column(name = "etat_compte_paiement")
    private Boolean etatComptePaiement;
}
