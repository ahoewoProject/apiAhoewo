package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Demarcheur;
import com.memoire.apiAhoewo.model.gestionDesComptes.Proprietaire;
import com.memoire.apiAhoewo.service.gestionDesComptes.ProprietaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/proprietaires/pagines", method = RequestMethod.GET)
    public Page<Proprietaire> getProprietaires(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.proprietaireService.getProprietaires(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des proprietaires.", e);
        }

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
