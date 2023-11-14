package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.service.gestionDesComptes.ResponsableAgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ResponsableAgenceImmobiliereController {
    @Autowired
    private ResponsableAgenceImmobiliereService responsableAgenceImmobiliereService;

    @RequestMapping(value = "/responsables-agence-immobiliere", method = RequestMethod.GET)
    public List<ResponsableAgenceImmobiliere> getAll() {

        List<ResponsableAgenceImmobiliere> responsableAgenceImmobilieres = new ArrayList<>();
        try {
            responsableAgenceImmobilieres = this.responsableAgenceImmobiliereService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return responsableAgenceImmobilieres;
    }

    @RequestMapping(value = "/responsable-agence-immobiliere/{id}", method = RequestMethod.GET)
    public ResponsableAgenceImmobiliere findById(@PathVariable Long id) {

        ResponsableAgenceImmobiliere responsableAgenceImmobiliere = new ResponsableAgenceImmobiliere();
        try {
            responsableAgenceImmobiliere = this.responsableAgenceImmobiliereService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return responsableAgenceImmobiliere;
    }

    @RequestMapping(value = "/responsable-agence-immobiliere/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerAgenceImmobiliere(@PathVariable Long id) {
        this.responsableAgenceImmobiliereService.deleteById(id);
    }

    @RequestMapping(value = "/count/responsables-agence-immobiliere", method = RequestMethod.GET)
    public int nombreDeResponsablesAgenceImmobiliere(){
        int nombres = this.responsableAgenceImmobiliereService.countResponsablesAgenceImmobiliere();
        return nombres;
    }
}
