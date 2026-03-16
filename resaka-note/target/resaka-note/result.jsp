<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Résultat Simulation - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>

        <a href="${pageContext.request.contextPath}/simulation" class="back-link">&#8592; Retour à la sélection</a>

        <h1>&#128200; Résultat de la Simulation</h1>

        <div class="simulation-tag">&#9888;&#65039; Simulation uniquement — aucune donnée n'est enregistrée</div>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty fallbackMessage}">
            <div class="error" style="background-color: #e2e3e5; color: #383d41; border-left-color: #6c757d;">
                &#9432;&#65039; ${fallbackMessage}
            </div>
        </c:if>

        <c:if test="${not empty selectedCandidat}">
            <!-- Student & Subject Info -->
            <div class="card">
                <div class="card-title">&#128100; Informations</div>
                <div class="info-grid">
                    <div class="info-item">
                        <div class="info-label">Candidat</div>
                        <div class="info-value">${selectedCandidat.nom} ${selectedCandidat.prenom}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Matricule</div>
                        <div class="info-value">${selectedCandidat.matricule}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Matière</div>
                        <div class="info-value">${selectedMatiere.nom}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Coefficient</div>
                        <div class="info-value">${selectedMatiere.coefficient}</div>
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty notes}">
            <!-- Notes from Correcteurs -->
            <div class="card">
                <div class="card-title">&#128221; Notes attribuées par les correcteurs</div>
                <table>
                    <thead>
                        <tr>
                            <th>Correcteur</th>
                            <th>Note</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="note" items="${notes}">
                            <tr>
                                <td>${note.correcteurNom}</td>
                                <td><strong>${note.valeurNote}</strong> / 20</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Gap Calculation -->
            <div class="card">
                <div class="card-title">&#128270; Calcul des écarts</div>
                <ul class="gap-list">
                    <c:forEach var="gap" items="${gapDetails}">
                        <li class="gap-item">${gap}</li>
                    </c:forEach>
                </ul>
                <div class="total-gap">
                    Total des écarts = <strong>${totalGap}</strong>
                </div>
            </div>

            <!-- Parameters -->
            <div class="card">
                <div class="card-title">&#9881;&#65039; Paramètres configurés pour ${selectedMatiere.nom}</div>
                <table>
                    <thead>
                        <tr>
                            <th>Min</th>
                            <th>Max</th>
                            <th>Opérateur</th>
                            <th>Résolution</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${parametres}">
                            <tr class="${matchedParametre != null && matchedParametre.id == p.id ? 'highlight-row' : ''}">
                                <td>${p.min}</td>
                                <td>${p.max}</td>
                                <td>
                                    <span class="badge ${p.operateur.symbole == '+' ? 'badge-plus' : p.operateur.symbole == '-' ? 'badge-minus' : p.operateur.symbole == '*' ? 'badge-mult' : 'badge-default'}">
                                        ${p.operateur.symbole} ${p.operateur.nom}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.idResolution == 1}">Moyenne</c:when>
                                        <c:when test="${p.idResolution == 2}">Note Max</c:when>
                                        <c:when test="${p.idResolution == 3}">Note Min</c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${p.operateur.symbole == '+'}">Note Max</c:when>
                                                <c:when test="${p.operateur.symbole == '-'}">Note Min</c:when>
                                                <c:otherwise>Moyenne</c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <c:if test="${not empty finalGrade}">
            <!-- Final Grade -->
            <div class="card final-grade-card">
                <div class="final-grade-label">Note finale simulée</div>
                <div class="final-grade-value">${finalGrade}</div>
                <div class="final-grade-label">sur 20</div>
                <div class="resolution-method">${resolutionMethod}</div>
            </div>
        </c:if>
    </div>
</body>
</html>
