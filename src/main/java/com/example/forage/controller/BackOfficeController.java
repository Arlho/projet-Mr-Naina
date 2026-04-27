package com.example.forage.controller;

import com.example.forage.model.Demande;
import com.example.forage.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class BackOfficeController {

    @Autowired
    private DemandeRepository demandeRepository;

    @GetMapping("/backoffice")
    public String dashboard() {
        return "redirect:/devis/liste";
    }

    // Endpoint AJAX pour récupérer les informations d'une demande
    @GetMapping("/api/demande/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDemandeInfo(@PathVariable Long id) {
        Optional<Demande> demandeOpt = demandeRepository.findById(id);
        
        if (demandeOpt.isPresent()) {
            Demande demande = demandeOpt.get();
            Map<String, Object> response = new HashMap<>();
            
            
            if (demande.getClient() != null) {
                response.put("clientNom", demande.getClient().getNom());
                response.put("clientContact", demande.getClient().getContact());
            } else {
                response.put("clientNom", "Inconnu");
                response.put("clientContact", "Inconnu");
            }
            
            response.put("lieu", demande.getLieu());
            response.put("district", demande.getDistrict());
            response.put("dateDemande", demande.getDateDemande() != null ? demande.getDateDemande().toString() : "");
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
