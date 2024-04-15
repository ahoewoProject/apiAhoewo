package com.memoire.apiAhoewo.servicesImpls.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Proprietaire;
import com.memoire.apiAhoewo.repositories.gestionDesComptes.ProprietaireRepository;
import com.memoire.apiAhoewo.services.gestionDesComptes.ProprietaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProprietaireServiceImpl implements ProprietaireService {
    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Override
    public List<Proprietaire> getAll() {
        return proprietaireRepository.findAll();
    }

    @Override
    public Page<Proprietaire> getProprietaires(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return proprietaireRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public Proprietaire findById(Long id) {
        return proprietaireRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        proprietaireRepository.deleteById(id);
    }

    @Override
    public int countProprietaires() {
        return (int) proprietaireRepository.count();
    }
}
