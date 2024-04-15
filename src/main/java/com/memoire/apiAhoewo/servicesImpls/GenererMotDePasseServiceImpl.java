package com.memoire.apiAhoewo.servicesImpls;

import com.memoire.apiAhoewo.services.GenererMotDePasseService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class GenererMotDePasseServiceImpl implements GenererMotDePasseService {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]|,./?><";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    private static final Random RANDOM = new SecureRandom();

    @Override
    public String genererMotDePasse(int nombreCaracteres) {
        StringBuilder password = new StringBuilder(nombreCaracteres);

        if (nombreCaracteres < 8) {
            throw new IllegalArgumentException("Le mot de passe doit comporter au moins 8 caractères.");
        }

        // Ajout d'au moins une lettre minuscule, une lettre majuscule, un chiffre et un caractère spécial
        password.append(CHAR_LOWER.charAt(RANDOM.nextInt(CHAR_LOWER.length())));
        password.append(CHAR_UPPER.charAt(RANDOM.nextInt(CHAR_UPPER.length())));
        password.append(NUMBER.charAt(RANDOM.nextInt(NUMBER.length())));
        password.append(OTHER_CHAR.charAt(RANDOM.nextInt(OTHER_CHAR.length())));

        // Génération des caractères restants du mot de passe
        for (int i = 4; i < nombreCaracteres; i++) {
            password.append(PASSWORD_ALLOW_BASE.charAt(RANDOM.nextInt(PASSWORD_ALLOW_BASE.length())));
        }

        // Mélange des caractères pour rendre le mot de passe plus aléatoire
        char[] motDePasseAleatoire = password.toString().toCharArray();
        for (int i = 0; i < nombreCaracteres; i++) {
            int random = RANDOM.nextInt(nombreCaracteres);
            char temp = motDePasseAleatoire[random];
            motDePasseAleatoire[random] = motDePasseAleatoire[i];
            motDePasseAleatoire[i] = temp;
        }

        return new String(motDePasseAleatoire);
    }
}
