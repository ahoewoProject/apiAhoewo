package com.memoire.apiAhoewo.models.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import com.memoire.apiAhoewo.models.gestionDesPublications.Publication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Demande extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "code_demande", unique = true, nullable = false)
    protected String codeDemande;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    protected Client client;

    @ManyToOne()
    @JoinColumn(name = "publication_id", nullable = false)
    protected Publication publication;

    @Column(name = "date_demande")
    protected Date dateDemande;

    @Column(name = "etat_demande")
    protected Integer etatDemande;
}
