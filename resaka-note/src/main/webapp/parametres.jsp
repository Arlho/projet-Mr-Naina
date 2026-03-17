<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Paramètres - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>

        <h1>🛠️ Gestion des Paramètres</h1>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="card">
            <div class="card-title">
                ${not empty editParametre ? 'Modifier le paramètre' : 'Ajouter un paramètre'}
            </div>
            <form action="${pageContext.request.contextPath}/parametres" method="post">
                <c:if test="${not empty editParametre}">
                    <input type="hidden" name="id" value="${editParametre.id}">
                </c:if>
                <div class="form-row">
                    <div class="form-group">
                        <label for="idMatiere">Matière</label>
                        <select name="idMatiere" id="idMatiere" required>
                            <option value="">-- Choisir --</option>
                            <c:forEach var="m" items="${matieres}">
                                <option value="${m.idMatiere}" ${editParametre != null && editParametre.idMatiere == m.idMatiere ? 'selected' : ''}>
                                    ${m.nom}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="idOperateur">Opérateur de comparaison</label>
                        <select name="idOperateur" id="idOperateur" required>
                            <option value="">-- Choisir --</option>
                            <c:forEach var="o" items="${operateurs}">
                                <option value="${o.id}" ${editParametre != null && editParametre.idOperateur == o.id ? 'selected' : ''}>
                                    écart ${o.symbole} (${o.nom})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="min">Valeur Min</label>
                        <input type="number" id="min" name="min" value="${editParametre != null ? editParametre.min : '0'}" required>
                    </div>
                    <div class="form-group">
                        <label for="max">Valeur Max</label>
                        <input type="number" id="max" name="max" value="${editParametre != null ? editParametre.max : '100'}" required>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="idResolution">Méthode de Résolution</label>
                        <select name="idResolution" id="idResolution" required>
                            <option value="">-- Choisir --</option>
                            <c:forEach var="r" items="${resolutions}">
                                <option value="${r.id}" ${editParametre != null && editParametre.idResolution == r.id ? 'selected' : ''}>
                                    ${r.description}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <button type="submit" class="btn btn-primary">
                        ${not empty editParametre ? '✔ Modifier' : '➕ Ajouter'}
                    </button>
                    <c:if test="${not empty editParametre}">
                        <a href="${pageContext.request.contextPath}/parametres" class="btn btn-edit">Annuler</a>
                    </c:if>
                </div>
            </form>
        </div>

        <div class="card">
            <div class="card-title">Liste des paramètres (${parametres.size()})</div>
            <c:choose>
                <c:when test="${empty parametres}">
                    <p style="color:rgba(255,255,255,0.5); text-align:center; padding:1rem;">Aucun paramètre enregistré.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>Matière</th>
                                <th>Condition</th>
                                <th>Min - Max</th>
                                <th>Résolution</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${parametres}">
                                <tr>
                                    <td>${p.matiereNom}</td>
                                    <td><span class="badge badge-mult">${p.operateur.symbole}</span></td>
                                    <td>${p.min} - ${p.max}</td>
                                    <td><strong>${p.resolutionDesc}</strong></td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/parametres?action=edit&id=${p.id}" class="btn btn-edit btn-sm">✎</a>
                                        <a href="${pageContext.request.contextPath}/parametres?action=delete&id=${p.id}" class="btn btn-danger btn-sm" onclick="return confirm('Supprimer ce paramètre ?')">🗑</a>
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
