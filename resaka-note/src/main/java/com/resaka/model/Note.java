package com.resaka.model;

public class Note {
    private int id;
    private int idCandidat;
    private int idMatiere;
    private int idCorrecteur;
    private double valeurNote;
    private String correcteurNom; 
    private String candidatNom;   
    private String matiereNom;    

    public Note() {}

    public Note(int id, int idCandidat, int idMatiere, int idCorrecteur, double valeurNote) {
        this.id = id;
        this.idCandidat = idCandidat;
        this.idMatiere = idMatiere;
        this.idCorrecteur = idCorrecteur;
        this.valeurNote = valeurNote;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdCandidat() { return idCandidat; }
    public void setIdCandidat(int idCandidat) { this.idCandidat = idCandidat; }
    public int getIdMatiere() { return idMatiere; }
    public void setIdMatiere(int idMatiere) { this.idMatiere = idMatiere; }
    public int getIdCorrecteur() { return idCorrecteur; }
    public void setIdCorrecteur(int idCorrecteur) { this.idCorrecteur = idCorrecteur; }
    public double getValeurNote() { return valeurNote; }
    public void setValeurNote(double valeurNote) { this.valeurNote = valeurNote; }
    public String getCorrecteurNom() { return correcteurNom; }
    public void setCorrecteurNom(String correcteurNom) { this.correcteurNom = correcteurNom; }
    public String getCandidatNom() { return candidatNom; }
    public void setCandidatNom(String candidatNom) { this.candidatNom = candidatNom; }
    public String getMatiereNom() { return matiereNom; }
    public void setMatiereNom(String matiereNom) { this.matiereNom = matiereNom; }
}
