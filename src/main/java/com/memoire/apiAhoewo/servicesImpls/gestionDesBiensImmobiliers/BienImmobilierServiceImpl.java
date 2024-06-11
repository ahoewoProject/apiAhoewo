package com.memoire.apiAhoewo.servicesImpls.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmAssocie;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers.BienImmobilierRepository;
import com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers.DelegationGestionRepository;
import com.memoire.apiAhoewo.services.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierAssocieService;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BienImmobilierServiceImpl implements BienImmobilierService {
    @Autowired
    private BienImmobilierRepository bienImmobilierRepository;
    @Autowired
    private PersonneService personneService;
    @Autowired
    private AgenceImmobiliereService agenceImmobiliereService;
    @Autowired
    private DelegationGestionRepository delegationGestionRepository;
    @Autowired
    private BienImmobilierAssocieService bienImmobilierAssocieService;

    @Override
    public List<BienImmobilier> getBiensImmobiliers(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());

        String roleCode = personne.getRole().getCode();

        if (personneService.estProprietaire(roleCode) || personneService.estDemarcheur(roleCode)) {
            return bienImmobilierRepository.findByPersonne(personne);
        } else if (personneService.estResponsable(roleCode)) {
            List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            return bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
        } else if (personneService.estAgentImmobilier(roleCode)) {
            List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            return bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
        }

        return null;
    }

    @Override
    public List<BienImmobilier> getBiensSupports(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<String> designations = Arrays.asList("Terrain", "Maison", "Immeuble", "Villa");

        String roleCode = personne.getRole().getCode();

        if (personneService.estProprietaire(roleCode) || personneService.estDemarcheur(roleCode)) {
            return bienImmobilierRepository.findAllByPersonneAndTypeDeBien_DesignationInOrderByCreerLeDesc(personne, designations);
        } else if (personneService.estResponsable(roleCode)) {
            List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            return bienImmobilierRepository.findAllByAgenceImmobiliereInAndTypeDeBien_DesignationInOrderByCreerLeDesc(agenceImmobilieres, designations);
        } else if (personneService.estAgentImmobilier(roleCode)) {
            List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            return bienImmobilierRepository.findAllByAgenceImmobiliereInAndTypeDeBien_DesignationInOrderByCreerLeDesc(agenceImmobilieres, designations);
        }

        return null;
    }

    @Override
    public Page<BienImmobilier> getBiensSupportsPagines(Principal principal, int numeroDeLaPage, int elementsParPage) {
        Personne personne = personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        List<String> designations = Arrays.asList("Terrain", "Maison", "Immeuble", "Villa");

        String roleCode = personne.getRole().getCode();

        if (personneService.estProprietaire(roleCode) || personneService.estDemarcheur(roleCode)) {
            return bienImmobilierRepository.findAllByPersonneAndTypeDeBien_DesignationInOrderByCreerLeDesc(personne, pageRequest, designations);
        } else if (personneService.estResponsable(roleCode)) {
            List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            return bienImmobilierRepository.findAllByAgenceImmobiliereInAndTypeDeBien_DesignationInOrderByCreerLeDesc(agenceImmobilieres, pageRequest, designations);
        } else if (personneService.estAgentImmobilier(roleCode)) {
            List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            return bienImmobilierRepository.findAllByAgenceImmobiliereInAndTypeDeBien_DesignationInOrderByCreerLeDesc(agenceImmobilieres, pageRequest, designations);
        }

        return null;
    }

    @Override
    public List<BienImmobilier> getBiensByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return bienImmobilierRepository.findByPersonne(personne);
    }

    @Override
    public List<BienImmobilier> getBiensPropresAndBiensDelegues(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<BienImmobilier> bienImmobilierList = new ArrayList<>();

        String roleCode = personne.getRole().getCode();

        if (personneService.estGerant(roleCode)) {
            List<DelegationGestion> delegationGestionList = delegationGestionRepository.findByGestionnaireAndStatutDelegation(personne, 1);

            bienImmobilierList = delegationGestionList.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList());
        } else if (personneService.estDemarcheur(roleCode)) {
            List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByPersonne(personne);
            List<DelegationGestion> delegationGestions = delegationGestionRepository.findByGestionnaireAndStatutDelegation(personne, 1);

            bienImmobilierList = new ArrayList<>(bienImmobiliers);
            bienImmobilierList.addAll(delegationGestions.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList()));
        } else if (personneService.estResponsable(roleCode)) {
            List<AgenceImmobiliere> agenceImmobiliereList = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobiliereList);
            List<DelegationGestion> delegationGestionList = delegationGestionRepository.findByAgenceImmobiliereInAndStatutDelegation(agenceImmobiliereList, 1);

            bienImmobilierList = new ArrayList<>(bienImmobiliers);
            bienImmobilierList.addAll(delegationGestionList.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList()));
        } else if (personneService.estAgentImmobilier(roleCode)) {
            List<AgenceImmobiliere> agenceImmobiliereList = agenceImmobiliereService.getAgencesImmobilieresList(principal);
            List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobiliereList);
            List<DelegationGestion> delegationGestionList = delegationGestionRepository.findByAgenceImmobiliereInAndStatutDelegation(agenceImmobiliereList, 1);

            bienImmobilierList = new ArrayList<>(bienImmobiliers);
            bienImmobilierList.addAll(delegationGestionList.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList()));
        }

        return bienImmobilierList;
    }

    @Override
    public BienImmobilier findById(Long id) {
        return bienImmobilierRepository.findById(id).orElse(null);
    }

    @Override
    public BienImmobilier findByCodeBien(String codeBien) {
        return bienImmobilierRepository.findByCodeBien(codeBien);
    }

    @Override
    public BienImmobilier save(BienImmobilier bienImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        bienImmobilier.setCodeBien("BIMMO" + UUID.randomUUID());
        bienImmobilier.setStatutBien("Disponible");
        bienImmobilier.setEtatBien(true);
        bienImmobilier.setCreerLe(new Date());
        bienImmobilier.setCreerPar(personne.getId());
        bienImmobilier.setStatut(true);
        BienImmobilier bienImmobilierInsere = bienImmobilierRepository.save(bienImmobilier);
        bienImmobilierInsere.setCodeBien("BIMMO00" + bienImmobilierInsere.getId());
        return bienImmobilierRepository.save(bienImmobilierInsere);
    }

    @Override
    public BienImmobilier update(BienImmobilier bienImmobilier, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        bienImmobilier.setModifierLe(new Date());
        bienImmobilier.setModifierPar(personne.getId());
        setBienAssocieByBienSupport(bienImmobilier);

        return bienImmobilierRepository.save(bienImmobilier);
    }

    private void setBienAssocieByBienSupport(BienImmobilier bienImmobilier) {
        List<BienImmAssocie> bienImmAssocieList =  bienImmobilierAssocieService.getBiensAssocies(bienImmobilier);
        if (bienImmAssocieList != null && !bienImmAssocieList.isEmpty()) {
            // Parcourt chaque bien associé
            for (BienImmAssocie bienAssocie : bienImmAssocieList) {
                bienAssocie.setAgenceImmobiliere(bienImmobilier.getAgenceImmobiliere());
                bienImmobilierAssocieService.setBienImmAssocie(bienAssocie);
            }
        }
    }

    @Override
    public BienImmobilier setBienImmobilier(BienImmobilier bienImmobilier) {
        return bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public boolean existsByCodeBien(String codeBien) {
        return bienImmobilierRepository.existsByCodeBien(codeBien);
    }

    @Override
    public void activerBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierRepository.findById(id).orElse(null);
        assert bienImmobilier != null;
        bienImmobilier.setEtatBien(true);
        bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void desactiverBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierRepository.findById(id).orElse(null);
        assert bienImmobilier != null;
        bienImmobilier.setEtatBien(false);
        bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void deleteById(Long id) {
        bienImmobilierRepository.deleteById(id);
    }
}
