package com.memoire.apiAhoewo.model.gestionDesComptes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="agents_immobiliers")
@DiscriminatorValue("AgentImmobilier")
public class AgentImmobilier extends Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;
}
