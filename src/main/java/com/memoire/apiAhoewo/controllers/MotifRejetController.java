package com.memoire.apiAhoewo.controllers;

import com.memoire.apiAhoewo.models.MotifRejet;
import com.memoire.apiAhoewo.services.MotifRejetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MotifRejetController {
    @Autowired
    private MotifRejetService motifRejetService;

    @RequestMapping(value = "/motifs-rejets/{code}/{creerPar}", method = RequestMethod.GET)
    public List<MotifRejet> getMotifsByCodeAndCreerPar(@PathVariable String code, @PathVariable Long creerPar) {

        List<MotifRejet> motifRejets = null;
        try {
            motifRejets = this.motifRejetService.getMotifsByCodeAndCreerPar(code, creerPar);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return motifRejets;
    }

    @RequestMapping(value = "/motif-rejet/{code}", method = RequestMethod.GET)
    public MotifRejet findByCode(@PathVariable String code) {

        MotifRejet motifRejet = new MotifRejet();
        try {
            motifRejet = this.motifRejetService.findByCode(code);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return motifRejet;
    }
}
