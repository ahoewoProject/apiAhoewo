package com.memoire.apiAhoewo.service.gestionDesBiensImmobiliers;

import com.memoire.apiAhoewo.model.gestionDesBiensImmobiliers.TypeDeBien;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface TypeDeBienService {
    public List<TypeDeBien> getAll();

    public Page<TypeDeBien> getTypesDeBienPagines(int numeroDeLaPage, int elementsParPage);

    public List<TypeDeBien> findTypeDeBienActifs();

    public TypeDeBien findById(Long id);

    public TypeDeBien findByDesignation(String designation);

    public TypeDeBien save(TypeDeBien typeDeBien, Principal principal);

    public TypeDeBien update(TypeDeBien typeDeBien, Principal principal);

    public void activerTypeDeBien(Long id);

    public void desactiverTypeDeBien(Long id);

    public void deleteById(Long id);
}
