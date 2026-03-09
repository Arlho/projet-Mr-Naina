<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Correcteurs - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>

        <h1>&#128221; Gestion des Correcteurs</h1>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <!-- Form -->
        <div class="card">
            <div class="card-title">
                ${not empty editCorrecteur ? 'Modifier le correcteur' : 'Ajouter un correcteur'}
            </div>
            <form action="${pageContext.request.contextPath}/correcteurs" method="post">
                <c:if test="${not empty editCorrecteur}">
                    <input type="hidden" name="id" value="${editCorrecteur.id}">
                </c:if>
                <div class="form-row">
                    <div class="form-group">
                        <label for="nom">Nom</label>
                        <input type="text" id="nom" name="nom" value="${editCorrecteur.nom}" required placeholder="Ex: Dupont">
                    </div>
                </div>
                <div class="form-row">
                    <button type="submit" class="btn btn-primary">
                        ${not empty editCorrecteur ? '&#10004; Modifier' : '&#10010; Ajouter'}
                    </button>
                    <c:if test="${not empty editCorrecteur}">
                        <a href="${pageContext.request.contextPath}/correcteurs" class="btn btn-edit">Annuler</a>
                    </c:if>
                </div>
            </form>
        </div>

        <!-- List -->
        <div class="card">
            <div class="card-title">Liste des correcteurs (${correcteurs.size()})</div>
            <c:choose>
                <c:when test="${empty correcteurs}">
                    <p style="color:rgba(255,255,255,0.5); text-align:center; padding:1rem;">Aucun correcteur enregistré.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nom</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${correcteurs}">
                                <tr>
                                    <td>${c.id}</td>
                                    <td>${c.nom}</td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/correcteurs?action=edit&id=${c.id}" class="btn btn-edit btn-sm">&#9998;</a>
                                        <a href="${pageContext.request.contextPath}/correcteurs?action=delete&id=${c.id}" class="btn btn-danger btn-sm" onclick="return confirm('Supprimer ce correcteur ?')">&#128465;</a>
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
