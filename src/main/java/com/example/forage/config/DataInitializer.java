package com.example.forage.config;

import com.example.forage.model.StatusDevis;
import com.example.forage.model.TypeDevis;
import com.example.forage.repository.StatusDevisRepository;
import com.example.forage.repository.TypeDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StatusDevisRepository statusDevisRepository;

    @Autowired
    private TypeDevisRepository typeDevisRepository;

    @Override
    public void run(String... args) throws Exception {
        if (statusDevisRepository.count() == 0) {
            statusDevisRepository.save(createStatus("Envoyé"));
            statusDevisRepository.save(createStatus("Accepte"));
            statusDevisRepository.save(createStatus("Refuser"));
        }

        if (typeDevisRepository.count() == 0) {
            typeDevisRepository.save(createType("Devis forage"));
            typeDevisRepository.save(createType("Devis d'examination"));
        }
    }

    private StatusDevis createStatus(String libelle) {
        StatusDevis s = new StatusDevis();
        s.setLibelle(libelle);
        return s;
    }

    private TypeDevis createType(String libelle) {
        TypeDevis t = new TypeDevis();
        t.setLibelle(libelle);
        return t;
    }
}
