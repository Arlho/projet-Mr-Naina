<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Matières - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>

        <h1>&#128214; Gestion des Matières</h1>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <!-- Form -->
        <div class="card">
            <div class="card-title">
                ${not empty editMatiere ? 'Modifier la matière' : 'Ajouter une matière'}
            </div>
            <form action="${pageContext.request.contextPath}/matieres" method="post">
                <c:if test="${not empty editMatiere}">
                    <input type="hidden" name="id" value="${editMatiere.idMatiere}">
                </c:if>
                <div class="form-row">
                    <div class="form-group">
                        <label for="nom">Nom de la matière</label>
                        <input type="text" id="nom" name="nom" value="${editMatiere.nom}" required placeholder="Ex: Algèbre">
                    </div>
                    <div class="form-group">
                        <label for="coefficient">Coefficient</label>
                        <input type="number" id="coefficient" name="coefficient" value="${editMatiere != null ? editMatiere.coefficient : '1'}" step="0.5" min="0.5" required>
                    </div>
                </div>
                <div class="form-row">
                    <button type="submit" class="btn btn-primary">
                        ${not empty editMatiere ? '&#10004; Modifier' : '&#10010; Ajouter'}
                    </button>
                    <c:if test="${not empty editMatiere}">
                        <a href="${pageContext.request.contextPath}/matieres" class="btn btn-edit">Annuler</a>
                    </c:if>
                </div>
            </form>
        </div>

        <!-- List -->
        <div class="card">
            <div class="card-title">Liste des matières (${matieres.size()})</div>
            <c:choose>
                <c:when test="${empty matieres}">
                    <p style="color:rgba(255,255,255,0.5); text-align:center; padding:1rem;">Aucune matière enregistrée.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nom</th>
                                <th>Coefficient</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="m" items="${matieres}">
                                <tr>
                                    <td>${m.idMatiere}</td>
                                    <td>${m.nom}</td>
                                    <td>${m.coefficient}</td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/matieres?action=edit&id=${m.idMatiere}" class="btn btn-edit btn-sm">&#9998;</a>
                                        <a href="${pageContext.request.contextPath}/matieres?action=delete&id=${m.idMatiere}" class="btn btn-danger btn-sm" onclick="return confirm('Supprimer cette matière ?')">&#128465;</a>
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
