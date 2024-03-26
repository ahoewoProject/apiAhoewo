package com.memoire.apiAhoewo.service;

import com.memoire.apiAhoewo.model.MotifRejet;

import java.security.Principal;
import java.util.List;

public interface MotifRejetService {
    List<MotifRejet> getMotifsByCodeAndCreerPar(String code, Long creerPar);

    MotifRejet findByCode(String code);

    MotifRejet save(MotifRejet motifRejet, Principal principal);
}
