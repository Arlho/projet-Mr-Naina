package com.resaka.servlet;

import com.resaka.dao.MatiereDAO;
import com.resaka.model.Matiere;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/matieres")
public class MatiereServlet extends HttpServlet {

    private final MatiereDAO matiereDAO = new MatiereDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                matiereDAO.delete(id);
                request.setAttribute("success", "Matière supprimée avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Matiere m = matiereDAO.findById(id);
                request.setAttribute("editMatiere", m);
            }

            request.setAttribute("matieres", matiereDAO.findAll());
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/matieres.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");
        String coeffStr = request.getParameter("coefficient");
        double coefficient = (coeffStr != null && !coeffStr.isEmpty()) ? Double.parseDouble(coeffStr) : 1;

        try {
            if (idStr != null && !idStr.isEmpty()) {
                matiereDAO.update(Integer.parseInt(idStr), nom, coefficient);
                request.setAttribute("success", "Matière modifiée avec succès.");
            } else {
                matiereDAO.insert(nom, coefficient);
                request.setAttribute("success", "Matière ajoutée avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
