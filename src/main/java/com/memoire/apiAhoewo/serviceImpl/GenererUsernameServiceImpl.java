package com.memoire.apiAhoewo.serviceImpl;

import com.memoire.apiAhoewo.service.GenererUsernameService;
import org.springframework.stereotype.Service;

import java.text.Normalizer;

@Service
public class GenererUsernameServiceImpl implements GenererUsernameService {
    @Override
    public String genererUsername(String prenoms) {
        // Séparation du prénom complet en mots
        String[] names = prenoms.split("\\s+");

        // Prendre le premier prénom
        String premierPrenom = names[0];

        // Supprimer les accents du prénom
        String prenomSansAccent = supprimerLesAccents(premierPrenom);

        // Mettre en minuscules
        String prenomEnMinuscule = prenomSansAccent.toLowerCase();

        return prenomEnMinuscule;
    }

    public static String supprimerLesAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
