package com.memoire.apiAhoewo.servicesImpls.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.models.gestionDesBiensImmobiliers.TypeDeBien;
import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.repositories.gestionDesBiensImmobiliers.TypeDeBienRepository;
import com.memoire.apiAhoewo.services.gestionDesBiensImmobiliers.TypeDeBienService;
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
public class TypeDeBienServiceImpl implements TypeDeBienService {
    @Autowired
    private TypeDeBienRepository typeDeBienRepository;
    @Autowired
    private PersonneService personneService;

    @Override
    public List<TypeDeBien> getAll() {
        return typeDeBienRepository.findAll();
    }

    @Override
    public Page<TypeDeBien> getTypesDeBienPagines(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return typeDeBienRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public List<TypeDeBien> findTypeDeBienActifs() {
        return typeDeBienRepository.findByEtat(true);
    }

    @Override
    public List<TypeDeBien> findTypeDeBienActifsByLibelle(List<String> designations) {
        return typeDeBienRepository.findByDesignationInAndEtat(designations, true);
    }

    @Override
    public TypeDeBien findById(Long id) {
        return typeDeBienRepository.findById(id).orElse(null);
    }

    @Override
    public TypeDeBien findByDesignation(String designation) {
        return typeDeBienRepository.findByDesignation(designation);
    }

    @Override
    public TypeDeBien findByCode(String code) {
        return typeDeBienRepository.findByCode(code);
    }


    @Override
    public TypeDeBien save(TypeDeBien typeDeBien, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        typeDeBien.setCode("TYBIEN" + UUID.randomUUID());
        typeDeBien.setEtat(true);
        typeDeBien.setCreerLe(new Date());
        typeDeBien.setCreerPar(personne.getId());
        typeDeBien.setStatut(true);
        TypeDeBien typeDeBienInsere = typeDeBienRepository.save(typeDeBien);
        typeDeBienInsere.setCode("TYBIEN00" + typeDeBienInsere.getId());
        return typeDeBienRepository.save(typeDeBienInsere);
    }

    @Override
    public TypeDeBien update(TypeDeBien typeDeBien, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        typeDeBien.setModifierLe(new Date());
        typeDeBien.setModifierPar(personne.getId());
        return typeDeBienRepository.save(typeDeBien);
    }

    @Override
    public void activerTypeDeBien(Long id) {
        TypeDeBien typeDeBien = typeDeBienRepository.findById(id).orElse(null);
        typeDeBien.setEtat(true);
        typeDeBienRepository.save(typeDeBien);
    }

    @Override
    public void desactiverTypeDeBien(Long id) {
        TypeDeBien typeDeBien = typeDeBienRepository.findById(id).orElse(null);
        typeDeBien.setEtat(false);
        typeDeBienRepository.save(typeDeBien);
    }

    @Override
    public void deleteById(Long id) {
        typeDeBienRepository.deleteById(id);
    }

    @Override
    public boolean isTypeBienSupport(String designation) {
        return designation.equals("Maison") || designation.equals("Immeuble")
                || designation.equals("Villa");
    }

    @Override
    public boolean isTypeBienAssocie(String designation) {
        return designation.equals("Appartement") || designation.equals("Chambre salon")
                || designation.equals("Chambre") || designation.equals("Bureau")
                || designation.equals("Boutique") || designation.equals("Magasin");
    }
}
