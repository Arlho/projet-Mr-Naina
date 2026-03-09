package com.resaka.model;

public class Matiere {
    private int idMatiere;
    private String nom;
    private double coefficient;

    public Matiere() {}

    public Matiere(int idMatiere, String nom, double coefficient) {
        this.idMatiere = idMatiere;
        this.nom = nom;
        this.coefficient = coefficient;
    }

    public int getIdMatiere() { return idMatiere; }
    public void setIdMatiere(int idMatiere) { this.idMatiere = idMatiere; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getCoefficient() { return coefficient; }
    public void setCoefficient(double coefficient) { this.coefficient = coefficient; }
}
