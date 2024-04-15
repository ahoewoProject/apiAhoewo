package com.memoire.apiAhoewo.servicesImpls.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Demarcheur;
import com.memoire.apiAhoewo.repositories.gestionDesComptes.DemarcheurRepository;
import com.memoire.apiAhoewo.services.gestionDesComptes.DemarcheurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemarcheurServiceImpl implements DemarcheurService {
    @Autowired
    private DemarcheurRepository demarcheurRepository;

    @Override
    public List<Demarcheur> getAll() {
        return demarcheurRepository.findAll();
    }

    @Override
    public Page<Demarcheur> getDemarcheurs(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return demarcheurRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public Demarcheur findById(Long id) {
        return demarcheurRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        demarcheurRepository.deleteById(id);
    }

    @Override
    public int countDemarcheurs() {
        return (int) demarcheurRepository.count();
    }
}
