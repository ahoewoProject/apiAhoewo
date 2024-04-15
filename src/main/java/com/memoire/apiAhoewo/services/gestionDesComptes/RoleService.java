package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Role;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface RoleService {
    public List<Role> getAll();

    Page<Role> getRoles(int numeroDeLaPage, int elementsParPage);

    public Role findById(Long id);

    public Role findByCode(String code);

    public Role save(Role role, Principal principal);

    public Role update(Role role, Principal principal);

    public void deleteById(Long id);

    public int countRole();
}
