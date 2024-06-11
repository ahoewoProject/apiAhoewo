package com.memoire.apiAhoewo.controllers;

import com.memoire.apiAhoewo.models.Motif;
import com.memoire.apiAhoewo.services.MotifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MotifController {
    @Autowired
    private MotifService motifService;

    @RequestMapping(value = "/motifs/{code}/{creerPar}", method = RequestMethod.GET)
    public List<Motif> getMotifsByCodeAndCreerPar(@PathVariable String code, @PathVariable Long creerPar) {

        List<Motif> motifList = new ArrayList<>();
        try {
            motifList = this.motifService.getMotifsByCodeAndCreerPar(code, creerPar);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return motifList;
    }

    @RequestMapping(value = "/motif/{code}", method = RequestMethod.GET)
    public Motif findByCode(@PathVariable String code) {

        Motif motif = new Motif();
        try {
            motif = this.motifService.findByCode(code);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return motif;
    }
}
