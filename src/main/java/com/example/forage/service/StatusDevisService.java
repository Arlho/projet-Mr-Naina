package com.example.forage.service;

import com.example.forage.model.StatusDevis;
import com.example.forage.repository.StatusDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusDevisService {

    private final StatusDevisRepository statusDevisRepository;

    @Autowired
    public StatusDevisService(StatusDevisRepository statusDevisRepository) {
        this.statusDevisRepository = statusDevisRepository;
    }

    public List<StatusDevis> findAll() {
        return statusDevisRepository.findAll();
    }

    public Optional<StatusDevis> findById(Long id) {
        return statusDevisRepository.findById(id);
    }

    public StatusDevis save(StatusDevis statusDevis) {
        return statusDevisRepository.save(statusDevis);
    }

    public void deleteById(Long id) {
        statusDevisRepository.deleteById(id);
    }

    public Optional<StatusDevis> findByLibelle(String libelle) {
        return statusDevisRepository.findByLibelleIgnoreCase(libelle);
    }
}
