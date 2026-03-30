package com.example.forage.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueStatusDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "devis_id")
    private Devis devis;

    @ManyToOne
    @JoinColumn(name = "status_devis_id")
    private StatusDevis statusDevis;

    private LocalDate dateStatus = LocalDate.now();
}
