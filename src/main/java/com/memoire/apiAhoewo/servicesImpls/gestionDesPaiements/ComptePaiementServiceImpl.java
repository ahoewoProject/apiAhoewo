package com.memoire.apiAhoewo.servicesImpls.gestionDesPaiements;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesPaiements.ComptePaiement;
import com.memoire.apiAhoewo.repositories.gestionDesPaiements.ComptePaiementRepository;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesPaiements.ComptePaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ComptePaiementServiceImpl implements ComptePaiementService {
    @Autowired
    private ComptePaiementRepository comptePaiementRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;
    @Autowired
    private ComptePaiementService comptePaiementService;

    @Override
    public Page<ComptePaiement> getComptesPaiements(Principal principal, int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);

        Personne personne = personneService.findByUsername(principal.getName());

        String roleCode = personne.getRole().getCode();

        if (personneService.estProprietaire(roleCode) || personneService.estDemarcheur(roleCode) ||
        personneService.estGerant(roleCode)) {
            return comptePaiementRepository.findAllByPersonneOrderByIdDesc(personne, pageRequest);
        } else if (personneService.estAgentImmobilier(roleCode) || personneService.estResponsable(roleCode)) {
            List<AgenceImmobiliere> agenceImmobiliereList = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            return comptePaiementRepository.findAllByAgenceImmobiliereInOrderByIdDesc(agenceImmobiliereList, pageRequest);
        }
        return null;
    }

    @Override
    public List<ComptePaiement> findByAgence(AgenceImmobiliere agenceImmobiliere) {
        return comptePaiementRepository.findByAgenceImmobiliere(agenceImmobiliere);
    }

    @Override
    public List<ComptePaiement> findByPersonne(Personne personne) {
        return comptePaiementRepository.findByPersonne(personne);
    }

    @Override
    public ComptePaiement findByTypeAndPersonneAndEtat(String type, Personne personne, Boolean etat) {
        return comptePaiementRepository.findByTypeAndPersonneAndEtatComptePaiement(type, personne, etat);
    }

    @Override
    public ComptePaiement findByTypeAndAgenceAndEtat(String type, AgenceImmobiliere agenceImmobiliere, Boolean etat) {
        return comptePaiementRepository.findByTypeAndAgenceImmobiliereAndEtatComptePaiement(type, agenceImmobiliere
                , etat);
    }

    @Override
    public ComptePaiement findById(Long comptePaiementId) {
        return comptePaiementRepository.findById(comptePaiementId).orElse(null);
    }

    @Override
    public ComptePaiement save(ComptePaiement comptePaiement, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        comptePaiement.setCodeComptePaiement("COMPAI" + UUID.randomUUID());
        List<ComptePaiement> existingComptes;
        if (comptePaiement.getPersonne() != null) {
            existingComptes = comptePaiementService.findByPersonne(comptePaiement.getPersonne());
        } else {
            existingComptes = comptePaiementService.findByAgence(comptePaiement.getAgenceImmobiliere());
        }

        if (existingComptes.isEmpty()) {
            comptePaiement.setEtatComptePaiement(true);
        } else {
            for (ComptePaiement cp : existingComptes) {
                if ((comptePaiement.getType().equals("T-Money") || comptePaiement.getType().equals("Moov-Money")) &&
                        (cp.getType().equals("T-Money") || cp.getType().equals("Moov-Money"))) {
                    comptePaiement.setEtatComptePaiement(false);
                } else if (comptePaiement.getType().equals("PayPal") && cp.getType().equals("PayPal")) {
                    comptePaiement.setEtatComptePaiement(false);
                } else {
                    comptePaiement.setEtatComptePaiement(true);
                }
            }
        }

        comptePaiement.setCreerLe(new Date());
        comptePaiement.setCreerPar(personne.getId());
        comptePaiement.setStatut(true);

        ComptePaiement comptePaiementAdd = comptePaiementRepository.save(comptePaiement);
        comptePaiementAdd.setCodeComptePaiement("COMPAI00" + comptePaiementAdd.getId());

        return comptePaiementRepository.save(comptePaiementAdd);
    }

    @Override
    public ComptePaiement update(ComptePaiement comptePaiement, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        comptePaiement.setModifierLe(new Date());
        comptePaiement.setModifierPar(personne.getId());

        return comptePaiementRepository.save(comptePaiement);
    }

    @Override
    public void activerComptePaiementByPersonne(Principal principal, Long comptePaiementId) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<ComptePaiement> comptePaiements = comptePaiementRepository.findByPersonne(personne);

        ComptePaiement comptePaiement = comptePaiementService.findById(comptePaiementId);

        String type = comptePaiement.getType();

        desactiverComptes(comptePaiements, comptePaiementId, type);

        comptePaiement.setEtatComptePaiement(true);
        comptePaiementRepository.save(comptePaiement);
    }

    @Override
    public void activerComptePaiementByAgence(Long comptePaiementId, Long agenceId) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findById(agenceId);
        List<ComptePaiement> comptePaiements = comptePaiementRepository.findByAgenceImmobiliere(agenceImmobiliere);

        ComptePaiement comptePaiement = comptePaiementService.findById(comptePaiementId);

        String type = comptePaiement.getType();

        desactiverComptes(comptePaiements, comptePaiementId, type);

        comptePaiement.setEtatComptePaiement(true);
        comptePaiementRepository.save(comptePaiement);
    }


    @Override
    public void desactiverComptePaiementByPersonne(Principal principal, Long comptePaiementId) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<ComptePaiement> comptePaiements = comptePaiementRepository.findByPersonne(personne);

        ComptePaiement comptePaiement = comptePaiementService.findById(comptePaiementId);

        String type =  comptePaiement.getType();

        desactiverComptes(comptePaiements, comptePaiementId, type);

        comptePaiement.setEtatComptePaiement(false);
        comptePaiementRepository.save(comptePaiement);
    }

    @Override
    public void desactiverComptePaiementByAgence(Long comptePaiementId, Long agenceId) {
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereService.findById(agenceId);
        List<ComptePaiement> comptePaiements = comptePaiementRepository.findByAgenceImmobiliere(agenceImmobiliere);

        ComptePaiement comptePaiement = comptePaiementService.findById(comptePaiementId);

        String type = comptePaiement.getType();

        desactiverComptes(comptePaiements, comptePaiementId, type);

        comptePaiement.setEtatComptePaiement(false);
        comptePaiementRepository.save(comptePaiement);
    }

    private void desactiverComptes(List<ComptePaiement> comptePaiements, Long comptePaiementId, String type) {
        for (ComptePaiement cp : comptePaiements) {
            if (!cp.getId().equals(comptePaiementId)) {
                if ((type.equals("T-Money") || type.equals("Moov-Money")) &&
                        (cp.getType().equals("T-Money") || cp.getType().equals("Moov-Money"))) {
                    cp.setEtatComptePaiement(false);
                    comptePaiementRepository.save(cp);
                } else if (type.equals("PayPal") && cp.getType().equals("PayPal")) {
                    cp.setEtatComptePaiement(false);
                    comptePaiementRepository.save(cp);
                }
            }
        }
    }
}
