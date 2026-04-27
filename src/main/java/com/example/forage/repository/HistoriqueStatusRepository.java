package com.example.forage.repository;

import com.example.forage.model.HistoriqueStatus;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HistoriqueStatusRepository extends JpaRepository<HistoriqueStatus, Long> {
    List<HistoriqueStatus> findByDemandeId(Long demandeId);
    List<HistoriqueStatus> findByDemandeIdOrderByDateStatusDesc(Long demandeId);
    HistoriqueStatus findTopByDemandeIdOrderByDateStatusDesc(Long demandeId);
    
    HistoriqueStatus findTopByDemandeIdOrderByIdDesc(Long demandeId);
    
    List<HistoriqueStatus> findByDemandeIdOrderByIdAsc(Long demandeId);

@Modifying
@Transactional
@Query("UPDATE HistoriqueStatus h SET h.dateStatus = :newDt, h.observation = :newObs WHERE h.id = :id")
void updateCustomised(
    @Param("newDt") LocalDateTime newDt,
    @Param("newObs") String newObs,
    @Param("id") Long id
);

@Query("SELECT h FROM HistoriqueStatus h WHERE h.status.id = 1")
List<HistoriqueStatus> rehetraDmCree();

@Query("SELECT h FROM HistoriqueStatus h WHERE h.status.id = 2")
List<HistoriqueStatus> rehetraDexCree();

@Query("SELECT h FROM HistoriqueStatus h WHERE h.status.id = 3")
List<HistoriqueStatus> rehetraDefCree();

@Query("SELECT h FROM HistoriqueStatus h WHERE h.status.id = 4")
List<HistoriqueStatus> rehetraDexRef();

@Query("SELECT h FROM HistoriqueStatus h WHERE h.status.id = 5")
List<HistoriqueStatus> rehetraDexAcc();

@Query("SELECT h FROM HistoriqueStatus h WHERE h.status.id = 6")
List<HistoriqueStatus> rehetraDefRef();

@Query("SELECT h FROM HistoriqueStatus h WHERE h.status.id = 7")
List<HistoriqueStatus> rehetraDefAcc();

}
