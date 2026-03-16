package com.resaka.servlet;

import com.resaka.dao.DatabaseConnection;
import com.resaka.model.*;

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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/simulation")
public class SimulationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<Candidat> candidats = loadCandidats(conn);
            List<Matiere> matieres = loadMatieres(conn);
            request.setAttribute("candidats", candidats);
            request.setAttribute("matieres", matieres);
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur de connexion à la base de données: " + e.getMessage());
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int idCandidat = Integer.parseInt(request.getParameter("idCandidat"));
        int idMatiere = Integer.parseInt(request.getParameter("idMatiere"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Load dropdown data again for the form
            List<Candidat> candidats = loadCandidats(conn);
            List<Matiere> matieres = loadMatieres(conn);
            request.setAttribute("candidats", candidats);
            request.setAttribute("matieres", matieres);

            // Find selected candidat and matiere objects
            Candidat selectedCandidat = candidats.stream()
                    .filter(c -> c.getId() == idCandidat).findFirst().orElse(null);
            Matiere selectedMatiere = matieres.stream()
                    .filter(m -> m.getIdMatiere() == idMatiere).findFirst().orElse(null);
            request.setAttribute("selectedCandidat", selectedCandidat);
            request.setAttribute("selectedMatiere", selectedMatiere);

            // 1. Fetch all notes for this (candidat, matiere)
            List<Note> notes = loadNotes(conn, idCandidat, idMatiere);
            request.setAttribute("notes", notes);

            if (notes.isEmpty()) {
                request.setAttribute("error", "Aucune note trouvée pour ce candidat dans cette matière.");
                request.getRequestDispatcher("/result.jsp").forward(request, response);
                return;
            }

            // 2. Calculate all pairwise gaps and their total
            List<String> gapDetails = new ArrayList<>();
            double totalGap = 0;
            for (int i = 0; i < notes.size(); i++) {
                for (int j = i + 1; j < notes.size(); j++) {
                    double gap = Math.abs(notes.get(i).getValeurNote() - notes.get(j).getValeurNote());
                    gapDetails.add("|" + notes.get(i).getValeurNote() + " - " + notes.get(j).getValeurNote() + "| = " + gap);
                    totalGap += gap;
                }
            }
            request.setAttribute("gapDetails", gapDetails);
            request.setAttribute("totalGap", totalGap);

            // 3. Load all parametres for this matiere
            List<Parametre> parametres = loadParametres(conn, idMatiere);
            request.setAttribute("parametres", parametres);

            // 4. Find the matching parametre based on the operator symbol
            Parametre matchedParametre = null;
            for (Parametre p : parametres) {
                String symb = p.getOperateur().getSymbole();
                boolean isMatch = false;
                
                switch (symb) {
                    case "<":
                        isMatch = (totalGap < p.getMax());
                        break;
                    case ">=":
                        isMatch = (totalGap >= p.getMin());
                        break;
                    case "<=":
                        isMatch = (totalGap <= p.getMax());
                        break;
                    case ">":
                        isMatch = (totalGap > p.getMin());
                        break;
                    case "between": // Fallback for original logic if needed
                    default:
                        isMatch = (totalGap >= p.getMin() && totalGap <= p.getMax());
                        break;
                }
                
                if (isMatch) {
                    matchedParametre = p;
                    break;
                }
            }

            if (matchedParametre == null && parametres != null && !parametres.isEmpty()) {
                Parametre closestParam = null;
                double minDifference = Double.MAX_VALUE;

                for (Parametre p : parametres) {
                    double maxVal = p.getMax();
                    double minVal = p.getMin();
                    
                    if (maxVal <= totalGap) {
                        double diff = totalGap - maxVal;
                        if (diff < minDifference) {
                            minDifference = diff;
                            closestParam = p;
                        }
                    }
                    
                    if (minVal <= totalGap) {
                        double diff = totalGap - minVal;
                        if (diff < minDifference) {
                            minDifference = diff;
                            closestParam = p;
                        }
                    }
                }

                if (closestParam == null) {
                    double bestMinVal = Double.MAX_VALUE;
                    for (Parametre p : parametres) {
                        double currentMinParamVal = Math.min(p.getMin(), p.getMax());
                        if (closestParam == null || currentMinParamVal < bestMinVal) {
                            closestParam = p;
                            bestMinVal = currentMinParamVal;
                        }
                    }
                }
                matchedParametre = closestParam;
                request.setAttribute("fallbackMessage", "Aucun seuil exact trouvé. Utilisation du paramètre le plus proche inférieur.");
            }

            request.setAttribute("matchedParametre", matchedParametre);

            if (matchedParametre == null) {
                request.setAttribute("error", "Aucun paramètre trouvé pour un écart total de " + totalGap);
                request.getRequestDispatcher("/result.jsp").forward(request, response);
                return;
            }

            // 5. Resolve the final grade based on the resolution ID or method
            // Mapping: 1 -> Petit (Average), 2 -> Moyenne (Max), 3 -> Grand (Min)
            // Note: Since I don't have the resolution table data in the model yet, 
            // I'll use a mapping based on the ID or the operator name if it changed.
            
            double finalGrade = 0;
            String resolutionMethod = "";
            int resId = matchedParametre.getIdResolution();

            if (resId == 1) {
                finalGrade = notes.stream().mapToDouble(Note::getValeurNote).average().orElse(0);
                resolutionMethod = "Moyenne des notes";
            } else if (resId == 2) {
                finalGrade = notes.stream().mapToDouble(Note::getValeurNote).max().orElse(0);
                resolutionMethod = "Note la plus haute";
            } else if (resId == 3) {
                finalGrade = notes.stream().mapToDouble(Note::getValeurNote).min().orElse(0);
                resolutionMethod = "Note la plus basse";
            } else {
                // Fallback to legacy operator-based resolution if no resolution ID matches
                String opSymb = matchedParametre.getOperateur().getSymbole();
                switch (opSymb) {
                    case "+":
                        finalGrade = notes.stream().mapToDouble(Note::getValeurNote).max().orElse(0);
                        resolutionMethod = "Note la plus haute";
                        break;
                    case "-":
                        finalGrade = notes.stream().mapToDouble(Note::getValeurNote).min().orElse(0);
                        resolutionMethod = "Note la plus basse";
                        break;
                    default:
                        finalGrade = notes.stream().mapToDouble(Note::getValeurNote).average().orElse(0);
                        resolutionMethod = "Moyenne des notes";
                }
            }

            // Round to 2 decimal places
            finalGrade = Math.round(finalGrade * 100.0) / 100.0;

            request.setAttribute("finalGrade", finalGrade);
            request.setAttribute("resolutionMethod", resolutionMethod);
            request.setAttribute("operateur", matchedParametre.getOperateur());

        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }

    private List<Candidat> loadCandidats(Connection conn) throws SQLException {
        List<Candidat> list = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, matricule FROM candidat ORDER BY nom, prenom";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
                    // Use a safe check for id_resolution if it was just added manually
                    try {
                        p.setIdResolution(rs.getInt("id_resolution"));
                    } catch (SQLException e) {
                        // Fallback if column still hasn't been added to the DB but is in SQL
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
