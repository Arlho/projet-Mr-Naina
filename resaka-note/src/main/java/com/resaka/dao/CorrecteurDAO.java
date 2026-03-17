package com.resaka.dao;

import com.resaka.model.Correcteur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorrecteurDAO {

    public List<Correcteur> findAll() throws SQLException {
        List<Correcteur> list = new ArrayList<>();
        String sql = "SELECT id, nom FROM correcteur ORDER BY nom";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Correcteur(rs.getInt("id"), rs.getString("nom")));
            }
        }
        return list;
    }

    public Correcteur findById(int id) throws SQLException {
        String sql = "SELECT id, nom FROM correcteur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Correcteur(rs.getInt("id"), rs.getString("nom"));
                }
            }
        }
        return null;
    }

    public void insert(String nom) throws SQLException {
        String sql = "INSERT INTO correcteur (nom) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.executeUpdate();
        }
    }

    public void update(int id, String nom) throws SQLException {
        String sql = "UPDATE correcteur SET nom=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM correcteur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
