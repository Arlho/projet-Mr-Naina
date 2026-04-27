package com.example.forage.config;

import com.example.forage.model.Status;
import com.example.forage.model.TypeDevis;
import com.example.forage.repository.StatusRepository;
import com.example.forage.repository.TypeDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StatusRepository statusDevisRepository;

    @Autowired
    private TypeDevisRepository typeDevisRepository;

    @Override
    public void run(String... args) throws Exception {
        if (statusDevisRepository.count() == 0) {
            statusDevisRepository.save(createStatus("Demande créée"));
            statusDevisRepository.save(createStatus("Devis examination créée"));
            statusDevisRepository.save(createStatus("Devis forage créée"));
            statusDevisRepository.save(createStatus("Devis examination refusée"));
            statusDevisRepository.save(createStatus("Devis examination acceptée"));
            statusDevisRepository.save(createStatus("Devis forage refusée"));
            statusDevisRepository.save(createStatus("Devis forage acceptée"));
        }

        if (typeDevisRepository.count() == 0) {
            typeDevisRepository.save(createType("Devis forage"));
            typeDevisRepository.save(createType("Devis d'examination"));
        }
    }

    private Status createStatus(String libelle) {
        Status s = new Status();
        s.setLibelle(libelle);
        return s;
    }

    private TypeDevis createType(String libelle) {
        TypeDevis t = new TypeDevis();
        t.setLibelle(libelle);
        return t;
    }
}
