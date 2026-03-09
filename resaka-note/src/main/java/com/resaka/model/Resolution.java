package com.resaka.model;

public class Resolution {
    private int id;
    private String description;
    private double resultat;

    public Resolution() {}

    public Resolution(int id, String description, double resultat) {
        this.id = id;
        this.description = description;
        this.resultat = resultat;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getResultat() { return resultat; }
    public void setResultat(double resultat) { this.resultat = resultat; }
}
