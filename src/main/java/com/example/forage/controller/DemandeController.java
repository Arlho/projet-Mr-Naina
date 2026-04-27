package com.example.forage.controller;

import com.example.forage.model.Client;
import com.example.forage.model.Demande;
import com.example.forage.model.Devis;
import com.example.forage.model.Status;
import com.example.forage.model.HistoriqueStatus;
import com.example.forage.repository.DevisRepository;
import com.example.forage.repository.HistoriqueStatusRepository;
import com.example.forage.service.DemandeService;
import com.example.forage.service.DevisService;
import com.example.forage.service.HistoriqueStatusService;
import com.example.forage.service.StatusService;

// import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Controller
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private HistoriqueStatusService historiqueStatusService;
    
    @Autowired
    private HistoriqueStatusRepository historiqueStatusRepository;

    @Autowired
    private DevisService devisService;

    @Autowired
    private  DevisRepository devisRepository;

    @Autowired
    private StatusService statusService;

    DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;

    }

    @PostMapping("/demande/details/{id}")
    public String dashboard(Model model , @PathVariable Long id) {
        Demande demande = demandeService.findById(id).get();
        List<HistoriqueStatus> statuses = historiqueStatusService.findAllByDemandeId(id);
        List<Devis> devises = devisService.findDevisByDemande(demande);
        Client client = demande.getClient();

        model.addAttribute("demande", demande);
        model.addAttribute("client", client);
        model.addAttribute("statuses", statuses);
        model.addAttribute("devises", devises);

        // Map pour stocker montant total par devis.id
        Map<Long, Double> montantTotalMap = new HashMap<>();

        for (Devis d : devises) {
            // somme des montants des détails du devis
            double total = devisRepository.findTotalById(d.getId());
            montantTotalMap.put(d.getId(), total);
        }

        model.addAttribute("montantTotalMap", montantTotalMap);

        return "detailsDemande";
    }
    
    @GetMapping("/demande/details/{id}")
    public String dashboardGet(Model model, @PathVariable Long id) {
        Demande demande = demandeService.findById(id).get();
        List<HistoriqueStatus> statuses = historiqueStatusService.findAllByDemandeId(id);
        List<Devis> devises = devisService.findDevisByDemande(demande);
        Client client = demande.getClient();

        model.addAttribute("demande", demande);
        model.addAttribute("client", client);
        model.addAttribute("statuses", statuses);
        model.addAttribute("devises", devises);

        // Map pour stocker montant total par devis.id
        Map<Long, Double> montantTotalMap = new HashMap<>();

        for (Devis d : devises) {
            // somme des montants des détails du devis
            double total = devisRepository.findTotalById(d.getId());
            montantTotalMap.put(d.getId(), total);
        }

        model.addAttribute("montantTotalMap", montantTotalMap);

        return "detailsDemande";
    }


    @PostMapping("/demande/majform/{id}")
    public String showUpdateForm(Model model, @PathVariable Long id) {
        Demande demande = demandeService.findById(id).get();
        List<HistoriqueStatus> statuses = historiqueStatusService.findAllByDemandeId(id);
        List<Status> optStatus = statusService.findAll();

        model.addAttribute("demande", demande);
        model.addAttribute("statuses", statuses);
        model.addAttribute("optionStatus", optStatus);

        return "modifDemandeForm";
    }

    @PostMapping("/demande/updateState/{id}")
    public String majStatusDemande(Model model,
        @RequestParam(value = "nouveauStatusId", required = false) Long nouveauStatusId,
        @RequestParam(value = "observation", required = false) String observation,
        @PathVariable Long id) {

        Demande demande = demandeService.findById(id).get();
        Status status = statusService.findById(nouveauStatusId).get();
        
        historiqueStatusService.save(new HistoriqueStatus(null,demande,status,observation,LocalDateTime.now()));

        return "redirect:/demande/details/" + id;
    }
    

    @PostMapping("/historique/update")
    public String majHistorique(Model model,
            @RequestParam(value = "id", required = false) Long idHisto,
            @RequestParam(value = "dateStatus", required = false) LocalDateTime newDate,
            @RequestParam(value = "observation", required = false) String newObs,
            @RequestParam(value = "idDemande", required = false) Long idDm
        ) {
            historiqueStatusRepository.updateCustomised(newDate, newObs,idHisto);

        return "redirect:/demande/details/" + idDm;
    }

}
