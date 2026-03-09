package com.resaka.servlet;

import com.resaka.dao.DatabaseConnection;
import com.resaka.model.Operateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/operateurs")
public class OperateurServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM operateur WHERE id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Opérateur supprimé avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, nom, symbole FROM operateur WHERE id = ?")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Operateur o = new Operateur(rs.getInt("id"), rs.getString("nom"), rs.getString("symbole"));
                            request.setAttribute("editOperateur", o);
                        }
                    }
                }
            }

            // Load all
            List<Operateur> list = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, nom, symbole FROM operateur ORDER BY nom");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Operateur(rs.getInt("id"), rs.getString("nom"), rs.getString("symbole")));
                }
            }
            request.setAttribute("operateurs", list);
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/operateurs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");
        String symbole = request.getParameter("symbole");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (idStr != null && !idStr.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE operateur SET nom=?, symbole=? WHERE id=?")) {
                    ps.setString(1, nom);
                    ps.setString(2, symbole);
                    ps.setInt(3, Integer.parseInt(idStr));
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Opérateur modifié avec succès.");
            } else {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO operateur (nom, symbole) VALUES (?, ?)")) {
                    ps.setString(1, nom);
                    ps.setString(2, symbole);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Opérateur ajouté avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
