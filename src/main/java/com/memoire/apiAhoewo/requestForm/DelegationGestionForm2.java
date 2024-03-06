package com.memoire.apiAhoewo.requestForm;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelegationGestionForm2 {
    private TypeDeBien typeDeBien;
    private String matriculeProprietaire;
    private String matriculeBienImmo;
    private String categorie;
    private Pays pays;
    private Region region;
    private Ville ville;
    private Quartier quartier;
    private AgenceImmobiliere agenceImmobiliere;
    private String adresse;
    private Integer surface;
    private String description;
}
