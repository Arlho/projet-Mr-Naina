package com.example.forage.repository;

import com.example.forage.model.TypeDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeDevisRepository extends JpaRepository<TypeDevis, Long> {
    Optional<TypeDevis> findByLibelleIgnoreCase(String libelle);
}
