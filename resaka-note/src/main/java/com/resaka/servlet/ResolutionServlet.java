package com.resaka.servlet;

import com.resaka.dao.ResolutionDAO;
import com.resaka.model.Resolution;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/resolutions")
public class ResolutionServlet extends HttpServlet {

    private final ResolutionDAO resolutionDAO = new ResolutionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                resolutionDAO.delete(id);
                request.setAttribute("success", "Résolution supprimée avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Resolution r = resolutionDAO.findById(id);
                request.setAttribute("editResolution", r);
            }

            request.setAttribute("resolutions", resolutionDAO.findAll());
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/resolutions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String description = request.getParameter("description");
        String resStr = request.getParameter("resultat");
        double resultat = (resStr != null && !resStr.isEmpty()) ? Double.parseDouble(resStr) : 0;

        try {
            if (idStr != null && !idStr.isEmpty()) {
                resolutionDAO.update(Integer.parseInt(idStr), description, resultat);
                request.setAttribute("success", "Résolution modifiée avec succès.");
            } else {
                resolutionDAO.insert(description, resultat);
                request.setAttribute("success", "Résolution ajoutée avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
