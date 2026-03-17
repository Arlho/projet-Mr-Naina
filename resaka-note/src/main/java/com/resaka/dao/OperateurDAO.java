package com.resaka.dao;

import com.resaka.model.Operateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperateurDAO {

    public List<Operateur> findAll() throws SQLException {
        List<Operateur> list = new ArrayList<>();
        String sql = "SELECT id, nom, symbole FROM operateur ORDER BY nom";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Operateur(rs.getInt("id"), rs.getString("nom"), rs.getString("symbole")));
            }
        }
        return list;
    }

    public Operateur findById(int id) throws SQLException {
        String sql = "SELECT id, nom, symbole FROM operateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Operateur(rs.getInt("id"), rs.getString("nom"), rs.getString("symbole"));
                }
            }
        }
        return null;
    }

    public void insert(String nom, String symbole) throws SQLException {
        String sql = "INSERT INTO operateur (nom, symbole) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, symbole);
            ps.executeUpdate();
        }
    }

    public void update(int id, String nom, String symbole) throws SQLException {
        String sql = "UPDATE operateur SET nom=?, symbole=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, symbole);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM operateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
