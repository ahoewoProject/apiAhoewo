package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.service.gestionDesComptes.AgentImmobilierService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
