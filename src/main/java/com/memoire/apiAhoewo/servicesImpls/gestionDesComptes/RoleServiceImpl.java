package com.memoire.apiAhoewo.servicesImpls.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Personne;
import com.memoire.apiAhoewo.models.gestionDesComptes.Role;
import com.memoire.apiAhoewo.repositories.gestionDesComptes.RoleRepository;
import com.memoire.apiAhoewo.services.gestionDesComptes.PersonneService;
import com.memoire.apiAhoewo.services.gestionDesComptes.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PersonneService personneService;

    @Override
    public List<Role> getAll() {
        return this.roleRepository.findAll();
    }

    @Override
    public Page<Role> getRoles(int numeroDeLaPage, int elementsParPage) {
        PageRequest pageRequest = PageRequest.of(numeroDeLaPage, elementsParPage);
        return roleRepository.findAllByOrderByCreerLeDesc(pageRequest);
    }

    @Override
    public Role findById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public Role save(Role role, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        role.setCreerLe(new Date());
        role.setCreerPar(personne.getId());
        role.setStatut(true);
        return this.roleRepository.save(role);
    }

    @Override
    public Role update(Role role, Principal principal) {
        Personne personne = personneService.findByUsername(principal.getName());
        role.setModifierLe(new Date());
        role.setModifierPar(personne.getId());
        return this.roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        this.roleRepository.deleteById(id);
    }

    @Override
    public int countRole() {
        return (int) this.roleRepository.count();
    }
}
