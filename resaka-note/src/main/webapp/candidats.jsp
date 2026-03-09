<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Candidats - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>

        <h1>&#128100; Gestion des Candidats</h1>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <!-- Form -->
        <div class="card">
            <div class="card-title">
                ${not empty editCandidat ? 'Modifier le candidat' : 'Ajouter un candidat'}
            </div>
            <form action="${pageContext.request.contextPath}/candidats" method="post">
                <c:if test="${not empty editCandidat}">
                    <input type="hidden" name="id" value="${editCandidat.id}">
                </c:if>
                <div class="form-row">
                    <div class="form-group">
                        <label for="nom">Nom</label>
                        <input type="text" id="nom" name="nom" value="${editCandidat.nom}" required placeholder="Ex: Rakoto">
                    </div>
                    <div class="form-group">
                        <label for="prenom">Prénom</label>
                        <input type="text" id="prenom" name="prenom" value="${editCandidat.prenom}" required placeholder="Ex: Jean">
                    </div>
                    <div class="form-group">
                        <label for="matricule">Matricule</label>
                        <input type="text" id="matricule" name="matricule" value="${editCandidat.matricule}" required placeholder="Ex: MAT004">
                    </div>
                </div>
                <div class="form-row">
                    <button type="submit" class="btn btn-primary">
                        ${not empty editCandidat ? '&#10004; Modifier' : '&#10010; Ajouter'}
                    </button>
                    <c:if test="${not empty editCandidat}">
                        <a href="${pageContext.request.contextPath}/candidats" class="btn btn-edit">Annuler</a>
                    </c:if>
                </div>
            </form>
        </div>

        <!-- List -->
        <div class="card">
            <div class="card-title">Liste des candidats (${candidats.size()})</div>
            <c:choose>
                <c:when test="${empty candidats}">
                    <p style="color:rgba(255,255,255,0.5); text-align:center; padding:1rem;">Aucun candidat enregistré.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nom</th>
                                <th>Prénom</th>
                                <th>Matricule</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${candidats}">
                                <tr>
                                    <td>${c.id}</td>
                                    <td>${c.nom}</td>
                                    <td>${c.prenom}</td>
                                    <td>${c.matricule}</td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/candidats?action=edit&id=${c.id}" class="btn btn-edit btn-sm">&#9998;</a>
                                        <a href="${pageContext.request.contextPath}/candidats?action=delete&id=${c.id}" class="btn btn-danger btn-sm" onclick="return confirm('Supprimer ce candidat ?')">&#128465;</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
