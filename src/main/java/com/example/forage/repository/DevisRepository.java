package com.example.forage.repository;

import com.example.forage.model.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Long> {

    // Récupérer tous les devis par demande
    List<Devis> findByDemandeId(Long demandeId);

    // Total par devis (liste des id de devis et leur total)
    @Query("SELECT d.devis.id, SUM(d.montant) FROM DetailsDevis d GROUP BY d.devis.id")
    List<Object[]> findTotalParDevis();

    // Total pour un devis spécifique
    @Query("SELECT SUM(d.montant) FROM DetailsDevis d WHERE d.devis.id = :devisId")
    Long findTotalById(Long devisId);

    @Query("SELECT SUM(d.montant) FROM DetailsDevis d")
    Long findTotal();

}