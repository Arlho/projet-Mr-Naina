package com.example.forage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.forage.model.Client;
import com.example.forage.repository.ClientRepository;
@Service
public class ClientService {
    
    private final ClientRepository clientService;

    
    public ClientService(ClientRepository clientService) {
        this.clientService = clientService;
    }

    public List<Client> findAll() {
        return clientService.findAll();
    }

    public Optional<Client> findById(Long id) {
        return clientService.findById(id);
    }

    public Client save(Client client) {
        return clientService.save(client);
    }

    public void deleteById(Long id) {
        clientService.deleteById(id);
    }

}
