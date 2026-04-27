package com.example.forage.service;

import com.example.forage.model.TypeDevis;
import com.example.forage.repository.TypeDevisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeDevisService {

    private final TypeDevisRepository typeDevisRepository;

    public TypeDevisService(TypeDevisRepository typeDevisRepository) {
        this.typeDevisRepository = typeDevisRepository;
    }

    public List<TypeDevis> findAll() {
        return typeDevisRepository.findAll();
    }

    public Optional<TypeDevis> findById(Long id) {
        return typeDevisRepository.findById(id);
    }

    public TypeDevis save(TypeDevis typeDevis) {
        return typeDevisRepository.save(typeDevis);
    }

    public void deleteById(Long id) {
        typeDevisRepository.deleteById(id);
    }

    public Optional<TypeDevis> findByLibelle(String libelle) {
        return typeDevisRepository.findByLibelleIgnoreCase(libelle);
    }
}
