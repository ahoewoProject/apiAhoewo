package com.memoire.apiAhoewo.requestForm;

public class MotifRejetServiceForm {
    Long id;
    String motifRejet;

    public MotifRejetServiceForm() {
    }

    public MotifRejetServiceForm(Long id, String motifRejet) {
        this.id = id;
        this.motifRejet = motifRejet;
    }

    public Long getId() {
        return id;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }
}
