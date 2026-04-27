package com.example.forage.repository;

import com.example.forage.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByLibelleIgnoreCase(String libelle);
    @Query("SELECT count(id) from Status")
    Long manisaStatus();
}
