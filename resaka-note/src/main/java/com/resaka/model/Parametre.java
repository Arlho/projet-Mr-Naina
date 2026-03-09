package com.resaka.model;

public class Parametre {
    private int id;
    private int idOperateur;
    private int idMatiere;
    private int min;
    private int max;
    private Operateur operateur; // joined field

    public Parametre() {}

    public Parametre(int id, int idOperateur, int idMatiere, int min, int max) {
        this.id = id;
        this.idOperateur = idOperateur;
        this.idMatiere = idMatiere;
        this.min = min;
        this.max = max;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdOperateur() { return idOperateur; }
    public void setIdOperateur(int idOperateur) { this.idOperateur = idOperateur; }
    public int getIdMatiere() { return idMatiere; }
    public void setIdMatiere(int idMatiere) { this.idMatiere = idMatiere; }
    public int getMin() { return min; }
    public void setMin(int min) { this.min = min; }
    public int getMax() { return max; }
    public void setMax(int max) { this.max = max; }
    public Operateur getOperateur() { return operateur; }
    public void setOperateur(Operateur operateur) { this.operateur = operateur; }
}
