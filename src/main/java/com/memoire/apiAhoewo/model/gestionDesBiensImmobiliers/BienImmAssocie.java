package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "biens_immobiliers_associes")
@DiscriminatorValue(value = "BIEN_IMMOBILIER_ASSOCIE")
public class BienImmAssocie extends BienImmobilier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "bien_immobilier_id")
    private BienImmobilier bienImmobilier;
}
