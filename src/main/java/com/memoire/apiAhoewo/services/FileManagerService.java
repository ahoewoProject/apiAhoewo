package com.memoire.apiAhoewo.services;

import com.memoire.apiAhoewo.exceptions.UnsupportedFileTypeException;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public interface FileManagerService {
    public byte[] lireFichier(String cheminFichier) throws IOException;

    public HttpHeaders construireHeaders(String cheminFichier, long contentLength) throws UnsupportedFileTypeException;
}
