<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Détails de la Demande</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="global-navbar">
    <span class="nav-brand">ETU3558</span>
    <a href="${pageContext.request.contextPath}/">FrontOffice (Dashboard)</a>
    <a href="${pageContext.request.contextPath}/backoffice">BackOffice</a>
    <a href="${pageContext.request.contextPath}/statistique">Statistiques</a>
</nav>
<div class="container">
    <h1>Détails de la Demande</h1>
    
    <div class="card">
        <h2>Informations sur la demande</h2>
        <p><strong>Date : </strong>${demande.dateDemande}</p>
        <p><strong>Client : </strong>${client.nom}</p>
        <p><strong>Lieu : </strong>${demande.lieu}, <small>${demande.district}</small></p>
    </div>

    <div class="card">
        <h2>Historique des Status</h2>
        <table>
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Observation</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${statuses}" var="s">
                    <tr>
                        <td>${s.dateStatus}</td>
                        <td><span class="status-badge">${s.status.libelle}</span></td>
                        <td>${s.observation}</td>
                        <td>
                            <button type="button" class="btn"
                                onclick="openModal('${s.id}', '${s.dateStatus}', '${s.observation}','${s.status.libelle}','${demande.id}')">
                                Modifier
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="card">
        <h2>Devis associés</h2>
        <c:if test="${empty devises}">
            <p>Aucun devis pour cette demande.</p>
        </c:if>
        <c:if test="${not empty devises}">
            <table>
                <thead>
                    <tr>
                        <th>ID Devis</th>
                        <th>Client</th>
                        <th>Montant</th>
                        <th>Lieu</th>
                        <th>type</th>
                        <th>Date</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${devises}" var="d">
                        <tr>
                            <td>${d.id}</td>
                            <td>${d.demande.client.nom}</td>
                            <td>${montantTotalMap[d.id]}</td>
                            <td>${d.typeDevis.libelle}</td>
                            <td>${d.demande.lieu}</td>
                            <td>${d.dateDevis}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/devis/delete/${d.id}" method="POST" onsubmit="return confirm('Supprimer ce devis ?');">
                                    <button type="submit" class="btn btn-danger">Supprimer</button>
                                </form>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/devis/details/${d.id}" method="GET">
                                    <button type="submit" class="btn">Voir</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</div>



<div id="modal" class="modal-overlay">
    <div class="modal-content">
        <h3>Modifier l'historique</h3>

        <form method="POST" action="${pageContext.request.contextPath}/historique/update">
            
            <input type="hidden" name="id" id="statusId">
            <input type="hidden" name="idDemande" id="idDemande">

            <div class="form-group">
                <label>Status Actuel</label>
                <input type="text" name="statusLibelle" id="statusLibelle" disabled>
            </div>

            <div class="form-group">
                <label>Date</label>
                <input type="datetime-local" name="dateStatus" id="dateStatus">
            </div>

            <div class="form-group">
                <label>Observation</label>
                <textarea name="observation" id="observation"></textarea>
            </div>

            <div class="action-btns">
                <button type="submit" class="btn btn-primary">Enregistrer</button>
                <button type="button" class="btn btn-danger" onclick="closeModal()">Annuler</button>
            </div>

        </form>
    </div>
</div>

<script>
    function openModal(id, date, observation, status,idDm) {
        document.getElementById("modal").style.display = "block";

        document.getElementById("statusId").value = id;

        // ⚠️ format datetime-local
        if (date) {
            let d = new Date(date);
            let formatted = d.toISOString().slice(0,16);
            document.getElementById("dateStatus").value = formatted;
        }
        document.getElementById("statusLibelle").value = status;
        document.getElementById("idDemande").value = idDm;
        document.getElementById("observation").value = observation || "";
    }

    function closeModal() {
        document.getElementById("modal").style.display = "none";
    }

</script>



</body>
</html>