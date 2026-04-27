<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BackOffice - Liste des Devis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .status-badge {
            padding: 4px 14px;
            border-radius: 20px;
            font-size: 0.75rem;
            font-weight: 600;
            letter-spacing: 0.3px;
            background: rgba(245, 158, 11, 0.15);
            color: var(--accent-orange);
            border: 1px solid rgba(245, 158, 11, 0.3);
        }
        .modal-overlay {
            display: none;
            position: fixed;
            top: 0; left: 0;
            width: 100%; height: 100%;
            background: rgba(0, 0, 0, 0.7);
            backdrop-filter: blur(4px);
            z-index: 1000;
        }
        .modal-content {
            background: var(--bg-card);
            margin: 12% auto;
            padding: 28px;
            width: 420px;
            max-width: 90%;
            border-radius: var(--radius-lg);
            border: 1px solid var(--border-color);
            box-shadow: var(--shadow-lg);
        }
        .modal-content h3 {
            margin-top: 0;
            margin-bottom: 20px;
            color: var(--text-primary);
        }
    </style>
</head>
<body>
    <nav class="global-navbar">
        <span class="nav-brand">ETU3558</span>
        <a href="${pageContext.request.contextPath}/">FrontOffice (Dashboard)</a>
        <a href="${pageContext.request.contextPath}/backoffice">BackOffice</a>
        <a href="${pageContext.request.contextPath}/statistique">Statistiques</a>
    </nav>
    <div class="container">
        <header>
            <div>
                <h1>BackOffice - Devis       -      ${chiffreAffaire}</h1>
                <p>Liste de tous vos Devis</p>
            </div>
            <div style="display: flex; gap: 10px;">
                <a href="${pageContext.request.contextPath}/devis/create" class="btn btn-primary">+ Nouveau Devis</a>
            </div>
        </header>

        <div class="card">
            <h2>Devis enregistrés</h2>

            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>N° Devis</th>
                            <th>Date</th>
                            <th>Demande</th>
                            <th>Montant Total</th>
                            <th>Type</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${devisList}" var="devis">
                            <tr>
                                <td><span class="badge">#${devis.id}</span></td>
                                <td>${devis.dateDevis}</td>
                                <td>N° ${devis.demande.id}</td>
                                <td>
                                    <fmt:formatNumber value="${montantTotalMap[devis.id]}" type="number" pattern="#,##0.00"/>
                                </td>
                                <td>${devis.typeDevis != null ? devis.typeDevis.libelle : '-'}</td>
                                <td class="action-btns">
                                    <a href="${pageContext.request.contextPath}/devis/details/${devis.id}" class="btn" style="background: var(--bg-input); color: var(--text-primary); border-color: var(--border-light);">Voir</a>
                                    <button type="button" class="btn" style="background: var(--bg-input); color: var(--text-primary); border-color: var(--border-light);" onclick="openStatusModal(${devis.id})">Modifier Statut</button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty devisList}">
                            <tr>
                                <td colspan="7" style="text-align: center; color: var(--text-muted); padding: 32px;">Aucun devis trouvé.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>

            </div>
        </div>
    </div>

    <!-- Modal Modifier Statut -->
    <div id="statusModal" class="modal-overlay">
        <div class="modal-content">
            <h3>Modifier le Statut</h3>
            <form action="${pageContext.request.contextPath}/devis/update-status" method="post">
                <input type="hidden" name="devisId" id="modalDevisId">
                <div class="form-group">
                    <label>Nouveau Statut</label>
                    <select name="statusId" required>
                        <c:forEach items="${allStatus}" var="status">
                            <option value="${status.id}">${status.libelle}</option>
                        </c:forEach>
                    </select>
                </div>
                <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 24px;">
                    <button type="button" class="btn btn-danger" onclick="document.getElementById('statusModal').style.display='none'">Annuler</button>
                    <button type="submit" class="btn btn-primary">Enregistrer</button>
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
q