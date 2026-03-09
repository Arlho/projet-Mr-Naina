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

@WebServlet("/notes")
public class NoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM note WHERE id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Note supprimée avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, id_candidat, id_matiere, id_correcteur, valeur_note FROM note WHERE id = ?")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Note n = new Note(rs.getInt("id"), rs.getInt("id_candidat"), rs.getInt("id_matiere"), rs.getInt("id_correcteur"), rs.getDouble("valeur_note"));
                            request.setAttribute("editNote", n);
                        }
                    }
                }
            }

            // Load dropdown data
            request.setAttribute("candidats", loadCandidats(conn));
            request.setAttribute("matieres", loadMatieres(conn));
            request.setAttribute("correcteurs", loadCorrecteurs(conn));

            // Load all notes with joined info
            List<Note> list = new ArrayList<>();
            String sql = "SELECT n.id, n.id_candidat, n.id_matiere, n.id_correcteur, n.valeur_note, "
                    + "c.nom as candidat_nom, c.prenom as candidat_prenom, "
                    + "m.nom as matiere_nom, cor.nom as correcteur_nom "
                    + "FROM note n "
                    + "JOIN candidat c ON n.id_candidat = c.id "
                    + "JOIN matiere m ON n.id_matiere = m.id_matiere "
                    + "JOIN correcteur cor ON n.id_correcteur = cor.id "
                    + "ORDER BY n.id DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Note n = new Note(rs.getInt("id"), rs.getInt("id_candidat"), rs.getInt("id_matiere"), rs.getInt("id_correcteur"), rs.getDouble("valeur_note"));
                    n.setCorrecteurNom(rs.getString("correcteur_nom")); // Reusing this field for display
                    // We can add more temporary fields to Note if needed, but for now we'll use attributes or custom display in JSP
                    request.setAttribute("note_" + n.getId() + "_candidat", rs.getString("candidat_nom") + " " + rs.getString("candidat_prenom"));
                    request.setAttribute("note_" + n.getId() + "_matiere", rs.getString("matiere_nom"));
                    list.add(n);
                }
            }
            request.setAttribute("notes", list);
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/notes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        int idCandidat = Integer.parseInt(request.getParameter("idCandidat"));
        int idMatiere = Integer.parseInt(request.getParameter("idMatiere"));
        int idCorrecteur = Integer.parseInt(request.getParameter("idCorrecteur"));
        double valeurNote = Double.parseDouble(request.getParameter("valeurNote"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (idStr != null && !idStr.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE note SET id_candidat=?, id_matiere=?, id_correcteur=?, valeur_note=? WHERE id=?")) {
                    ps.setInt(1, idCandidat);
                    ps.setInt(2, idMatiere);
                    ps.setInt(3, idCorrecteur);
                    ps.setDouble(4, valeurNote);
                    ps.setInt(5, Integer.parseInt(idStr));
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Note modifiée avec succès.");
            } else {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO note (id_candidat, id_matiere, id_correcteur, valeur_note) VALUES (?, ?, ?, ?)")) {
                    ps.setInt(1, idCandidat);
                    ps.setInt(2, idMatiere);
                    ps.setInt(3, idCorrecteur);
                    ps.setDouble(4, valeurNote);
                    ps.executeUpdate();
                }
                request.setAttribute("success", "Note ajoutée avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }

    private List<Candidat> loadCandidats(Connection conn) throws SQLException {
        List<Candidat> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT id, nom, prenom, matricule FROM candidat ORDER BY nom");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Candidat(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("matricule")));
            }
        }
        return list;
    }

    private List<Matiere> loadMatieres(Connection conn) throws SQLException {
        List<Matiere> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT id_matiere, nom, coefficient FROM matiere ORDER BY nom");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Matiere(rs.getInt("id_matiere"), rs.getString("nom"), rs.getDouble("coefficient")));
            }
        }
        return list;
    }

    private List<Correcteur> loadCorrecteurs(Connection conn) throws SQLException {
        List<Correcteur> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT id, nom FROM correcteur ORDER BY nom");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Correcteur(rs.getInt("id"), rs.getString("nom")));
            }
        }
        return list;
    }
}
