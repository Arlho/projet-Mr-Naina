package com.resaka.servlet;

import com.resaka.dao.CandidatDAO;
import com.resaka.dao.CorrecteurDAO;
import com.resaka.dao.MatiereDAO;
import com.resaka.dao.NoteDAO;
import com.resaka.model.Note;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/notes")
public class NoteServlet extends HttpServlet {

    private final NoteDAO noteDAO = new NoteDAO();
    private final CandidatDAO candidatDAO = new CandidatDAO();
    private final MatiereDAO matiereDAO = new MatiereDAO();
    private final CorrecteurDAO correcteurDAO = new CorrecteurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                noteDAO.delete(id);
                request.setAttribute("success", "Note supprimée avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Note n = noteDAO.findById(id);
                request.setAttribute("editNote", n);
            }

            // Load dropdown data
            request.setAttribute("candidats", candidatDAO.findAll());
            request.setAttribute("matieres", matiereDAO.findAll());
            request.setAttribute("correcteurs", correcteurDAO.findAll());

            // Load all notes with joined info
            List<Note> notes = noteDAO.findAll();
            request.setAttribute("notes", notes);
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

        try {
            if (idStr != null && !idStr.isEmpty()) {
                noteDAO.update(Integer.parseInt(idStr), idCandidat, idMatiere, idCorrecteur, valeurNote);
                request.setAttribute("success", "Note modifiée avec succès.");
            } else {
                noteDAO.insert(idCandidat, idMatiere, idCorrecteur, valeurNote);
                request.setAttribute("success", "Note ajoutée avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
