package com.example.forage.service;

import com.example.forage.model.DetailsDevis;
import com.example.forage.repository.DetailsDevisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetailsDevisService {

    private final DetailsDevisRepository detailsDevisRepository;

    
    public DetailsDevisService(DetailsDevisRepository detailsDevisRepository) {
        this.detailsDevisRepository = detailsDevisRepository;
    }

    public List<DetailsDevis> findAll() {
        return detailsDevisRepository.findAll();
    }


    public Optional<DetailsDevis> findById(Long id) {
        return detailsDevisRepository.findById(id);
    }

    public DetailsDevis save(DetailsDevis detailsDevis) {
        return detailsDevisRepository.save(detailsDevis);
    }

    public void deleteById(Long id) {
        detailsDevisRepository.deleteById(id);
    }

    public List<DetailsDevis> findByDevisId(Long devisId) {
        return detailsDevisRepository.findByDevisId(devisId);
    }
}
