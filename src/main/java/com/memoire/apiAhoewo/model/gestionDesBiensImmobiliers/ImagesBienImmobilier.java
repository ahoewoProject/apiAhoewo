package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images_biens_immobiliers")
public class ImagesBienImmobilier extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_image", nullable = false)
    private String nomImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bien_immobilier_id")
    private BienImmobilier bienImmobilier;
}
