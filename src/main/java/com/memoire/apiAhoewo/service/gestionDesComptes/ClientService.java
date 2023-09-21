package com.memoire.apiAhoewo.service.gestionDesComptes;

import com.memoire.apiAhoewo.model.gestionDesComptes.Client;

import java.util.List;

public interface ClientService {
    public List<Client> getAll();

    public Client findById(Long id);

    public void deleteById(Long id);

    public int countClients();
}
