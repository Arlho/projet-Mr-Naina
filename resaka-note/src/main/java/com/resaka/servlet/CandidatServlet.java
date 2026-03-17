package com.resaka.servlet;

import com.resaka.dao.CandidatDAO;
import com.resaka.model.Candidat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/candidats")
public class CandidatServlet extends HttpServlet {

    private final CandidatDAO candidatDAO = new CandidatDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                candidatDAO.delete(id);
                request.setAttribute("success", "Candidat supprimé avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Candidat c = candidatDAO.findById(id);
                request.setAttribute("editCandidat", c);
            }

            request.setAttribute("candidats", candidatDAO.findAll());
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

        try {
            if (idStr != null && !idStr.isEmpty()) {
                candidatDAO.update(Integer.parseInt(idStr), nom, prenom, matricule);
                request.setAttribute("success", "Candidat modifié avec succès.");
            } else {
                candidatDAO.insert(nom, prenom, matricule);
                request.setAttribute("success", "Candidat ajouté avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
