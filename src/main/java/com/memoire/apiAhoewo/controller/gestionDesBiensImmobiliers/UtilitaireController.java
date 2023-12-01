package com.memoire.apiAhoewo.controller.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.Utilitaire;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.UtilitaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UtilitaireController {
    @Autowired
    private UtilitaireService utilitaireService;

    @RequestMapping(value = "/utilitaire/{idBienImmobilier}", method = RequestMethod.GET)
    public Utilitaire getUtilitaireByBienImmobilier(@PathVariable Long idBienImmobilier) {

        Utilitaire utilitaire = new Utilitaire();
        try {
            utilitaire = this.utilitaireService.getByBienImmobilier(idBienImmobilier);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return utilitaire;
    }
}
