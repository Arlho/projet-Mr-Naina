package com.resaka.dao;

import com.resaka.model.Matiere;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatiereDAO {

    public List<Matiere> findAll() throws SQLException {
        List<Matiere> list = new ArrayList<>();
        String sql = "SELECT id_matiere, nom, coefficient FROM matiere ORDER BY nom";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Matiere(rs.getInt("id_matiere"), rs.getString("nom"), rs.getDouble("coefficient")));
            }
        }
        return list;
    }

    public Matiere findById(int id) throws SQLException {
        String sql = "SELECT id_matiere, nom, coefficient FROM matiere WHERE id_matiere = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Matiere(rs.getInt("id_matiere"), rs.getString("nom"), rs.getDouble("coefficient"));
                }
            }
        }
        return null;
    }

    public void insert(String nom, double coefficient) throws SQLException {
        String sql = "INSERT INTO matiere (nom, coefficient) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setDouble(2, coefficient);
            ps.executeUpdate();
        }
    }

    public void update(int id, String nom, double coefficient) throws SQLException {
        String sql = "UPDATE matiere SET nom=?, coefficient=? WHERE id_matiere=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setDouble(2, coefficient);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM matiere WHERE id_matiere = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
