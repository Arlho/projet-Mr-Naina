package com.example.forage.repository;

import com.example.forage.model.DetailsDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetailsDevisRepository extends JpaRepository<DetailsDevis, Long> {
    List<DetailsDevis> findByDevisId(Long devisId);
    void deleteByDevisId(Long devisId);
}
