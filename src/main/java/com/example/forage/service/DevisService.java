package com.example.forage.service;

import com.example.forage.model.Client;
import com.example.forage.model.Demande;
import com.example.forage.model.DetailsDevis;
import com.example.forage.model.Devis;
import com.example.forage.model.HistoriqueStatus;
import com.example.forage.model.Status;
import com.example.forage.repository.DetailsDevisRepository;
import com.example.forage.repository.DevisRepository;

import com.example.forage.repository.HistoriqueStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DevisService {
    private final DetailsDevisRepository detailsDevisRepository;
    private final HistoriqueStatusService historiqueStatusService;
    private final DetailsDevisService detailsDevisService;;
    private final DevisRepository devisRepository;
    private final ClientService clientService;
    private final StatusService statutService;

    public DevisService(StatusService statutService, DevisRepository devisRepository, DetailsDevisService detailsDevisService, ClientService clientService, HistoriqueStatusService historiqueStatusService, DetailsDevisRepository detailsDevisRepository, HistoriqueStatusRepository historiqueStatusRepository) {
        this.devisRepository = devisRepository;
        this.detailsDevisService = detailsDevisService;
        this.clientService = clientService;
        this.historiqueStatusService = historiqueStatusService;
        this.statutService = statutService;
        this.detailsDevisRepository = detailsDevisRepository;
    }

    public List<Devis> findAll() {
        return devisRepository.findAll();
    }

    public Optional<Devis> findById(Long id) {
        return devisRepository.findById(id);
    }

    public Devis save(Devis devis) {
        return devisRepository.save(devis);
    }

    public void deleteById(Long id) {
        devisRepository.deleteById(id);
    }

    @Transactional
    public Devis enregistrerAll(Devis devis, List<DetailsDevis> detailsDevis){

        for (DetailsDevis detailDevis : detailsDevis) {
            String a = detailDevis.getPu().toString();
            int thePriceOfTheDetail = Integer.parseInt(a);

            int comparaison = 1000000;

            BigDecimal reduction = BigDecimal.valueOf(10);
            
            BigDecimal montantFinal = BigDecimal.ZERO;

            if(thePriceOfTheDetail - comparaison >= 0){

                BigDecimal reduc = (detailDevis.getPu().multiply(reduction)).divide(BigDecimal.valueOf(100)).multiply(detailDevis.getQuantite());
                
                montantFinal = detailDevis.getMontant().subtract(reduc);
            }else{
                montantFinal = detailDevis.getMontant();
            }
            detailDevis.setMontant(montantFinal);

            detailsDevisService.save(detailDevis);
        }

        System.err.println(devis.getTypeDevis().getLibelle());

        if ("Devis forage".equals(devis.getTypeDevis().getLibelle())) {
            Status status = statutService.findByLibelle("Devis forage créée").get();
            historiqueStatusService.save(new HistoriqueStatus(null, devis.getDemande(), status, "Premiere envoie", LocalDateTime.now()));
        }
        else if("Devis d'examination".equals(devis.getTypeDevis().getLibelle())){
            Status status = statutService.findByLibelle("Devis examination créée").get();
            historiqueStatusService.save(new HistoriqueStatus(null, devis.getDemande(), status,"Premiere envoie", LocalDateTime.now()));
        }

        return devisRepository.save(devis);
    }

    public Client findClientByDevis(Devis devis) {
        Long idc = clientService.findById(devis.getDemande().getClient().getId()).get().getId();

        return clientService.findById(idc).get();
    }

    public List<Devis> findDevisByDemande(Demande demande) {
        List<Devis> devises = devisRepository.findByDemandeId(demande.getId());

        return devises;
    }

    @Transactional
    public void supprimerTousAbout(Devis devis) {
        Long devisId = devis.getId();

        // 1. Supprimer les détails du devis
        detailsDevisRepository.deleteByDevisId(devisId);

        // 2. Supprimer les historiques liés à la demande (optionnel mais conseillé)
        // historiqueStatusRepository.delete

        // 3. Supprimer le devis
        devisRepository.deleteById(devisId);
    }



}
