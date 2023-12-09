package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Administrateur;
import com.memoire.apiAhoewo.model.gestionDesComptes.Demarcheur;
import com.memoire.apiAhoewo.service.gestionDesComptes.DemarcheurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DemarcheurController {
    @Autowired
    private DemarcheurService demarcheurService;

    @RequestMapping(value = "/demarcheurs", method = RequestMethod.GET)
    public List<Demarcheur> getAll() {

        List<Demarcheur> demarcheurList = new ArrayList<>();
        try {
            demarcheurList = this.demarcheurService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return demarcheurList;
    }

    @RequestMapping(value = "/demarcheurs/pagines", method = RequestMethod.GET)
    public Page<Demarcheur> getDemarcheurs(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.demarcheurService.getDemarcheurs(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des demarcheurs.", e);
        }
    }

    @RequestMapping(value = "/demarcheur/{id}", method = RequestMethod.GET)
    public Demarcheur findById(@PathVariable Long id) {

        Demarcheur demarcheur = new Demarcheur();
        try {
            demarcheur = this.demarcheurService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return demarcheur;
    }

    @RequestMapping(value = "/demarcheur/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerDemarcheur(@PathVariable Long id) {
        this.demarcheurService.deleteById(id);
    }

    @RequestMapping(value = "/count/demarcheurs", method = RequestMethod.GET)
    public int nombreDeDemarcheurs(){
        int nombres = this.demarcheurService.countDemarcheurs();
        return nombres;
    }
}
