<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forage Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="global-navbar">
        <span class="nav-brand">ETU3558</span>
        <a href="${pageContext.request.contextPath}/">FrontOffice (Dashboard)</a>
        <a href="${pageContext.request.contextPath}/backoffice">BackOffice</a>
        <a href="${pageContext.request.contextPath}/statistique">Statistiques</a>
    </nav>
    <div class="container">
        <header>
            <div>
                <h1>Système Forage</h1>
                <p>Gestion des projets et suivi des demandes</p>
            </div>
            <div style="display: flex; align-items: center; gap: 12px;">
                <span class="badge">Session : Sprint 3</span>
            </div>
        </header>

        <%-- <div class="grid-layout"> --%>
            <!-- CARTE CLIENTS -->
            <div class="card">
                <h2>
                    <span>Clients</span>
                    <button class="btn btn-primary" onclick="toggleForm('clientForm')">+ Nouveau</button>
                </h2>

                <div id="clientForm" class="form-section" style="display: none;">
                    <form action="${pageContext.request.contextPath}/client/save" method="POST">
                        <div class="form-group">
                            <label>Nom du Client</label>
                            <input type="text" name="nom" placeholder="Ex: Jean Dupont" required>
                        </div>
                        <div class="form-group">
                            <label>Contact (Téléphone)</label>
                            <input type="text" name="contact" placeholder="Ex: 032 12..." required>
                        </div>
                        <button type="submit" class="btn btn-primary" style="width: 100%">Enregistrer le Client</button>
                    </form>
                </div>

                <div style="overflow-x: auto;">
                    <table>
                        <thead>
                            <tr>
                                <th>Nom</th>
                                <th>Contact</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${clients}" var="client">
                                <tr>
                                    <td style="font-weight: 600;">${client.nom}</td>
                                    <td>${client.contact}</td>
                                    <td class="action-btns">
                                        <form action="${pageContext.request.contextPath}/client/delete/${client.id}" method="POST" onsubmit="return confirm('Supprimer ce client ?');">
                                            <button type="submit" class="btn btn-danger">Supprimer</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <tr></tr>

            <!-- CARTE DEMANDES -->
            <div class="card">
                <h2>
                    <span>Demandes</span>
                    <button class="btn btn-primary" onclick="toggleForm('demandeForm')">+ Nouvelle</button>
                </h2>

                <div id="demandeForm" class="form-section" style="display: none;">
                    <form action="${pageContext.request.contextPath}/demande/save" method="POST">
                        <div class="form-group">
                            <label>Client</label>
                            <select name="client.id" required>
                                <option value="" disabled selected>Sélectionner un client</option>
                                <c:forEach items="${clients}" var="c">
                                    <option value="${c.id}">${c.nom}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px;">
                            <div class="form-group">
                                <label>Lieu</label>
                                <input type="text" name="lieu" placeholder="Antananarivo" required>
                            </div>
                            <div class="form-group">
                                <label>District</label>
                                <input type="text" name="district" placeholder="District">
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Date</label>
                            <input type="date" name="dateDemande" id="dateInput" required>
                        </div>
                        <button type="submit" class="btn btn-primary" style="width: 100%">Enregistrer la Demande</button>
                    </form>
                </div>

                <div style="overflow-x: auto;">
                    <table>
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Client</th>
                                <th>Localisation</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${demandes}" var="demande">
                                <tr>
                                    <td>${demande.dateDemande}</td>
                                    <td>
                                        <span class="badge">${demande.client.nom}</span>
                                    </td>
                                    <td>
                                        <span>${demande.lieu}</span><br>
                                        <small style="color: var(--text-muted);">${demande.district}</small>
                                    </td>
                                        <td>
                                            ${lastStatuses[demande.id].status.libelle}
                                        </td>
                                    <td class="action-btns">
                                        <form action="${pageContext.request.contextPath}/demande/details/${demande.id}" method="POST">
                                            <button type="submit" class="btn btn-danger">Details</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/demande/majform/${demande.id}" method="POST">
                                            <button type="submit" class="btn btn-danger">Modifier</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/demande/delete/${demande.id}" method="POST" onsubmit="return confirm('Confirmer la suppression ?');">
                                            <button type="submit" class="btn btn-danger">Supprimer</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    <%-- </div> --%>

    <script>
        function toggleForm(id) {
            const el = document.getElementById(id);
            el.style.display = el.style.display === 'none' ? 'block' : 'none';
        }
        document.getElementById('dateInput').valueAsDate = new Date();
    </script>
</body>
</html>
