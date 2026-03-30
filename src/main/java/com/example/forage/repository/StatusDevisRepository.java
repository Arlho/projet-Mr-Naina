package com.example.forage.repository;

import com.example.forage.model.StatusDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusDevisRepository extends JpaRepository<StatusDevis, Long> {
    Optional<StatusDevis> findByLibelleIgnoreCase(String libelle);
}
