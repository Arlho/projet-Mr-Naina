package com.resaka.servlet;

import com.resaka.dao.MatiereDAO;
import com.resaka.dao.OperateurDAO;
import com.resaka.dao.ParametreDAO;
import com.resaka.dao.ResolutionDAO;
import com.resaka.model.Parametre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/parametres")
public class ParametreServlet extends HttpServlet {

    private final ParametreDAO parametreDAO = new ParametreDAO();
    private final OperateurDAO operateurDAO = new OperateurDAO();
    private final MatiereDAO matiereDAO = new MatiereDAO();
    private final ResolutionDAO resolutionDAO = new ResolutionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                parametreDAO.delete(id);
                request.setAttribute("success", "Paramètre supprimé avec succès.");
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Parametre p = parametreDAO.findById(id);
                request.setAttribute("editParametre", p);
            }

            // Load dropdown data
            request.setAttribute("operateurs", operateurDAO.findAll());
            request.setAttribute("matieres", matiereDAO.findAll());
            request.setAttribute("resolutions", resolutionDAO.findAllByDescription());

            // Load all parametres with joined info
            List<Parametre> parametres = parametreDAO.findAll();
            request.setAttribute("parametres", parametres);
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        request.getRequestDispatcher("/parametres.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int idOperateur = Integer.parseInt(request.getParameter("idOperateur"));
        int idMatiere = Integer.parseInt(request.getParameter("idMatiere"));
        int idResolution = Integer.parseInt(request.getParameter("idResolution"));
        int min = Integer.parseInt(request.getParameter("min"));
        int max = Integer.parseInt(request.getParameter("max"));

        try {
            if (idStr != null && !idStr.isEmpty()) {
                parametreDAO.update(Integer.parseInt(idStr), idOperateur, idMatiere, idResolution, min, max);
                request.setAttribute("success", "Paramètre modifié avec succès.");
            } else {
                parametreDAO.insert(idOperateur, idMatiere, idResolution, min, max);
                request.setAttribute("success", "Paramètre ajouté avec succès.");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
        }

        doGet(request, response);
    }
}
