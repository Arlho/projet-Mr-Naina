package com.resaka.dao;

import com.resaka.model.Note;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    public List<Note> findAll() throws SQLException {
        List<Note> list = new ArrayList<>();
        String sql = "SELECT n.id, n.id_candidat, n.id_matiere, n.id_correcteur, n.valeur_note, "
                + "c.nom as candidat_nom, c.prenom as candidat_prenom, "
                + "m.nom as matiere_nom, cor.nom as correcteur_nom "
                + "FROM note n "
                + "JOIN candidat c ON n.id_candidat = c.id "
                + "JOIN matiere m ON n.id_matiere = m.id_matiere "
                + "JOIN correcteur cor ON n.id_correcteur = cor.id "
                + "ORDER BY n.id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Note n = new Note(rs.getInt("id"), rs.getInt("id_candidat"), rs.getInt("id_matiere"),
                        rs.getInt("id_correcteur"), rs.getDouble("valeur_note"));
                n.setCorrecteurNom(rs.getString("correcteur_nom"));
                n.setCandidatNom(rs.getString("candidat_nom") + " " + rs.getString("candidat_prenom"));
                n.setMatiereNom(rs.getString("matiere_nom"));
                list.add(n);
            }
        }
        return list;
    }

    public Note findById(int id) throws SQLException {
        String sql = "SELECT id, id_candidat, id_matiere, id_correcteur, valeur_note FROM note WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Note(rs.getInt("id"), rs.getInt("id_candidat"), rs.getInt("id_matiere"),
                            rs.getInt("id_correcteur"), rs.getDouble("valeur_note"));
                }
            }
        }
        return null;
    }

    public List<Note> findByCandidatAndMatiere(int idCandidat, int idMatiere) throws SQLException {
        List<Note> list = new ArrayList<>();
        String sql = "SELECT n.id, n.id_candidat, n.id_matiere, n.id_correcteur, n.valeur_note, c.nom as correcteur_nom "
                + "FROM note n JOIN correcteur c ON n.id_correcteur = c.id "
                + "WHERE n.id_candidat = ? AND n.id_matiere = ? ORDER BY c.nom";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCandidat);
            ps.setInt(2, idMatiere);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Note n = new Note();
                    n.setId(rs.getInt("id"));
                    n.setIdCandidat(rs.getInt("id_candidat"));
                    n.setIdMatiere(rs.getInt("id_matiere"));
                    n.setIdCorrecteur(rs.getInt("id_correcteur"));
                    n.setValeurNote(rs.getDouble("valeur_note"));
                    n.setCorrecteurNom(rs.getString("correcteur_nom"));
                    list.add(n);
                }
            }
        }
        return list;
    }

    public void insert(int idCandidat, int idMatiere, int idCorrecteur, double valeurNote) throws SQLException {
        String sql = "INSERT INTO note (id_candidat, id_matiere, id_correcteur, valeur_note) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCandidat);
            ps.setInt(2, idMatiere);
            ps.setInt(3, idCorrecteur);
            ps.setDouble(4, valeurNote);
            ps.executeUpdate();
        }
    }

    public void update(int id, int idCandidat, int idMatiere, int idCorrecteur, double valeurNote) throws SQLException {
        String sql = "UPDATE note SET id_candidat=?, id_matiere=?, id_correcteur=?, valeur_note=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCandidat);
            ps.setInt(2, idMatiere);
            ps.setInt(3, idCorrecteur);
            ps.setDouble(4, valeurNote);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM note WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
