package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Confort;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.ConfortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConfortController {
    @Autowired
    private ConfortService confortService;

    @RequestMapping(value = "/confort/{idBienImmobilier}", method = RequestMethod.GET)
    public Confort getConfortByBienImmobilier(@PathVariable Long idBienImmobilier) {

        Confort confort = new Confort();
        try {
            confort = this.confortService.getByBienImmobilier(idBienImmobilier);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return confort;
    }
}
