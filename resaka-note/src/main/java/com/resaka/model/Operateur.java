package com.resaka.model;

public class Operateur {
    private int id;
    private String nom;
    private String symbole;

    public Operateur() {}

    public Operateur(int id, String nom, String symbole) {
        this.id = id;
        this.nom = nom;
        this.symbole = symbole;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getSymbole() { return symbole; }
    public void setSymbole(String symbole) { this.symbole = symbole; }
}
