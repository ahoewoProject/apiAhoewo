package com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.EntiteDeBase;

import javax.persistence.*;

@Entity
@Table(name = "images_bien_immobilier")
public class ImagesBienImmobilier extends EntiteDeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_image", nullable = false)
    private String nomImage;

    @ManyToOne()
    @JoinColumn(name = "bien_immobilier_id")
    private BienImmobilier bienImmobilier;

    public ImagesBienImmobilier() {
    }

    public ImagesBienImmobilier(Long id, String nomImage) {
        this.id = id;
        this.nomImage = nomImage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNomImage() {
        return nomImage;
    }

    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }

    public BienImmobilier getBienImmobilier() {
        return bienImmobilier;
    }

    public void setBienImmobilier(BienImmobilier bienImmobilier) {
        this.bienImmobilier = bienImmobilier;
    }

    @Override
    public String toString() {
        return "ImagesBienImmobilier{" +
                "id=" + id +
                ", nomImage='" + nomImage + '\'' +
                ", bienImmobilier=" + bienImmobilier +
                '}';
    }
}
