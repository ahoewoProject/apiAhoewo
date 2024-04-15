package com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.EntiteDeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "types_de_bien")
public class TypeDeBien extends EntiteDeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "designation", unique = true, nullable = false)
    private String designation;

    @Column(name = "etat")
    protected Boolean etat;

    public boolean estNull() {
        return id == null || code == null || designation == null;
    }
}
