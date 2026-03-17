package com.resaka.servlet;

import com.resaka.dao.OperateurDAO;
import com.resaka.model.Operateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/operateurs")
public class OperateurServlet extends HttpServlet {

    private final OperateurDAO operateurDAO = new OperateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                operateurDAO.delete(id);
                request.setAttribute("success", "Opérateur supprimé avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Operateur o = operateurDAO.findById(id);
                request.setAttribute("editOperateur", o);
            }

            request.setAttribute("operateurs", operateurDAO.findAll());
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/operateurs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");
        String symbole = request.getParameter("symbole");

        try {
            if (idStr != null && !idStr.isEmpty()) {
                operateurDAO.update(Integer.parseInt(idStr), nom, symbole);
                request.setAttribute("success", "Opérateur modifié avec succès.");
            } else {
                operateurDAO.insert(nom, symbole);
                request.setAttribute("success", "Opérateur ajouté avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
