package com.example.forage.controller;

import com.example.forage.model.Client;
import com.example.forage.model.Demande;
import com.example.forage.model.HistoriqueStatus;
import com.example.forage.repository.ClientRepository;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.repository.DevisRepository;
import com.example.forage.repository.HistoriqueStatusRepository;
import com.example.forage.repository.StatusRepository;
import com.example.forage.service.DemandeService;
import com.example.forage.service.HistoriqueStatusService;

// import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
public class DashboardController {

    private final HistoriqueStatusService historiqueStatusService;

    private final DemandeService demandeService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private HistoriqueStatusRepository historiqueStatusRepository;

    DashboardController(DemandeService demandeService, HistoriqueStatusService historiqueStatusService, 
            HistoriqueStatusRepository historiqueStatusRepository) {
        this.demandeService = demandeService;
        this.historiqueStatusService = historiqueStatusService;
        this.historiqueStatusRepository = historiqueStatusRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Demande> demandes = demandeService.findAll();
        Map<Long, HistoriqueStatus> lastStatuses = new HashMap<>();
        model.addAttribute("clients", clientRepository.findAll());

        for (Demande d : demandes) {
            HistoriqueStatus hs = historiqueStatusService.findLastStatusByDemandeId(Long.valueOf(d.getId()));
            System.out.println(hs.getStatus().getLibelle());
            lastStatuses.put(d.getId(), hs);
        }

        model.addAttribute("demandes", demandes);
        model.addAttribute("lastStatuses", lastStatuses);

        return "dashboard";
    }

    @PostMapping("/client/save")
    public String saveClient(@ModelAttribute Client client) {
        clientRepository.save(client);
        return "redirect:/";
    }


    @PostMapping("/client/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/demande/save")
    public String saveDemande(@ModelAttribute Demande demande) {
        if (demande.getDateDemande() == null) {
            demande.setDateDemande(LocalDate.now());
        }
        demandeService.save(demande);
        return "redirect:/";
    }

    @PostMapping("/demande/delete/{id}")
    public String deleteDemande(@PathVariable Long id) {
        demandeRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/statistique")
    public String stat(Model model) {
        model.addAttribute("nombreStatus", statusRepository.manisaStatus());
        model.addAttribute("nombreClient", clientRepository.manisaClient());
        model.addAttribute("chiffreAffaire", devisRepository.findTotal());
        model.addAttribute("listeDemande", demandeRepository.findAll());

        // Listes complètes pour les sections cliquables
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("allStatus", statusRepository.findAll());

        model.addAttribute("demandeCree", historiqueStatusRepository.rehetraDmCree());
        model.addAttribute("examinationCree", historiqueStatusRepository.rehetraDexCree());
        model.addAttribute("forageCree", historiqueStatusRepository.rehetraDefCree());
        model.addAttribute("forageRefuse", historiqueStatusRepository.rehetraDefRef());
        model.addAttribute("forageAccepte", historiqueStatusRepository.rehetraDefAcc());
        model.addAttribute("examinationRefuse", historiqueStatusRepository.rehetraDexRef());
        model.addAttribute("examinationAccepte", historiqueStatusRepository.rehetraDexAcc());

        model.addAttribute("nbdemandeCree", historiqueStatusRepository.rehetraDmCree().size());
        model.addAttribute("nbexaminationCree", historiqueStatusRepository.rehetraDexCree().size());
        model.addAttribute("nbforageCree", historiqueStatusRepository.rehetraDefCree().size());
        model.addAttribute("nbforageRefuse", historiqueStatusRepository.rehetraDefRef().size());
        model.addAttribute("nbforageAccepte", historiqueStatusRepository.rehetraDefAcc().size());
        model.addAttribute("nbexaminationRefuse", historiqueStatusRepository.rehetraDexRef().size());
        model.addAttribute("nbexaminationAccepte", historiqueStatusRepository.rehetraDexAcc().size());

        return "statistique";
    }

}
