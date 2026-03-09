package com.resaka.servlet;

import com.resaka.dao.DatabaseConnection;
import com.resaka.model.Resolution;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/resolutions")
public class ResolutionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM resolution WHERE id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Résolution supprimée avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, description, resultat FROM resolution WHERE id = ?")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Resolution r = new Resolution(rs.getInt("id"), rs.getString("description"), rs.getDouble("resultat"));
                            request.setAttribute("editResolution", r);
                        }
                    }
                }
            }

            // Load all
            List<Resolution> list = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, description, resultat FROM resolution ORDER BY id");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Resolution(rs.getInt("id"), rs.getString("description"), rs.getDouble("resultat")));
                }
            }
            request.setAttribute("resolutions", list);
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/resolutions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String description = request.getParameter("description");
        String resStr = request.getParameter("resultat");
        double resultat = (resStr != null && !resStr.isEmpty()) ? Double.parseDouble(resStr) : 0;

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (idStr != null && !idStr.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE resolution SET description=?, resultat=? WHERE id=?")) {
                    ps.setString(1, description);
                    ps.setDouble(2, resultat);
                    ps.setInt(3, Integer.parseInt(idStr));
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Résolution modifiée avec succès.");
            } else {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resolution (description, resultat) VALUES (?, ?)")) {
                    ps.setString(1, description);
                    ps.setDouble(2, resultat);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Résolution ajoutée avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
