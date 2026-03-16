<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Résultats Finaux - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>
        <div class="main-content">
            <div class="header">
                <h1>🏆 Résultats Finaux des Candidats</h1>
            </div>

            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>

            <div class="card">
                <p>Tableau récapitulatif des notes finales obtenues par chaque candidat pour toutes les matières.</p>
                
                <div class="table-container">
                    <table class="results-table">
                        <thead>
                            <tr>
                                <th class="text-left">Matricule</th>
                                <th class="text-left">Candidat</th>
                                <c:forEach var="matiere" items="${matieres}">
                                    <th>${matiere.nom} <br><small>(Coeff. ${matiere.coefficient})</small></th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="candidat" items="${candidats}">
                                <tr>
                                    <td class="text-left"><strong>${candidat.matricule}</strong></td>
                                    <td class="text-left">${candidat.nom} ${candidat.prenom}</td>
                                    
                                    <c:forEach var="matiere" items="${matieres}">
                                        <td>
                                            <c:set var="grade" value="${finalGrades[candidat.id][matiere.idMatiere]}" />
                                            <c:choose>
                                                <c:when test="${grade > 0}">
                                                    <span class="grade-badge">
                                                        <fmt:formatNumber value="${grade}" pattern="#,##0.00" />
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="grade-badge grade-missing">N/A</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty candidats}">
                                <tr>
                                    <td colspan="${matieres.size() + 2}">Aucun candidat trouvé.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
