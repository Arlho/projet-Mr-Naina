package com.resaka.servlet;

import com.resaka.dao.CandidatDAO;
import com.resaka.dao.MatiereDAO;
import com.resaka.dao.NoteDAO;
import com.resaka.dao.ParametreDAO;
import com.resaka.model.*;
import com.resaka.service.ResultatService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/resultats")
public class ResultatsServlet extends HttpServlet {

    private final CandidatDAO candidatDAO = new CandidatDAO();
    private final MatiereDAO matiereDAO = new MatiereDAO();
    private final NoteDAO noteDAO = new NoteDAO();
    private final ParametreDAO parametreDAO = new ParametreDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Candidat> candidats = candidatDAO.findAll();
            List<Matiere> matieres = matiereDAO.findAll();

            // Map: Candidat ID -> (Matiere ID -> Final Grade)
            Map<Integer, Map<Integer, Double>> finalGrades = new HashMap<>();

            // Pre-load all parametres grouped by Matiere
            Map<Integer, List<Parametre>> parametresByMatiere = new HashMap<>();
            for (Matiere m : matieres) {
                parametresByMatiere.put(m.getIdMatiere(), parametreDAO.findByMatiere(m.getIdMatiere()));
            }

            for (Candidat c : candidats) {
                Map<Integer, Double> gradesForCandidat = new HashMap<>();
                for (Matiere m : matieres) {
                    List<Note> notes = noteDAO.findByCandidatAndMatiere(c.getId(), m.getIdMatiere());
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
}
