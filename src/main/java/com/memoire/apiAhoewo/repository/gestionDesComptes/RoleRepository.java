package com.memoire.apiAhoewo.repository.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Override
    Page<Role> findAll(Pageable pageable);

    Role findByCode(String code);
}
