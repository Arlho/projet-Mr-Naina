package com.example.forage.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal montantTotal = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "typedevis_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TypeDevis typeDevis;

    private LocalDate dateDevis = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "demande_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "status_devis_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private StatusDevis statusDevis;
}
