<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Résolutions - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>

        <h1>✅ Gestion des Résolutions</h1>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="card">
            <div class="card-title">
                ${not empty editResolution ? 'Modifier la résolution' : 'Ajouter une résolution'}
            </div>
            <form action="${pageContext.request.contextPath}/resolutions" method="post">
                <c:if test="${not empty editResolution}">
                    <input type="hidden" name="id" value="${editResolution.id}">
                </c:if>
                <div class="form-row">
                    <div class="form-group">
                        <label for="description">Description (Nom)</label>
                        <input type="text" id="description" name="description" value="${editResolution.description}" required placeholder="Ex: Petit">
                    </div>
                </div>
                <div class="form-row">
                    <button type="submit" class="btn btn-primary">
                        ${not empty editResolution ? '✔ Modifier' : '➕ Ajouter'}
                    </button>
                    <c:if test="${not empty editResolution}">
                        <a href="${pageContext.request.contextPath}/resolutions" class="btn btn-edit">Annuler</a>
                    </c:if>
                </div>
            </form>
        </div>

        <div class="card">
            <div class="card-title">Liste des résolutions (${resolutions.size()})</div>
            <c:choose>
                <c:when test="${empty resolutions}">
                    <p style="color:rgba(255,255,255,0.5); text-align:center; padding:1rem;">Aucune résolution enregistrée.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Description</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${resolutions}">
                                <tr>
                                    <td>${r.id}</td>
                                    <td>${r.description}</td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/resolutions?action=edit&id=${r.id}" class="btn btn-edit btn-sm">✎</a>
                                        <a href="${pageContext.request.contextPath}/resolutions?action=delete&id=${r.id}" class="btn btn-danger btn-sm" onclick="return confirm('Supprimer cette résolution ?')">🗑</a>
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
