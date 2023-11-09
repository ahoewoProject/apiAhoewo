package com.memoire.apiAhoewo.service;

import com.memoire.apiAhoewo.exception.UnsupportedFileTypeException;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public interface FileManagerService {

    public byte[] lireFichier(String cheminFichier) throws IOException;

    public HttpHeaders construireHeaders(String cheminFichier, long contentLength) throws UnsupportedFileTypeException;
}
