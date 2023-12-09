package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Client;
import com.memoire.apiAhoewo.service.gestionDesComptes.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public List<Client> getAll() {

        List<Client> clientList = new ArrayList<>();
        try {
            clientList = this.clientService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return clientList;
    }

    @RequestMapping(value = "/clients/pagines", method = RequestMethod.GET)
    public Page<Client> getClients(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.clientService.getClients(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des clients.", e);
        }
    }

    @RequestMapping(value = "/client/{id}", method = RequestMethod.GET)
    public Client findById(@PathVariable Long id) {

        Client client = new Client();
        try {
            client = this.clientService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return client;
    }

    @RequestMapping(value = "/client/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerClient(@PathVariable Long id) {
        this.clientService.deleteById(id);
    }

    @RequestMapping(value = "/count/clients", method = RequestMethod.GET)
    public int nombreDeClients(){
        int nombres = this.clientService.countClients();
        return nombres;
    }
}
