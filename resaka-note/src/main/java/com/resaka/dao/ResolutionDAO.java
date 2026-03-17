package com.resaka.dao;

import com.resaka.model.Resolution;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResolutionDAO {

    public List<Resolution> findAll() throws SQLException {
        List<Resolution> list = new ArrayList<>();
        String sql = "SELECT id, description, resultat FROM resolution ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Resolution(rs.getInt("id"), rs.getString("description"), rs.getDouble("resultat")));
            }
        }
        return list;
    }

    public List<Resolution> findAllByDescription() throws SQLException {
        List<Resolution> list = new ArrayList<>();
        String sql = "SELECT id, description FROM resolution ORDER BY description";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Resolution(rs.getInt("id"), rs.getString("description"), 0));
            }
        }
        return list;
    }

    public Resolution findById(int id) throws SQLException {
        String sql = "SELECT id, description, resultat FROM resolution WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Resolution(rs.getInt("id"), rs.getString("description"), rs.getDouble("resultat"));
                }
            }
        }
        return null;
    }

    public void insert(String description, double resultat) throws SQLException {
        String sql = "INSERT INTO resolution (description, resultat) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, description);
            ps.setDouble(2, resultat);
            ps.executeUpdate();
        }
    }

    public void update(int id, String description, double resultat) throws SQLException {
        String sql = "UPDATE resolution SET description=?, resultat=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, description);
            ps.setDouble(2, resultat);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM resolution WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
