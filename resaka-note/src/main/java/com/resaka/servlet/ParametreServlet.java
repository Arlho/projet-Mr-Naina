package com.resaka.servlet;

import com.resaka.dao.DatabaseConnection;
import com.resaka.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/parametres")
public class ParametreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM parametre WHERE id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Paramètre supprimé avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, id_operateur, id_matiere, id_resolution, min, max FROM parametre WHERE id = ?")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Parametre p = new Parametre(rs.getInt("id"), rs.getInt("id_operateur"), rs.getInt("id_matiere"), rs.getInt("id_resolution"), rs.getInt("min"), rs.getInt("max"));
                            request.setAttribute("editParametre", p);
                        }
                    }
                }
            }

            // Load dropdown data
            request.setAttribute("operateurs", loadOperateurs(conn));
            request.setAttribute("matieres", loadMatieres(conn));
            request.setAttribute("resolutions", loadResolutions(conn));

            // Load all with joins
            List<Parametre> list = new ArrayList<>();
            String sql = "SELECT p.id, p.id_operateur, p.id_matiere, p.id_resolution, p.min, p.max, "
                    + "o.symbole as op_symbole, m.nom as mat_nom, r.description as res_desc "
                    + "FROM parametre p "
                    + "JOIN operateur o ON p.id_operateur = o.id "
                    + "JOIN matiere m ON p.id_matiere = m.id_matiere "
                    + "JOIN resolution r ON p.id_resolution = r.id "
                    + "ORDER BY m.nom, p.min";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Parametre p = new Parametre(rs.getInt("id"), rs.getInt("id_operateur"), rs.getInt("id_matiere"), rs.getInt("id_resolution"), rs.getInt("min"), rs.getInt("max"));
                    
                    Operateur op = new Operateur();
                    op.setSymbole(rs.getString("op_symbole"));
                    p.setOperateur(op);

                    request.setAttribute("param_" + p.getId() + "_matiere", rs.getString("mat_nom"));
                    request.setAttribute("param_" + p.getId() + "_resolution", rs.getString("res_desc"));
                    list.add(p);
                }
            }
            request.setAttribute("parametres", list);

        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/parametres.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int idOperateur = Integer.parseInt(request.getParameter("idOperateur"));
        int idMatiere = Integer.parseInt(request.getParameter("idMatiere"));
        int idResolution = Integer.parseInt(request.getParameter("idResolution"));
        int min = Integer.parseInt(request.getParameter("min"));
        int max = Integer.parseInt(request.getParameter("max"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (idStr != null && !idStr.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE parametre SET id_operateur=?, id_matiere=?, id_resolution=?, min=?, max=? WHERE id=?")) {
                    ps.setInt(1, idOperateur);
                    ps.setInt(2, idMatiere);
                    ps.setInt(3, idResolution);
                    ps.setInt(4, min);
                    ps.setInt(5, max);
                    ps.setInt(6, Integer.parseInt(idStr));
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Paramètre modifié avec succès.");
            } else {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO parametre (id_operateur, id_matiere, id_resolution, min, max) VALUES (?, ?, ?, ?, ?)")) {
                    ps.setInt(1, idOperateur);
                    ps.setInt(2, idMatiere);
                    ps.setInt(3, idResolution);
                    ps.setInt(4, min);
                    ps.setInt(5, max);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Paramètre ajouté avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }

    private List<Operateur> loadOperateurs(Connection conn) throws SQLException {
        List<Operateur> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT id, nom, symbole FROM operateur ORDER BY nom");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Operateur(rs.getInt("id"), rs.getString("nom"), rs.getString("symbole")));
            }
        }
        return list;
    }

    private List<Matiere> loadMatieres(Connection conn) throws SQLException {
        List<Matiere> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT id_matiere, nom FROM matiere ORDER BY nom");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Matiere m = new Matiere();
                m.setIdMatiere(rs.getInt("id_matiere"));
                m.setNom(rs.getString("nom"));
                list.add(m);
            }
        }
        return list;
    }

    private List<Resolution> loadResolutions(Connection conn) throws SQLException {
        List<Resolution> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT id, description FROM resolution ORDER BY description");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Resolution(rs.getInt("id"), rs.getString("description"), 0));
            }
        }
        return list;
    }
}
