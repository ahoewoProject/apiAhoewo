package com.memoire.apiAhoewo.servicesImpls;

import com.memoire.apiAhoewo.exceptions.UnsupportedFileTypeException;
import com.memoire.apiAhoewo.fileManager.FileFilter;
import com.memoire.apiAhoewo.services.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileManagerServiceImpl implements FileManagerService {
    @Autowired
    private FileFilter fileFilter;

    /* Fonction pour lire un fichier, c'est-à-dire afficher le fichier en visualisant son
    contenu */
    @Override
    public byte[] lireFichier(String cheminFichier) throws IOException {
        Path cheminVersFichier = Paths.get(cheminFichier);
        return Files.readAllBytes(cheminVersFichier);
    }

    /* Fonction pour l'entête de, c'est-à-dire, de quel type de fichier,
    s'agit-il, ainsi de suite */
    @Override
    public HttpHeaders construireHeaders(String cheminFichier, long contentLength) throws UnsupportedFileTypeException {
        String contentType = fileFilter.determineContentType(cheminFichier);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(contentLength);

        return headers;
    }
}
