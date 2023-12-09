package com.memoire.apiAhoewo.controller.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Role;
import com.memoire.apiAhoewo.service.gestionDesComptes.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public List<Role> getAll() {

        List<Role> roles = new ArrayList<>();
        try {
            roles = this.roleService.getAll();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
        }
        return roles;
    }

    @RequestMapping(value = "/roles/pagines", method = RequestMethod.GET)
    public Page<Role> getRolesPaginees(
            @RequestParam(value = "numeroDeLaPage") int numeroDeLaPage,
            @RequestParam(value = "elementsParPage") int elementsParPage) {

        try {
            return this.roleService.getRoles(numeroDeLaPage, elementsParPage);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erreur " + e.getMessage());
            throw new RuntimeException("Une erreur s'est produite lors de la récupération des roles.", e);
        }
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public Role findById(@PathVariable Long id) {

        Role role = new Role();
        try {
            role = this.roleService.findById(id);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
        }
        return role;
    }

    @RequestMapping(value = "/role/ajouter", method = RequestMethod.POST, headers = "accept=Application/json")
    public ResponseEntity<?> ajouterRole(Principal principal, @RequestBody Role role) {
        try {
            Role existingRole = roleService.findByCode(role.getCode());

            if (existingRole != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un rôle avec ce code " + role.getCode() + " existe déjà.");
            }

            role = this.roleService.save(role, principal);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout du rôle : " + e.getMessage());
        }
    }

    @RequestMapping(value = "/role/modifier/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public ResponseEntity<?> modifierRole(Principal principal, @RequestBody Role roleModifie, @PathVariable  Long id) {
        Role role = roleService.findById(id);
        Role existingRole = roleService.findByCode(roleModifie.getCode());
        try {
            if (existingRole != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un rôle avec le code " + roleModifie.getCode() + " existe déjà.");
            }
            role.setCode(roleModifie.getCode());
            role.setLibelle(roleModifie.getLibelle());
            role = this.roleService.update(role, principal);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            System.out.println("Erreur " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la modification du rôle : " + e.getMessage());
        }

    }

    @RequestMapping(value = "/role/supprimer/{id}", method = RequestMethod.DELETE, headers = "accept=Application/json")
    public void supprimerRole(@PathVariable Long id) {
        this.roleService.deleteById(id);
    }

    @RequestMapping(value = "/count/roles", method = RequestMethod.GET)
    public int nombreDeRoles(){
        int nombres = this.roleService.countRole();
        return nombres;
    }
}
