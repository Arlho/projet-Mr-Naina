package com.example.forage.repository;

import com.example.forage.model.HistoriqueStatusDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueStatusDevisRepository extends JpaRepository<HistoriqueStatusDevis, Long> {
}
