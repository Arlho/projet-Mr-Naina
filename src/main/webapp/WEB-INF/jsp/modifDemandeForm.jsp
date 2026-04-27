<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BackOffice - Créer un Devis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>

<nav class="global-navbar">
    <span class="nav-brand">ETU3558</span>
    <a href="${pageContext.request.contextPath}/">FrontOffice (Dashboard)</a>
    <a href="${pageContext.request.contextPath}/backoffice">BackOffice</a>
    <a href="${pageContext.request.contextPath}/statistique">Statistiques</a>
</nav>

<form action="${pageContext.request.contextPath}/demande/updateState/${demande.id}" method="post">
    <div class="container">

        <!-- HEADER -->
        <header>
            <div>
                <h1>Modifier statut Demande</h1>
                <p>Mettre à jour l’état de la demande client</p>
            </div>
        </header>

        <!-- INFOS DEMANDE + CLIENT -->
        <div class="grid-layout">

            <div class="card">
                <h2>Informations Demande</h2>
                <p><strong>ID :</strong> ${demande.id}</p>
                <p><strong>Date :</strong> ${demande.dateDemande}</p>
                <p><strong>Lieu :</strong> ${demande.lieu}</p>
                <p><strong>District :</strong> ${demande.district}</p>
            </div>

            <div class="card">
                <h2>Informations Client</h2>
                <p><strong>ID :</strong> ${demande.client.id}</p>
                <p><strong>Nom :</strong> ${demande.client.nom}</p>
                <p><strong>Contact :</strong> ${demande.client.contact}</p>
            </div>

        </div>

        <!-- HISTORIQUE -->
        <div class="card">
            <h2>Historique des Statuts</h2>
            <table>
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Statut</th>
                        <th>Observation</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${statuses}" var="s">
                        <tr>
                            <td>${s.dateStatus}</td>
                            <td>
                                <span class="status-badge">${s.status.libelle}</span>
                            </td>
                            <td>${s.observation}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- FORMULAIRE -->
        <div class="card">
            <h2>Modifier le statut</h2>

            <div class="form-group">
                <label>Choisir le nouveau statut</label>
                <select name="nouveauStatusId" required>
                    <option value="" disabled selected>-- Choisir le statut --</option>
                    <c:forEach items="${optionStatus}" var="status">
                        <option value="${status.id}">${status.libelle}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Observation</label>
                <textarea name="observation" rows="3" placeholder="Ajouter un commentaire..."></textarea>
            </div>

            <div class="action-btns">
                <button type="submit" class="btn btn-primary">Modifier</button>
            </div>
        </div>

    </div>
</form>

</body>
</html>
