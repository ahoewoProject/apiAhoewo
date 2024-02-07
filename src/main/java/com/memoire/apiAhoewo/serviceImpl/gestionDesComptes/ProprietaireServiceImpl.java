package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Proprietaire;
import com.memoire.apiAhoewo.repository.gestionDesComptes.ProprietaireRepository;
import com.memoire.apiAhoewo.service.gestionDesComptes.ProprietaireService;
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
