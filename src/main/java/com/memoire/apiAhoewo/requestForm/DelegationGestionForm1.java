package com.memoire.apiAhoewo.requestForm;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelegationGestionForm1 {
    private String matricule;
    private String codeAgence;
    private BienImmobilier bienImmobilier;
    private Date dateDelegation;
    private Boolean statutDelegation;
}
