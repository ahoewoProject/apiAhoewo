package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Caracteristiques;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.CaracteristiquesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CaracteristiquesController {
    @Autowired
    private CaracteristiquesService caracteristiquesService;

    @RequestMapping(value = "/caracteristiques/bien-immobilier/{idBienImmobilier}", method = RequestMethod.GET)
    public Caracteristiques getCaracteristiquesOfBienImmobilier(@PathVariable Long idBienImmobilier) {

        Caracteristiques caracteristique = new Caracteristiques();
        try {
            caracteristique = this.caracteristiquesService.findByBienImmobilier(idBienImmobilier);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return caracteristique;
    }
}
