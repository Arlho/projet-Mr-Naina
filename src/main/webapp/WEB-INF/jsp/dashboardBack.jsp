<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BackOffice - Liste des Devis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="nav.jsp" />
    <div class="container">
        <header>
            <div>
                <h1>BackOffice - Devis</h1>
                <p style="color: #666">Liste de tous vos Devis</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/devis/create" class="btn btn-primary">+ Nouveau Devis</a>
                <a href="${pageContext.request.contextPath}/" class="btn">Retour Frontend</a>
            </div>
        </header>

        <div class="card" style="margin-top: 20px;">
            <h2>
                <span>Liste des Devis enregistrés</span>
            </h2>

            <div style="overflow-x: auto; margin-top: 15px;">
                <table>
                    <thead>
                        <tr>
                            <th>N° Devis</th>
                            <th>Date</th>
                            <th>Demande Associée</th>
                            <th>Montant Total</th>
                            <th>Type</th>
                            <th>Statut</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${devisList}" var="devis">
                            <tr>
                                <td>${devis.id}</td>
                                <td>${devis.dateDevis}</td>
                                <td>N° ${devis.demande.id}</td>
                                <td style="font-weight: bold;">${devis.montantTotal}</td>
                                <td>${devis.typeDevis != null ? devis.typeDevis.libelle : '-'}</td>
                                <td>
                                    <span class="badge" style="background-color: #f39c12;">
                                        ${devis.statusDevis != null ? devis.statusDevis.libelle : 'En attente'}
                                    </span>
                                </td>
                                <td class="action-btns">
                                    <a href="${pageContext.request.contextPath}/devis/details/${devis.id}" class="btn" style="background:#17a2b8; color:white;">Voir</a>
                                    <button type="button" class="btn" style="background:#f39c12; color:white;" onclick="openStatusModal(${devis.id})">Modifier Statut</button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty devisList}">
                            <tr>
                                <td colspan="7" style="text-align: center; color: #999;">Aucun devis trouvé.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Modal Modifier Statut -->
    <div id="statusModal" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index: 1000;">
        <div style="background:white; margin:10% auto; padding:20px; width:400px; border-radius:8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
            <h3 style="margin-top:0;">Modifier le Statut</h3>
            <form action="${pageContext.request.contextPath}/devis/update-status" method="post">
                <input type="hidden" name="devisId" id="modalDevisId">
                <div style="margin-bottom:15px;">
                    <label style="display:block; margin-bottom:5px;">Nouveau Statut</label>
                    <select name="statusId" style="width:100%; padding:8px; border: 1px solid #ddd; border-radius: 4px;" required>
                        <c:forEach items="${allStatus}" var="status">
                            <option value="${status.id}">${status.libelle}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="text-align:right; margin-top:20px;">
                    <button type="button" class="btn" style="background:#f44336; color:white;" onclick="document.getElementById('statusModal').style.display='none'">Annuler</button>
                    <button type="submit" class="btn btn-primary" style="margin-left:10px;">Enregistrer</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openStatusModal(devisId) {
            document.getElementById('modalDevisId').value = devisId;
            document.getElementById('statusModal').style.display = 'block';
        }
    </script>
</body>
</html>
