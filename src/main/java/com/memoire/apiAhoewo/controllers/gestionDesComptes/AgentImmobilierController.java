package com.memoire.apiAhoewo.controllers.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.services.gestionDesComptes.AgentImmobilierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AgentImmobilierController {
    @Autowired
    private AgentImmobilierService agentImmobilierService;

    @RequestMapping(value = "/agents-immobiliers", method = RequestMethod.GET)
    public List<AgentImmobilier> getAll() {

        List<AgentImmobilier> agentImmobilierList = new ArrayList<>();
        try {
            agentImmobilierList = this.agentImmobilierService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return agentImmobilierList;
    }

    @RequestMapping(value = "/agent-immobilier/{id}", method = RequestMethod.GET)
    public AgentImmobilier findById(@PathVariable Long id) {

        AgentImmobilier agentImmobilier = new AgentImmobilier();
        try {
            agentImmobilier = this.agentImmobilierService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return agentImmobilier;
    }
}
