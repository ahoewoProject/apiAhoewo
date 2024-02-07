package com.memoire.apiAhoewo.requestForm;

public class ServiceNonTrouveForm {
    private String nomAgence;
    private String nomDuService;
    private String descriptionDuService;

    public ServiceNonTrouveForm(String nomAgence, String nomDuService, String descriptionDuService) {
        this.nomAgence = nomAgence;
        this.nomDuService = nomDuService;
        this.descriptionDuService = descriptionDuService;
    }

    public String getNomAgence() {
        return nomAgence;
    }

    public void setNomAgence(String nomAgence) {
        this.nomAgence = nomAgence;
    }

    public String getNomDuService() {
        return nomDuService;
    }

    public void setNomDuService(String nomDuService) {
        this.nomDuService = nomDuService;
    }

    public String getDescriptionDuService() {
        return descriptionDuService;
    }

    public void setDescriptionDuService(String descriptionDuService) {
        this.descriptionDuService = descriptionDuService;
    }
}
