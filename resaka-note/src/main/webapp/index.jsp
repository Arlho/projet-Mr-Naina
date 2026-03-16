<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Simulation de Notes - Resaka Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <div class="page-wrapper">
        <%@ include file="nav.jsp" %>

        <div class="center-wrapper">
            <div class="card narrow-card">
        <h1>&#128218; Simulation de Notes</h1>
        <p class="subtitle">Sélectionnez un candidat et une matière pour simuler le calcul</p>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/simulation" method="post">
            <div class="form-group">
                <label for="idCandidat"><span class="icon">&#128100;</span> Candidat</label>
                <select name="idCandidat" id="idCandidat" required>
                    <option value="">-- Choisir un candidat --</option>
                    <c:forEach var="c" items="${candidats}">
                        <option value="${c.id}" ${param.idCandidat == c.id ? 'selected' : ''}>
                            ${c.matricule} - ${c.nom} ${c.prenom}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="idMatiere"><span class="icon">&#128214;</span> Matière / Examen</label>
                <select name="idMatiere" id="idMatiere" required>
                    <option value="">-- Choisir une matière --</option>
                    <c:forEach var="m" items="${matieres}">
                        <option value="${m.idMatiere}" ${param.idMatiere == m.idMatiere ? 'selected' : ''}>
                            ${m.nom} (coeff. ${m.coefficient})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Simuler le calcul</button>
        </form>
            </div>
        </div>
    </div>
</body>
</html>
