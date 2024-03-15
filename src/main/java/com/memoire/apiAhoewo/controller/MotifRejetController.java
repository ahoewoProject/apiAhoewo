package com.memoire.apiAhoewo.controller;

import com.memoire.apiAhoewo.model.MotifRejet;
import com.memoire.apiAhoewo.service.MotifRejetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MotifRejetController {
    @Autowired
    private MotifRejetService motifRejetService;

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
