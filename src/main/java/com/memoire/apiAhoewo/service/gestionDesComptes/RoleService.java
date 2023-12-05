package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Role;

import java.security.Principal;
import java.util.List;

public interface RoleService {
    public List<Role> getAll();

    public Role findById(Long id);

    public Role findByCode(String code);

    public Role save(Role role, Principal principal);

    public Role update(Role role, Principal principal);

    public void deleteById(Long id);

    public int countRole();
}
