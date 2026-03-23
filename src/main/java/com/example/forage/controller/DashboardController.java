package com.example.forage.controller;

import com.example.forage.model.Client;
import com.example.forage.model.Demande;
import com.example.forage.repository.ClientRepository;
import com.example.forage.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class DashboardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DemandeRepository demandeRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("demandes", demandeRepository.findAll());
        model.addAttribute("newClient", new Client());
        
        Demande d = new Demande();
        d.setClient(new Client()); // Initialisation pour éviter le NullPointer dans th:field="*{client.id}"
        model.addAttribute("newDemande", d);
        
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
        demandeRepository.save(demande);
        return "redirect:/";
    }

    @PostMapping("/demande/delete/{id}")
    public String deleteDemande(@PathVariable Long id) {
        demandeRepository.deleteById(id);
        return "redirect:/";
    }
}
