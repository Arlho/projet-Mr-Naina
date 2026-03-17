package com.resaka.servlet;

import com.resaka.dao.CorrecteurDAO;
import com.resaka.model.Correcteur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/correcteurs")
public class CorrecteurServlet extends HttpServlet {

    private final CorrecteurDAO correcteurDAO = new CorrecteurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                correcteurDAO.delete(id);
                request.setAttribute("success", "Correcteur supprimé avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Correcteur c = correcteurDAO.findById(id);
                request.setAttribute("editCorrecteur", c);
            }

            request.setAttribute("correcteurs", correcteurDAO.findAll());
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/correcteurs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");

        try {
            if (idStr != null && !idStr.isEmpty()) {
                correcteurDAO.update(Integer.parseInt(idStr), nom);
                request.setAttribute("success", "Correcteur modifié avec succès.");
            } else {
                correcteurDAO.insert(nom);
                request.setAttribute("success", "Correcteur ajouté avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
