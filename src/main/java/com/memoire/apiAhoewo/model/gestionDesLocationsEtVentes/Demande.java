package com.memoire.apiAhoewo.model.gestionDesLocationsEtVentes;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import com.memoire.apiAhoewo.model.gestionDesComptes.Client;
import com.memoire.apiAhoewo.model.gestionDesPublications.Publication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Demande extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    protected Client client;

    @ManyToOne()
    @JoinColumn(name = "publication_id", nullable = false)
    protected Publication publication;

    @Column(name = "etat_demande")
    protected Boolean etatDemande;
}
