<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Opérateurs - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>

        <h1>⚙️ Gestion des Opérateurs</h1>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="card">
            <div class="card-title">
                ${not empty editOperateur ? 'Modifier l\'opérateur' : 'Ajouter un opérateur'}
            </div>
            <form action="${pageContext.request.contextPath}/operateurs" method="post">
                <c:if test="${not empty editOperateur}">
                    <input type="hidden" name="id" value="${editOperateur.id}">
                </c:if>
                <div class="form-row">
                    <div class="form-group">
                        <label for="nom">Nom de l'opérateur</label>
                        <input type="text" id="nom" name="nom" value="${editOperateur.nom}" required placeholder="Ex: Supérieur">
                    </div>
                    <div class="form-group">
                        <label for="symbole">Symbole</label>
                        <input type="text" id="symbole" name="symbole" value="${editOperateur.symbole}" required placeholder="Ex: >">
                    </div>
                </div>
                <div class="form-row">
                    <button type="submit" class="btn btn-primary">
                        ${not empty editOperateur ? '✔ Modifier' : '➕ Ajouter'}
                    </button>
                    <c:if test="${not empty editOperateur}">
                        <a href="${pageContext.request.contextPath}/operateurs" class="btn btn-edit">Annuler</a>
                    </c:if>
                </div>
            </form>
        </div>

        <div class="card">
            <div class="card-title">Liste des opérateurs (${operateurs.size()})</div>
            <c:choose>
                <c:when test="${empty operateurs}">
                    <p style="color:rgba(255,255,255,0.5); text-align:center; padding:1rem;">Aucun opérateur enregistré.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nom</th>
                                <th>Symbole</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${operateurs}">
                                <tr>
                                    <td>${o.id}</td>
                                    <td>${o.nom}</td>
                                    <td><span class="badge badge-mult">${o.symbole}</span></td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/operateurs?action=edit&id=${o.id}" class="btn btn-edit btn-sm">✎</a>
                                        <a href="${pageContext.request.contextPath}/operateurs?action=delete&id=${o.id}" class="btn btn-danger btn-sm" onclick="return confirm('Supprimer cet opérateur ?')">🗑</a>
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
