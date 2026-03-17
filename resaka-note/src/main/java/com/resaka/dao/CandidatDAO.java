package com.resaka.dao;

import com.resaka.model.Candidat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatDAO {

    public List<Candidat> findAll() throws SQLException {
        List<Candidat> list = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, matricule FROM candidat ORDER BY nom, prenom";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Candidat(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("matricule")));
            }
        }
        return list;
    }

    public Candidat findById(int id) throws SQLException {
        String sql = "SELECT id, nom, prenom, matricule FROM candidat WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Candidat(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("matricule"));
                }
            }
        }
        return null;
    }

    public void insert(String nom, String prenom, String matricule) throws SQLException {
        String sql = "INSERT INTO candidat (nom, prenom, matricule) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, matricule);
            ps.executeUpdate();
        }
    }

    public void update(int id, String nom, String prenom, String matricule) throws SQLException {
        String sql = "UPDATE candidat SET nom=?, prenom=?, matricule=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, matricule);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM candidat WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
