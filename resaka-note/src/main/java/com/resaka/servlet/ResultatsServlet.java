package com.resaka.servlet;

import com.resaka.dao.DatabaseConnection;
import com.resaka.model.*;
import com.resaka.service.ResultatService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/resultats")
public class ResultatsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<Candidat> candidats = loadCandidats(conn);
            List<Matiere> matieres = loadMatieres(conn);
            
            // Map: Candidat ID -> (Matiere ID -> Final Grade)
            Map<Integer, Map<Integer, Double>> finalGrades = new HashMap<>();
            
            // Pre-load all parametres grouped by Matiere
            Map<Integer, List<Parametre>> parametresByMatiere = new HashMap<>();
            for (Matiere m : matieres) {
                parametresByMatiere.put(m.getIdMatiere(), loadParametres(conn, m.getIdMatiere()));
            }

            for (Candidat c : candidats) {
                Map<Integer, Double> gradesForCandidat = new HashMap<>();
                for (Matiere m : matieres) {
                    List<Note> notes = loadNotes(conn, c.getId(), m.getIdMatiere());
                    List<Parametre> rules = parametresByMatiere.get(m.getIdMatiere());
                    
                    double finalGrade = ResultatService.calculateFinalGrade(notes, rules);
                    gradesForCandidat.put(m.getIdMatiere(), finalGrade);
                }
                finalGrades.put(c.getId(), gradesForCandidat);
            }

            request.setAttribute("candidats", candidats);
            request.setAttribute("matieres", matieres);
            request.setAttribute("finalGrades", finalGrades);

        } catch (SQLException e) {
            request.setAttribute("error", "Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
        }

        request.getRequestDispatcher("/resultats.jsp").forward(request, response);
    }
    
    // Extracted from SimulationServlet logic
    private List<Candidat> loadCandidats(Connection conn) throws SQLException {
        List<Candidat> list = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, matricule FROM candidat ORDER BY nom, prenom";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Candidat c = new Candidat();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setMatricule(rs.getString("matricule"));
                list.add(c);
            }
        }
        return list;
    }

    private List<Matiere> loadMatieres(Connection conn) throws SQLException {
        List<Matiere> list = new ArrayList<>();
        String sql = "SELECT id_matiere, nom, coefficient FROM matiere ORDER BY nom";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Matiere m = new Matiere();
                m.setIdMatiere(rs.getInt("id_matiere"));
                m.setNom(rs.getString("nom"));
                m.setCoefficient(rs.getDouble("coefficient"));
                list.add(m);
            }
        }
        return list;
    }

    private List<Note> loadNotes(Connection conn, int idCandidat, int idMatiere) throws SQLException {
        List<Note> list = new ArrayList<>();
        String sql = "SELECT n.id, n.id_candidat, n.id_matiere, n.id_correcteur, n.valeur_note, c.nom as correcteur_nom "
                + "FROM note n JOIN correcteur c ON n.id_correcteur = c.id "
                + "WHERE n.id_candidat = ? AND n.id_matiere = ? ORDER BY c.nom";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCandidat);
            ps.setInt(2, idMatiere);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Note n = new Note();
                    n.setId(rs.getInt("id"));
                    n.setIdCandidat(rs.getInt("id_candidat"));
                    n.setIdMatiere(rs.getInt("id_matiere"));
                    n.setIdCorrecteur(rs.getInt("id_correcteur"));
                    n.setValeurNote(rs.getDouble("valeur_note"));
                    n.setCorrecteurNom(rs.getString("correcteur_nom"));
                    list.add(n);
                }
            }
        }
        return list;
    }

    private List<Parametre> loadParametres(Connection conn, int idMatiere) throws SQLException {
        List<Parametre> list = new ArrayList<>();
        String sql = "SELECT p.id, p.id_operateur, p.id_matiere, p.id_resolution, p.min, p.max, "
                + "o.id as op_id, o.nom as op_nom, o.symbole as op_symbole "
                + "FROM parametre p JOIN operateur o ON p.id_operateur = o.id "
                + "WHERE p.id_matiere = ? ORDER BY p.min";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
}
