package com.example.forage.repository;

import com.example.forage.model.Client;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT count(c.id) from Client c")
    Long manisaClient();
}
