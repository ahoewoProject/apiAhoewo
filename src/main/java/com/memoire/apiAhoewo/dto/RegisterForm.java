package com.memoire.apiAhoewo.dto;

import com.memoire.apiAhoewo.models.gestionDesComptes.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
    private Long id;
    private String nom;
    private String prenom;
    private String username;
    private String email;
    private String motDePasse;
    private String telephone;
    private Role role;
}

