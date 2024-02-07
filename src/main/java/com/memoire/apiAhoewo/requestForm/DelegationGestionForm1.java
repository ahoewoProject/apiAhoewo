package com.memoire.apiAhoewo.requestForm;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;

import java.util.Date;

public class DelegationGestionForm1 {
    private String matricule;
    private String codeAgence;
    private BienImmobilier bienImmobilier;
    private Date dateDelegation;
    private Boolean statutDelegation;

    public DelegationGestionForm1(String matricule, String codeAgence, BienImmobilier bienImmobilier,
                                  Date dateDelegation, Boolean statutDelegation) {
        this.matricule = matricule;
        this.codeAgence = codeAgence;
        this.bienImmobilier = bienImmobilier;
        this.dateDelegation = dateDelegation;
        this.statutDelegation = statutDelegation;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getCodeAgence() {
        return codeAgence;
    }

    public void setCodeAgence(String codeAgence) {
        this.codeAgence = codeAgence;
    }

    public BienImmobilier getBienImmobilier() {
        return bienImmobilier;
    }

    public void setBienImmobilier(BienImmobilier bienImmobilier) {
        this.bienImmobilier = bienImmobilier;
    }

    public Date getDateDelegation() {
        return dateDelegation;
    }

    public void setDateDelegation(Date dateDelegation) {
        this.dateDelegation = dateDelegation;
    }

    public Boolean getStatutDelegation() {
        return statutDelegation;
    }

    public void setStatutDelegation(Boolean statutDelegation) {
        this.statutDelegation = statutDelegation;
    }
}
