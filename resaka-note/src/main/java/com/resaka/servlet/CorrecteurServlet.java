package com.resaka.servlet;

import com.resaka.dao.DatabaseConnection;
import com.resaka.model.Correcteur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/correcteurs")
public class CorrecteurServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM correcteur WHERE id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Correcteur supprimé avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, nom FROM correcteur WHERE id = ?")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Correcteur c = new Correcteur(rs.getInt("id"), rs.getString("nom"));
                            request.setAttribute("editCorrecteur", c);
                        }
                    }
                }
            }

            // Load all
            List<Correcteur> list = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, nom FROM correcteur ORDER BY nom");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Correcteur(rs.getInt("id"), rs.getString("nom")));
                }
            }
            request.setAttribute("correcteurs", list);
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/correcteurs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (idStr != null && !idStr.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE correcteur SET nom=? WHERE id=?")) {
                    ps.setString(1, nom);
                    ps.setInt(2, Integer.parseInt(idStr));
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Correcteur modifié avec succès.");
            } else {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO correcteur (nom) VALUES (?)")) {
                    ps.setString(1, nom);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Correcteur ajouté avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
