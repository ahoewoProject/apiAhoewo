package com.memoire.apiAhoewo.servicesImpls.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmAssocie;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers.BienImmAssocieRepository;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierAssocieService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BienImmAssocieServiceImpl implements BienImmobilierAssocieService {
    @Autowired
    private BienImmAssocieRepository bienImmAssocieRepository;
    @Autowired
    private BienImmobilierService bienImmobilierService;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;

    @Override
    public Page<BienImmAssocie> getBiensAssociesPaginesByBienImmobilier(Long id, int numeroDeLaPage, int elementsParPage) {
        BienImmobilier bienImmobilier = bienImmobilierService.findById(id);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return bienImmAssocieRepository.findAllByBienImmobilierOrderByCreerLeDesc(bienImmobilier, pageRequest);
    }

    @Override
    public List<BienImmAssocie> getBiensAssocies(BienImmobilier bienImmobilier) {
        return bienImmAssocieRepository.findAllByBienImmobilier(bienImmobilier);
    }

    @Override
    public List<BienImmAssocie> getBiensAssocies(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        String roleCode = personne.getRole().getCode();

        if (personneService.estProprietaire(roleCode) || personneService.estDemarcheur(roleCode)) {
            return bienImmAssocieRepository.findAllByPersonneOrderByCreerLeDesc(personne);
        } else if (personneService.estResponsable(roleCode)) {
            List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            return bienImmAssocieRepository.findAllByAgenceImmobiliereInOrderByCreerLeDesc(agenceImmobilieres);
        } else if (personneService.estAgentImmobilier(roleCode)) {
            List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            return bienImmAssocieRepository.findAllByAgenceImmobiliereInOrderByCreerLeDesc(agenceImmobilieres);
        }

        return null;
    }

    @Override
    public BienImmAssocie findById(Long id) {
        return bienImmAssocieRepository.findById(id).orElse(null);
    }

    @Override
    public BienImmAssocie save(BienImmAssocie bienImmAssocie, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        if (bienImmAssocie.getBienImmobilier().getAgenceImmobiliere() != null) {
            bienImmAssocie.setAgenceImmobiliere(bienImmAssocie.getBienImmobilier().getAgenceImmobiliere());
        } else {
            bienImmAssocie.setPersonne(bienImmAssocie.getBienImmobilier().getPersonne());
        }
        bienImmAssocie.setCodeBien("BIMMA" + UUID.randomUUID());
        bienImmAssocie.setStatutBien("Disponible");
        bienImmAssocie.setEtatBien(true);
        bienImmAssocie.setCreerLe(new Date());
        bienImmAssocie.setCreerPar(personne.getId());
        bienImmAssocie.setStatut(true);
        BienImmAssocie magasinInsere = bienImmAssocieRepository.save(bienImmAssocie);
        magasinInsere.setCodeBien("BIMMA00" + magasinInsere.getId());
        return bienImmAssocieRepository.save(magasinInsere);
    }

    @Override
    public BienImmAssocie update(BienImmAssocie bienImmAssocie, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        if (bienImmAssocie.getBienImmobilier().getAgenceImmobiliere() != null) {
            bienImmAssocie.setAgenceImmobiliere(bienImmAssocie.getBienImmobilier().getAgenceImmobiliere());
        } else {
            bienImmAssocie.setPersonne(bienImmAssocie.getBienImmobilier().getPersonne());
        }
        bienImmAssocie.setModifierLe(new Date());
        bienImmAssocie.setModifierPar(personne.getId());
        return bienImmAssocieRepository.save(bienImmAssocie);
    }

    @Override
    public BienImmAssocie setBienImmAssocie(BienImmAssocie bienImmAssocie) {
        return bienImmAssocieRepository.save(bienImmAssocie);
    }

    @Override
    public boolean bienImmobilierExists(BienImmobilier bienImmobilier) {
        return bienImmAssocieRepository.existsByBienImmobilier(bienImmobilier);
    }
}
