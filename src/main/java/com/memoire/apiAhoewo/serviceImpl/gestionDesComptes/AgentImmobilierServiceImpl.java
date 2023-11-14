package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesComptes.AgentImmobilier;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesAgencesImmobilieres.AgenceImmobiliereRepository;
import com.memoire.apiAhoewo.repository.gestionDesComptes.AgentImmobilierRepository;
import com.memoire.apiAhoewo.service.gestionDesComptes.AgentImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class AgentImmobilierServiceImpl implements AgentImmobilierService {
    @Autowired
    private AgentImmobilierRepository agentImmobilierRepository;

    @Autowired
    private AgenceImmobiliereRepository agenceImmobiliereRepository;

    @Autowired
    private PersonneService personneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<AgentImmobilier> getAll() {
        return agentImmobilierRepository.findAll();
    }

    @Override
    public List<AgentImmobilier> findAgentsImmobiliersByResponsable(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return agentImmobilierRepository.findByCreerPar(personne.getId());
    }

    @Override
    public AgentImmobilier findById(Long id) {
        return agentImmobilierRepository.findById(id).orElse(null);
    }

    @Override
    public AgentImmobilier save(AgentImmobilier agentImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereRepository.findByResponsableAgenceImmobiliere((ResponsableAgenceImmobiliere) personne);

        boolean estCertifieAgentImmobilier = false;

        // Vérifiez si au moins une agence immobilière est certifiée
        for (AgenceImmobiliere agence : agenceImmobilieres) {
            if (agence.getEstCertifie()) {
                estCertifieAgentImmobilier = true;
                break; // Si une agence est certifiée, pas besoin de continuer la boucle
            }
        }

        agentImmobilier.setEtatCompte(true);
        agentImmobilier.setEstCertifie(estCertifieAgentImmobilier);
        agentImmobilier.setCreerLe(new Date());
        agentImmobilier.setCreerPar(personne.getId());
        agentImmobilier.setStatut(true);
        agentImmobilier.setMotDePasse(passwordEncoder.encode(agentImmobilier.getMotDePasse()));
        return agentImmobilierRepository.save(agentImmobilier);
    }

    @Override
    public void deleteById(Long id) {
        agentImmobilierRepository.deleteById(id);
    }

    @Override
    public int countAgentImmobiliers() {
        return (int) agentImmobilierRepository.count();
    }

    @Override
    public int countAgentsImmobiliersByResponsable(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<AgentImmobilier> agentImmobiliers = agentImmobilierRepository.findByCreerPar(personne.getId());
        int count = agentImmobiliers.size();
        return count;
    }
}
