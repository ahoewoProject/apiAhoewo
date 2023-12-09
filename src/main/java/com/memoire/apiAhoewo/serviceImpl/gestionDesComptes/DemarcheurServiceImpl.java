package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Demarcheur;
import com.memoire.apiAhoewo.repository.gestionDesComptes.DemarcheurRepository;
import com.memoire.apiAhoewo.service.gestionDesComptes.DemarcheurService;
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
        return demarcheurRepository.findAll(pageRequest);
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
