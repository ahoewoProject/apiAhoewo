package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Divertissement;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.DivertissementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DivertissementController {
    @Autowired
    private DivertissementService divertissementService;

    @RequestMapping(value = "/divertissement/{idBienImmobilier}", method = RequestMethod.GET)
    public Divertissement getDivertissementByBienImmobilier(@PathVariable Long idBienImmobilier) {

        Divertissement divertissement = new Divertissement();
        try {
            divertissement = this.divertissementService.getByBienImmobilier(idBienImmobilier);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return divertissement;
    }
}
