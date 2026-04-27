package com.example.forage.controller;

import com.example.forage.model.Demande;
import com.example.forage.model.DetailsDevis;
import com.example.forage.model.Devis;
import com.example.forage.model.TypeDevis;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.repository.DevisRepository;
import com.example.forage.service.DemandeService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private DemandeService demandeService;

    @Autowired
    private com.example.forage.service.TypeDevisService typeDevisService;

    @Autowired
    private  DevisRepository devisRepository;

    DevisController() {
    }

    @GetMapping("/liste")
    public String listeDevis(Model model) {
        List<Devis> devisList = devisService.findAll();

        // Map pour stocker montant total par devis.id
        Map<Long, Double> montantTotalMap = new HashMap<>();

        for (Devis d : devisList) {
            // somme des montants des détails du devis
            double total = devisRepository.findTotalById(d.getId());
            montantTotalMap.put(d.getId(), total);
        }
        model.addAttribute("chiffreAffaire", devisRepository.findTotal());
        model.addAttribute("devisList", devisList);
        model.addAttribute("montantTotalMap", montantTotalMap);

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
            model.addAttribute("montantTotal", devisRepository.findTotalById(id));
            return "devisDetails";
        }
        return "redirect:/devis/liste";
    }

    @PostMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        Devis devis = devisService.findById(id).get();
        Long idDemande = devis.getDemande().getId(); // 🔥 récupère AVANT suppression

        devisService.supprimerTousAbout(devis);

        return "redirect:/demande/details/" + idDemande;
    }

    @PostMapping("/save")
    public String saveDevis(
            @RequestParam("demande.id") Long demandeId,
            @RequestParam(value = "typeDevis.id", required = false) Long typeDevisId,
            @RequestParam(value = "detailsLibelles", required = false) String[] libelles,
            @RequestParam(value = "detailspu", required = false) BigDecimal[] pus,
            @RequestParam(value = "detailsquantite", required = false) BigDecimal[] quantites
            ) {

        Demande demandeOpt = demandeService.findById(demandeId).get();
        TypeDevis typeDevis = typeDevisService.findById(typeDevisId).get();
        List<DetailsDevis> listeDetailsDevis = new ArrayList<DetailsDevis>();
            
        Devis devis = new Devis(null, typeDevis, LocalDate.now(),demandeOpt);
        devis = devisService.save(devis);

        if (libelles != null && quantites != null && libelles.length == quantites.length) {
            for (int i = 0; i < libelles.length; i++) {
                String libelle = libelles[i];
                BigDecimal pu = pus[i];
                BigDecimal montant = pu.multiply(quantites[i]);

                if (libelle != null && !libelle.trim().isEmpty() && montant != null) {
                    DetailsDevis detail = new DetailsDevis();
                    detail.setDevis(devis);
                    detail.setLibelle(libelle);
                    detail.setPu(pu);
                    detail.setQuantite(quantites[i]);
                    detail.setMontant(montant);
                    detail.setMontantOrigine(montant);
                    
                    listeDetailsDevis.add(detail);
                }
            }
        }

        devisService.enregistrerAll(devis, listeDetailsDevis);

        return "redirect:/devis/liste";
    }

}
