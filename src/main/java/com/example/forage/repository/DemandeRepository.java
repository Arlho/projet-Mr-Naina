package com.example.forage.repository;

import com.example.forage.model.Demande;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
}
