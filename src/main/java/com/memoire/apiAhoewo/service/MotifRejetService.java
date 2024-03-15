package com.memoire.apiAhoewo.service;

import com.memoire.apiAhoewo.model.MotifRejet;

import java.security.Principal;

public interface MotifRejetService {
    MotifRejet findByCode(String code);

    MotifRejet save(MotifRejet motifRejet, Principal principal);
}
