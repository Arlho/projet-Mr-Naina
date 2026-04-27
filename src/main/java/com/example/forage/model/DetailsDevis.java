package com.example.forage.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "detailsdevis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "devis_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Devis devis;

    @Column(nullable = false)
    private BigDecimal pu;

    @Column(nullable = false)
    private BigDecimal quantite;

    private String libelle;

    @Column(nullable = false)
    private BigDecimal montant;

    @Column(nullable = false)
    private BigDecimal montantOrigine;
}
