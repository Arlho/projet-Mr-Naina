package com.resaka.dao;

import com.resaka.model.Operateur;
import com.resaka.model.Parametre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParametreDAO {

    public List<Parametre> findAll() throws SQLException {
        List<Parametre> list = new ArrayList<>();
        String sql = "SELECT p.id, p.id_operateur, p.id_matiere, p.id_resolution, p.min, p.max, "
                + "o.symbole as op_symbole, m.nom as mat_nom, r.description as res_desc "
                + "FROM parametre p "
                + "JOIN operateur o ON p.id_operateur = o.id "
                + "JOIN matiere m ON p.id_matiere = m.id_matiere "
                + "JOIN resolution r ON p.id_resolution = r.id "
                + "ORDER BY m.nom, p.min";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Parametre p = new Parametre(rs.getInt("id"), rs.getInt("id_operateur"),
                        rs.getInt("id_matiere"), rs.getInt("id_resolution"), rs.getInt("min"), rs.getInt("max"));
                Operateur op = new Operateur();
                op.setSymbole(rs.getString("op_symbole"));
                p.setOperateur(op);
                p.setMatiereNom(rs.getString("mat_nom"));
                p.setResolutionDesc(rs.getString("res_desc"));
                list.add(p);
            }
        }
        return list;
    }

    public Parametre findById(int id) throws SQLException {
        String sql = "SELECT id, id_operateur, id_matiere, id_resolution, min, max FROM parametre WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Parametre(rs.getInt("id"), rs.getInt("id_operateur"),
                            rs.getInt("id_matiere"), rs.getInt("id_resolution"), rs.getInt("min"), rs.getInt("max"));
                }
            }
        }
        return null;
    }

    public List<Parametre> findByMatiere(int idMatiere) throws SQLException {
        List<Parametre> list = new ArrayList<>();
        String sql = "SELECT p.id, p.id_operateur, p.id_matiere, p.id_resolution, p.min, p.max, "
                + "o.id as op_id, o.nom as op_nom, o.symbole as op_symbole "
                + "FROM parametre p JOIN operateur o ON p.id_operateur = o.id "
                + "WHERE p.id_matiere = ? ORDER BY p.min";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMatiere);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Parametre p = new Parametre();
                    p.setId(rs.getInt("id"));
                    p.setIdOperateur(rs.getInt("id_operateur"));
                    p.setIdMatiere(rs.getInt("id_matiere"));
                    try {
                        p.setIdResolution(rs.getInt("id_resolution"));
                    } catch (SQLException e) {
                        p.setIdResolution(0);
                    }
                    p.setMin(rs.getInt("min"));
                    p.setMax(rs.getInt("max"));

                    Operateur op = new Operateur();
                    op.setId(rs.getInt("op_id"));
                    op.setNom(rs.getString("op_nom"));
                    op.setSymbole(rs.getString("op_symbole"));
                    p.setOperateur(op);

                    list.add(p);
                }
            }
        }
        return list;
    }

    public void insert(int idOperateur, int idMatiere, int idResolution, int min, int max) throws SQLException {
        String sql = "INSERT INTO parametre (id_operateur, id_matiere, id_resolution, min, max) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOperateur);
            ps.setInt(2, idMatiere);
            ps.setInt(3, idResolution);
            ps.setInt(4, min);
            ps.setInt(5, max);
            ps.executeUpdate();
        }
    }

    public void update(int id, int idOperateur, int idMatiere, int idResolution, int min, int max) throws SQLException {
        String sql = "UPDATE parametre SET id_operateur=?, id_matiere=?, id_resolution=?, min=?, max=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOperateur);
            ps.setInt(2, idMatiere);
            ps.setInt(3, idResolution);
            ps.setInt(4, min);
            ps.setInt(5, max);
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM parametre WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
