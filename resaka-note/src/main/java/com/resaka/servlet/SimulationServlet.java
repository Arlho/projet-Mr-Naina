package com.resaka.servlet;

import com.resaka.dao.CandidatDAO;
import com.resaka.dao.MatiereDAO;
import com.resaka.dao.NoteDAO;
import com.resaka.dao.ParametreDAO;
import com.resaka.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/simulation")
public class SimulationServlet extends HttpServlet {

    private final CandidatDAO candidatDAO = new CandidatDAO();
    private final MatiereDAO matiereDAO = new MatiereDAO();
    private final NoteDAO noteDAO = new NoteDAO();
    private final ParametreDAO parametreDAO = new ParametreDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("candidats", candidatDAO.findAll());
            request.setAttribute("matieres", matiereDAO.findAll());
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

        try {
            // Load dropdown data again for the form
            List<Candidat> candidats = candidatDAO.findAll();
            List<Matiere> matieres = matiereDAO.findAll();
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
            List<Note> notes = noteDAO.findByCandidatAndMatiere(idCandidat, idMatiere);
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
            List<Parametre> parametres = parametreDAO.findByMatiere(idMatiere);
            request.setAttribute("parametres", parametres);

            // 4. Find the matching parametre based on the operator symbol
            Parametre matchedParametre = findMatchingParametre(parametres, totalGap);

            // 5. Fallback: find closest lower parametre if no exact match
            if (matchedParametre == null && parametres != null && !parametres.isEmpty()) {
                matchedParametre = findClosestParametre(parametres, totalGap);
                request.setAttribute("fallbackMessage", "Aucun seuil exact trouvé. Utilisation du paramètre le plus proche inférieur.");
            }

            request.setAttribute("matchedParametre", matchedParametre);

            if (matchedParametre == null) {
                request.setAttribute("error", "Aucun paramètre trouvé pour un écart total de " + totalGap);
                request.getRequestDispatcher("/result.jsp").forward(request, response);
                return;
            }

            // 6. Resolve the final grade based on the resolution ID
            double finalGrade = calculateFinalGrade(notes, matchedParametre);
            String resolutionMethod = getResolutionMethod(matchedParametre);

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

    // --- Fonctions de calcul (logique métier dans le servlet) ---

    private Parametre findMatchingParametre(List<Parametre> parametres, double totalGap) {
        for (Parametre p : parametres) {
            if (p.getOperateur() == null || p.getOperateur().getSymbole() == null) continue;

            String symb = p.getOperateur().getSymbole();
            boolean isMatch = false;

            switch (symb) {
                case "<":  isMatch = (totalGap < p.getMax()); break;
                case ">=": isMatch = (totalGap >= p.getMin()); break;
                case "<=": isMatch = (totalGap <= p.getMax()); break;
                case ">":  isMatch = (totalGap > p.getMin()); break;
                case "between":
                default:   isMatch = (totalGap >= p.getMin() && totalGap <= p.getMax()); break;
            }

            if (isMatch) {
                return p;
            }
        }
        return null;
    }

    private Parametre findClosestParametre(List<Parametre> parametres, double totalGap) {
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
        return closestParam;
    }

    private double calculateFinalGrade(List<Note> notes, Parametre matchedParametre) {
        double finalGrade = 0;
        int resId = matchedParametre.getIdResolution();

        if (resId == 1) {
            finalGrade = notes.stream().mapToDouble(Note::getValeurNote).average().orElse(0);
        } else if (resId == 2) {
            finalGrade = notes.stream().mapToDouble(Note::getValeurNote).max().orElse(0);
        } else if (resId == 3) {
            finalGrade = notes.stream().mapToDouble(Note::getValeurNote).min().orElse(0);
        } else {
            String opSymb = matchedParametre.getOperateur().getSymbole();
            switch (opSymb) {
                case "+":
                    finalGrade = notes.stream().mapToDouble(Note::getValeurNote).max().orElse(0);
                    break;
                case "-":
                    finalGrade = notes.stream().mapToDouble(Note::getValeurNote).min().orElse(0);
                    break;
                default:
                    finalGrade = notes.stream().mapToDouble(Note::getValeurNote).average().orElse(0);
            }
        }
        return finalGrade;
    }

    private String getResolutionMethod(Parametre matchedParametre) {
        int resId = matchedParametre.getIdResolution();

        if (resId == 1) return "Moyenne des notes";
        if (resId == 2) return "Note la plus haute";
        if (resId == 3) return "Note la plus basse";

        String opSymb = matchedParametre.getOperateur().getSymbole();
        switch (opSymb) {
            case "+": return "Note la plus haute";
            case "-": return "Note la plus basse";
            default:  return "Moyenne des notes";
        }
    }
}
