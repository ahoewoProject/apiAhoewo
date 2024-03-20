package com.memoire.apiAhoewo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  EntiteDeBase {
    @Column(name = "creer_par", nullable = false)
    protected Long creerPar;

    @Column(name = "creer_le", nullable = false)
    protected Date creerLe;

    @Column(name = "modifier_par")
    protected Long modifierPar;

    @Column(name = "modifier_le")
    protected Date modifierLe;

    @Column(name = "statut")
    protected Boolean statut;
}
