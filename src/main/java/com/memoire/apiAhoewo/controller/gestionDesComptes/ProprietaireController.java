package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Proprietaire;
import com.memoire.apiAhoewo.service.gestionDesComptes.ProprietaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProprietaireController {
    @Autowired
    private ProprietaireService proprietaireService;

    @RequestMapping(value = "/proprietaires", method = RequestMethod.GET)
    public List<Proprietaire> getAll() {

        List<Proprietaire> proprietaireList = new ArrayList<>();
        try {
            proprietaireList = this.proprietaireService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return proprietaireList;
    }

    @RequestMapping(value = "/proprietaire/{id}", method = RequestMethod.GET)
    public Proprietaire findById(@PathVariable Long id) {

        Proprietaire proprietaire = new Proprietaire();
        try {
            proprietaire = this.proprietaireService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return proprietaire;
    }

    @RequestMapping(value = "/proprietaire/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerProprietaire(@PathVariable Long id) {
        this.proprietaireService.deleteById(id);
    }

    @RequestMapping(value = "/count/proprietaires", method = RequestMethod.GET)
    public int nombreDeProprietaires(){
        int nombres = this.proprietaireService.countProprietaires();
        return nombres;
    }
}
