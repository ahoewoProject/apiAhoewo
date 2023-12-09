package com.memoire.apiAhoewo.serviceImpl.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.ResponsableAgenceImmobiliere;
import com.memoire.apiAhoewo.repository.gestionDesComptes.ResponsableAgenceImmobiliereRepository;
import com.memoire.apiAhoewo.service.gestionDesComptes.ResponsableAgenceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponsableAgenceImmobiliereServiceImpl implements ResponsableAgenceImmobiliereService {
    @Autowired
    private ResponsableAgenceImmobiliereRepository responsableAgenceImmobiliereRepository;

    @Override
    public List<ResponsableAgenceImmobiliere> getAll() {
        return responsableAgenceImmobiliereRepository.findAll();
    }

    @Override
    public Page<ResponsableAgenceImmobiliere> getResponsables(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return responsableAgenceImmobiliereRepository.findAll(pageRequest);
    }

    @Override
    public ResponsableAgenceImmobiliere findById(Long id) {
        return responsableAgenceImmobiliereRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        responsableAgenceImmobiliereRepository.deleteById(id);
    }

    @Override
    public int countResponsablesAgenceImmobiliere() {
        return (int) responsableAgenceImmobiliereRepository.count();
    }
}
