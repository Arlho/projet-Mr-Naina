package com.resaka.servlet;

import com.resaka.dao.DatabaseConnection;
import com.resaka.model.Matiere;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/matieres")
public class MatiereServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM matiere WHERE id_matiere = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Matière supprimée avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("SELECT id_matiere, nom, coefficient FROM matiere WHERE id_matiere = ?")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Matiere m = new Matiere(rs.getInt("id_matiere"), rs.getString("nom"), rs.getDouble("coefficient"));
                            request.setAttribute("editMatiere", m);
                        }
                    }
                }
            }

            // Load all
            List<Matiere> list = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id_matiere, nom, coefficient FROM matiere ORDER BY nom");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Matiere(rs.getInt("id_matiere"), rs.getString("nom"), rs.getDouble("coefficient")));
                }
            }
            request.setAttribute("matieres", list);
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/matieres.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");
        String coeffStr = request.getParameter("coefficient");
        double coefficient = (coeffStr != null && !coeffStr.isEmpty()) ? Double.parseDouble(coeffStr) : 1;

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (idStr != null && !idStr.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE matiere SET nom=?, coefficient=? WHERE id_matiere=?")) {
                    ps.setString(1, nom);
                    ps.setDouble(2, coefficient);
                    ps.setInt(3, Integer.parseInt(idStr));
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Matière modifiée avec succès.");
            } else {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO matiere (nom, coefficient) VALUES (?, ?)")) {
                    ps.setString(1, nom);
                    ps.setDouble(2, coefficient);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Matière ajoutée avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
