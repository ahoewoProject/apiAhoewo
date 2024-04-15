package com.memoire.apiAhoewo.services.gestionDesComptes;

import com.memoire.apiAhoewo.models.gestionDesComptes.Client;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientService {
    public List<Client> getAll();

    Page<Client> getClients(int numeroDeLaPage, int elementsParPage);

    public Client findById(Long id);

    public void deleteById(Long id);

    public int countClients();
}
