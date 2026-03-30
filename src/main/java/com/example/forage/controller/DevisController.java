package com.example.forage.controller;

import com.example.forage.model.Demande;
import com.example.forage.model.DetailsDevis;
import com.example.forage.model.Devis;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.service.DetailsDevisService;
import com.example.forage.service.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/devis")
public class DevisController {

    @Autowired
    private DevisService devisService;

    @Autowired
    private DetailsDevisService detailsDevisService;

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private com.example.forage.service.StatusDevisService statusDevisService;

    @Autowired
    private com.example.forage.repository.HistoriqueStatusDevisRepository historiqueStatusDevisRepository;

    @Autowired
    private com.example.forage.service.TypeDevisService typeDevisService;

    @GetMapping("/liste")
    public String listeDevis(Model model) {
        model.addAttribute("devisList", devisService.findAll());
        model.addAttribute("allStatus", statusDevisService.findAll());
        return "dashboardBack";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("demandes", demandeRepository.findAll());
        model.addAttribute("typesDevis", typeDevisService.findAll());
        return "devisForm";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        Optional<Devis> devisOpt = devisService.findById(id);
        if (devisOpt.isPresent()) {
            model.addAttribute("devis", devisOpt.get());
            model.addAttribute("detailsList", detailsDevisService.findByDevisId(id));
            return "devisDetails";
        }
        return "redirect:/devis/liste";
    }

    @PostMapping("/save")
    public String saveDevis(
            @RequestParam("demande.id") Long demandeId,
            @RequestParam(value = "typeDevis.id", required = false) Long typeDevisId,
            @RequestParam(value = "detailsLibelles", required = false) String[] libelles,
            @RequestParam(value = "detailsMontants", required = false) BigDecimal[] montants
    ) {
        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
        
        if (demandeOpt.isPresent()) {
            Demande demande = demandeOpt.get();
            
            Devis devis = new Devis();
            devis.setDemande(demande);
            devis.setDateDevis(LocalDate.now());
            devis.setMontantTotal(BigDecimal.ZERO); 
            
            if (typeDevisId != null) {
                typeDevisService.findById(typeDevisId).ifPresent(devis::setTypeDevis);
            }
            statusDevisService.findByLibelle("Envoyé").ifPresent(devis::setStatusDevis);
            
            devis = devisService.save(devis);
            
            if (devis.getStatusDevis() != null) {
                com.example.forage.model.HistoriqueStatusDevis h = new com.example.forage.model.HistoriqueStatusDevis();
                h.setDevis(devis);
                h.setStatusDevis(devis.getStatusDevis());
                h.setDateStatus(LocalDate.now());
                historiqueStatusDevisRepository.save(h);
            }
            
            BigDecimal grandTotal = BigDecimal.ZERO;

            if (libelles != null && montants != null && libelles.length == montants.length) {
                for (int i = 0; i < libelles.length; i++) {
                    String libelle = libelles[i];
                    BigDecimal montant = montants[i];

                    if (libelle != null && !libelle.trim().isEmpty() && montant != null) {
                        DetailsDevis detail = new DetailsDevis();
                        detail.setDevis(devis);
                        detail.setLibelle(libelle);
                        detail.setMontant(montant);
                        
                        detailsDevisService.save(detail);
                        grandTotal = grandTotal.add(montant);
                    }
                }
            }

            devis.setMontantTotal(grandTotal);
            devisService.save(devis);
        }

        return "redirect:/devis/liste";
    }

    @PostMapping("/update-status")
    public String updateDevisStatus(@RequestParam("devisId") Long devisId, @RequestParam("statusId") Long statusId) {
        Optional<Devis> devisOpt = devisService.findById(devisId);
        Optional<com.example.forage.model.StatusDevis> statusOpt = statusDevisService.findById(statusId);

        if (devisOpt.isPresent() && statusOpt.isPresent()) {
            Devis devis = devisOpt.get();
            com.example.forage.model.StatusDevis status = statusOpt.get();
            
            devis.setStatusDevis(status);
            devisService.save(devis);
            
            com.example.forage.model.HistoriqueStatusDevis historique = new com.example.forage.model.HistoriqueStatusDevis();
            historique.setDevis(devis);
            historique.setStatusDevis(status);
            historique.setDateStatus(LocalDate.now());
            historiqueStatusDevisRepository.save(historique);
        }
        
        return "redirect:/devis/liste";
    }
}
