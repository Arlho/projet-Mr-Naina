package com.resaka.servlet;

import com.resaka.dao.DatabaseConnection;
import com.resaka.model.Candidat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/candidats")
public class CandidatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM candidat WHERE id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Candidat supprimé avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, nom, prenom, matricule FROM candidat WHERE id = ?")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Candidat c = new Candidat(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("matricule"));
                            request.setAttribute("editCandidat", c);
                        }
                    }
                }
            }

            // Load all candidats
            List<Candidat> list = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, nom, prenom, matricule FROM candidat ORDER BY nom, prenom");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Candidat(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("matricule")));
                }
            }
            request.setAttribute("candidats", list);
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/candidats.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String matricule = request.getParameter("matricule");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (idStr != null && !idStr.isEmpty()) {
                // UPDATE
                try (PreparedStatement ps = conn.prepareStatement("UPDATE candidat SET nom=?, prenom=?, matricule=? WHERE id=?")) {
                    ps.setString(1, nom);
                    ps.setString(2, prenom);
                    ps.setString(3, matricule);
                    ps.setInt(4, Integer.parseInt(idStr));
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Candidat modifié avec succès.");
            } else {
                // INSERT
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO candidat (nom, prenom, matricule) VALUES (?, ?, ?)")) {
                    ps.setString(1, nom);
                    ps.setString(2, prenom);
                    ps.setString(3, matricule);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Candidat ajouté avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
