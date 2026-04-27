package com.example.forage.service;

import com.example.forage.model.Demande;
import com.example.forage.model.HistoriqueStatus;
import com.example.forage.model.Status;
import com.example.forage.repository.DemandeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeService {

    private final HistoriqueStatusService historiqueService;
    private final StatusService statusService;
    private final DemandeRepository demandeRepository;

    public DemandeService(DemandeRepository demandeRepository, StatusService statusService, HistoriqueStatusService historiqueService) {
        this.demandeRepository = demandeRepository;
        this.statusService = statusService;
        this.historiqueService = historiqueService;
    }

    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    public Optional<Demande> findById(Long id) {
        return demandeRepository.findById(id);
    }

    @Transactional
    public Demande save(Demande demande) {
        if (demande.getDateDemande() == null) {
            demande.setDateDemande(LocalDate.now());
        }
        Demande res = demandeRepository.save(demande);
        Status status = statusService.findByLibelle("Demande créée").get();
        HistoriqueStatus nouvelleStatus = new HistoriqueStatus(null, demande, status,"premier status",LocalDateTime.now());
        historiqueService.save(nouvelleStatus);

        return res;
    }

    public void deleteById(Long id) {
        demandeRepository.deleteById(id);
    }

    
}
