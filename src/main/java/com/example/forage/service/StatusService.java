package com.example.forage.service;

import com.example.forage.model.Status;
import com.example.forage.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    public Optional<Status> findById(Long id) {
        return statusRepository.findById(id);
    }

    public Status save(Status statusDevis) {
        return statusRepository.save(statusDevis);
    }

    public void deleteById(Long id) {
        statusRepository.deleteById(id);
    }

    public Optional<Status> findByLibelle(String libelle) {
        return statusRepository.findByLibelleIgnoreCase(libelle);
    }

}
