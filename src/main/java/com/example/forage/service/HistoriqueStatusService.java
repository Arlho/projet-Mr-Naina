package com.example.forage.service;

import com.example.forage.model.HistoriqueStatus;
import com.example.forage.repository.HistoriqueStatusRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueStatusService {

    private final HistoriqueStatusRepository historiqueStatusRepository;

    public HistoriqueStatusService(HistoriqueStatusRepository historiqueStatusRepository) {
        this.historiqueStatusRepository = historiqueStatusRepository;
    }

    public List<HistoriqueStatus> findAll() {
        return historiqueStatusRepository.findAll();
    }

    public Optional<HistoriqueStatus> findById(Long id) {
        return historiqueStatusRepository.findById(id);
    }

    public HistoriqueStatus save(HistoriqueStatus histo) {
        return historiqueStatusRepository.save(histo);
    }

    public void deleteById(Long id) {
        historiqueStatusRepository.deleteById(id);
    }

    public List<HistoriqueStatus> findAllByDemandeId(Long demandeId) {
        return historiqueStatusRepository.findByDemandeIdOrderByIdAsc(demandeId);
    }

    public HistoriqueStatus findLastStatusByDemandeId(Long demandeId) {
        return historiqueStatusRepository.findTopByDemandeIdOrderByIdDesc(demandeId);
    }
    

}
