package com.example.forage.service;

import com.example.forage.model.Devis;
import com.example.forage.repository.DevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DevisService {

    private final DevisRepository devisRepository;

    @Autowired
    public DevisService(DevisRepository devisRepository) {
        this.devisRepository = devisRepository;
    }

    public List<Devis> findAll() {
        return devisRepository.findAll();
    }

    public Optional<Devis> findById(Long id) {
        return devisRepository.findById(id);
    }

    public Devis save(Devis devis) {
        return devisRepository.save(devis);
    }

    public void deleteById(Long id) {
        devisRepository.deleteById(id);
    }
}
