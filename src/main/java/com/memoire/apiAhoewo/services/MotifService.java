package com.memoire.apiAhoewo.services;

import com.memoire.apiAhoewo.models.Motif;

import java.security.Principal;
import java.util.List;

public interface MotifService {
    List<Motif> getMotifsByCodeAndCreerPar(String code, Long creerPar);

    Motif findByCode(String code);

    Motif save(Motif motif, Principal principal);
}
