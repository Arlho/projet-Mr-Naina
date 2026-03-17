<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notes - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>

        <h1>📝 Gestion des Notes</h1>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="card">
            <div class="card-title">
                ${not empty editNote ? 'Modifier la note' : 'Attribuer une note'}
            </div>
            <form action="${pageContext.request.contextPath}/notes" method="post">
                <c:if test="${not empty editNote}">
                    <input type="hidden" name="id" value="${editNote.id}">
                </c:if>
                <div class="form-row">
                    <div class="form-group">
                        <label for="idCandidat">Candidat</label>
                        <select name="idCandidat" id="idCandidat" required>
                            <option value="">-- Choisir --</option>
                            <c:forEach var="c" items="${candidats}">
                                <option value="${c.id}" ${editNote != null && editNote.idCandidat == c.id ? 'selected' : ''}>
                                    ${c.nom} ${c.prenom} (${c.matricule})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="idMatiere">Matière</label>
                        <select name="idMatiere" id="idMatiere" required>
                            <option value="">-- Choisir --</option>
                            <c:forEach var="m" items="${matieres}">
                                <option value="${m.idMatiere}" ${editNote != null && editNote.idMatiere == m.idMatiere ? 'selected' : ''}>
                                    ${m.nom}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="idCorrecteur">Correcteur</label>
                        <select name="idCorrecteur" id="idCorrecteur" required>
                            <option value="">-- Choisir --</option>
                            <c:forEach var="co" items="${correcteurs}">
                                <option value="${co.id}" ${editNote != null && editNote.idCorrecteur == co.id ? 'selected' : ''}>
                                    ${co.nom}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="valeurNote">Note / 20</label>
                        <input type="number" id="valeurNote" name="valeurNote" value="${editNote.valeurNote}" step="0.25" min="0" max="20" required>
                    </div>
                </div>
                <div class="form-row">
                    <button type="submit" class="btn btn-primary">
                        ${not empty editNote ? '✔ Modifier' : '➕ Ajouter'}
                    </button>
                    <c:if test="${not empty editNote}">
                        <a href="${pageContext.request.contextPath}/notes" class="btn btn-edit">Annuler</a>
                    </c:if>
                </div>
            </form>
        </div>

        <div class="card">
            <div class="card-title">Liste des notes (${notes.size()})</div>
            <c:choose>
                <c:when test="${empty notes}">
                    <p style="color:rgba(255,255,255,0.5); text-align:center; padding:1rem;">Aucune note enregistrée.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Candidat</th>
                                <th>Matière</th>
                                <th>Correcteur</th>
                                <th>Note</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="n" items="${notes}">
                                <tr>
                                    <td>${n.id}</td>
                                    <td>${n.candidatNom}</td>
                                    <td>${n.matiereNom}</td>
                                    <td>${n.correcteurNom}</td>
                                    <td><strong>${n.valeurNote}</strong> / 20</td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/notes?action=edit&id=${n.id}" class="btn btn-edit btn-sm">✎</a>
                                        <a href="${pageContext.request.contextPath}/notes?action=delete&id=${n.id}" class="btn btn-danger btn-sm" onclick="return confirm('Supprimer cette note ?')">🗑</a>
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
