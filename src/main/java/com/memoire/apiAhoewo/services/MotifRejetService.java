package com.memoire.apiAhoewo.services;

import com.memoire.apiAhoewo.models.MotifRejet;

import java.security.Principal;
import java.util.List;

public interface MotifRejetService {
    List<MotifRejet> getMotifsByCodeAndCreerPar(String code, Long creerPar);

    MotifRejet findByCode(String code);

    MotifRejet save(MotifRejet motifRejet, Principal principal);
}
