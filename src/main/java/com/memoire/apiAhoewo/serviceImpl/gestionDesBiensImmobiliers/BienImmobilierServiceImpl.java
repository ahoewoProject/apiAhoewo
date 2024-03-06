package com.memoire.apiAhoewo.serviceImpl.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesAgencesImmobilieres.AgenceImmobiliere;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.BienImmobilier;
import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.DelegationGestion;
import com.memoire.apiAhoewo.model.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.BienImmobilierRepository;
import com.memoire.apiAhoewo.repository.gestionDesBiensImmobiliers.DelegationGestionRepository;
import com.memoire.apiAhoewo.service.gestionDesAgencesImmobilieres.AgenceImmobiliereService;
import com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers.BienImmobilierService;
import com.memoire.apiAhoewo.service.gestionDesComptes.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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

    @Override
    public List<BienImmobilier> getAll() {
        return bienImmobilierRepository.findAll();
    }

    @Override
    public Page<BienImmobilier> getBiensPaginesByProprietaire(Principal principal, int numeroDeLaPage, int elementsParPage) {
        Personne personne = personneService.findByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        List<String> designations = new ArrayList<>();
        designations.add("Terrain");
        designations.add("Maison");
        designations.add("Immeuble");
        designations.add("Villa");
        return bienImmobilierRepository.findAllByPersonneAndTypeDeBien_DesignationInOrderByCreerLeDesc(personne, pageRequest, designations);
    }

    @Override
    public Page<BienImmobilier> getBiensPaginesOfAgencesByResponsable(Principal principal, int numeroDeLaPage, int elementsParPage) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByResponsable(principal);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        List<String> designations = new ArrayList<>();
        designations.add("Terrain");
        designations.add("Maison");
        designations.add("Immeuble");
        designations.add("Villa");
        return bienImmobilierRepository.findAllByAgenceImmobiliereInAndTypeDeBien_DesignationInOrderByCreerLeDesc(agenceImmobilieres, pageRequest, designations);
    }

    @Override
    public Page<BienImmobilier> getBiensPaginesOfAgencesByAgent(Principal principal, int numeroDeLaPage, int elementsParPage) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByAgent(principal);
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        List<String> designations = new ArrayList<>();
        designations.add("Terrain");
        designations.add("Maison");
        designations.add("Immeuble");
        designations.add("Villa");
        return bienImmobilierRepository.findAllByAgenceImmobiliereInAndTypeDeBien_DesignationInOrderByCreerLeDesc(agenceImmobilieres, pageRequest, designations);
    }

    @Override
    public List<BienImmobilier> getBiensByProprietaire(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        return bienImmobilierRepository.findByPersonne(personne);
    }

    @Override
    public List<BienImmobilier> getBiensOfAgencesByResponsable(Principal principal) {

        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByResponsable(principal);

        return bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public List<BienImmobilier> getBiensOfAgencesByAgent(Principal principal) {
        List<AgenceImmobiliere> agenceImmobilieres = agenceImmobiliereService.getAgencesByAgent(principal);

        return bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobilieres);
    }

    @Override
    public List<BienImmobilier> getBiensPropresAndBiensDelegues(Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        List<BienImmobilier> bienImmobilierList = new ArrayList<>();

        if (personne.getRole().getCode().equals("ROLE_GERANT")) {
            List<DelegationGestion> delegationGestionList = delegationGestionRepository.findByGestionnaireAndStatutDelegation(personne, 1);

            bienImmobilierList = delegationGestionList.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList());
        } else if (personne.getRole().getCode().equals("ROLE_DEMARCHEUR")) {
            List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByPersonne(personne);
            List<DelegationGestion> delegationGestions = delegationGestionRepository.findByGestionnaireAndStatutDelegation(personne, 1);

            bienImmobilierList = new ArrayList<>(bienImmobiliers);
            bienImmobilierList.addAll(delegationGestions.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList()));
        } else if (personne.getRole().getCode().equals("ROLE_RESPONSABLE")) {
            List<AgenceImmobiliere> agenceImmobiliereList = agenceImmobiliereService.getAgencesByResponsable(principal);
            List<BienImmobilier> bienImmobiliers = bienImmobilierRepository.findByAgenceImmobiliereIn(agenceImmobiliereList);
            List<DelegationGestion> delegationGestionList = delegationGestionRepository.findByAgenceImmobiliereInAndStatutDelegation(agenceImmobiliereList, 1);

            bienImmobilierList = new ArrayList<>(bienImmobiliers);
            bienImmobilierList.addAll(delegationGestionList.stream()
                    .map(DelegationGestion::getBienImmobilier)
                    .collect(Collectors.toList()));
        } else if (personne.getRole().getCode().equals("ROLE_AGENTIMMOBILIER")) {
            List<AgenceImmobiliere> agenceImmobiliereList = agenceImmobiliereService.getAgencesByAgent(principal);
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
        return bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public boolean existsByCodeBien(String codeBien) {
        return bienImmobilierRepository.existsByCodeBien(codeBien);
    }

    @Override
    public void activerBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierRepository.findById(id).orElse(null);
        bienImmobilier.setEtatBien(true);
        bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void desactiverBienImmobilier(Long id) {
        BienImmobilier bienImmobilier = bienImmobilierRepository.findById(id).orElse(null);
        bienImmobilier.setEtatBien(false);
        bienImmobilierRepository.save(bienImmobilier);
    }

    @Override
    public void deleteById(Long id) {
        bienImmobilierRepository.deleteById(id);
    }
}
