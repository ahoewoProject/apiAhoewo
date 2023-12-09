package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Demarcheur;
import com.memoire.apiAhoewo.model.gestionDesComptes.Notaire;
import com.memoire.apiAhoewo.service.gestionDesComptes.NotaireService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NotaireController {
    @Autowired
    private NotaireService notaireService;
    @Autowired
    private PersonneService personneService;

    @RequestMapping(value = "/notaires", method = RequestMethod.GET)
    public List<Notaire> getAll() {

        List<Notaire> notaireList = new ArrayList<>();
        try {
            notaireList = this.notaireService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return notaireList;
    }

    @RequestMapping(value = "/notaires/pagines", method = RequestMethod.GET)
    public Page<Notaire> getNotaires(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.notaireService.getNotaires(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des notaires.", e);
        }
    }

    @RequestMapping(value = "/notaire/{id}", method = RequestMethod.GET)
    public Notaire findById(@PathVariable Long id) {

        Notaire notaire = new Notaire();
        try {
            notaire = this.notaireService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return notaire;
    }

    @RequestMapping(value = "/notaire/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterNotaire(Principal principal, @RequestBody Notaire notaire) {
        try {
            if (personneService.emailExists(notaire.getEmail())) {
                new ResponseEntity<>("Un notaire avec cette adresse e-mail existe déjà", HttpStatus.CONFLICT);
            }
            notaire = this.notaireService.save(notaire, principal);
            return ResponseEntity.ok(notaire);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du notaire : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/notaire/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerNotaire(@PathVariable Long id) {
        this.notaireService.deleteById(id);
    }

    @RequestMapping(value = "/count/notaires", method = RequestMethod.GET)
    public int nombreDeNotaires(){
        int nombres = this.notaireService.countNotaires();
        return nombres;
    }
}
