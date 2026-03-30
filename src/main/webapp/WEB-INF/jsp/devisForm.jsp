<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BackOffice - Créer un Devis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .form-section { background: #fdfdfd; padding: 25px; border: 1px solid #ddd; border-radius: 8px; }
        .detail-row { display: flex; gap: 10px; margin-bottom: 10px; align-items: center; }
        .detail-row input { flex: 1; margin: 0; }
        .info-box { margin-top: 10px; padding: 15px; background: #eef; border-radius: 4px; display: none; line-height: 1.5; border-left: 4px solid #007bff; }
    </style>
</head>
<body>
    <jsp:include page="nav.jsp" />
    <div class="container">
        <header>
            <div>
                <h1>Création d'un Nouveau Devis</h1>
                <p style="color: #666">Renseignez les détails du devis</p>
            </div>
            <a href="${pageContext.request.contextPath}/devis/liste" class="btn">Retour à la liste</a>
        </header>

        <div class="form-section" style="margin-top: 20px;">
            <form action="${pageContext.request.contextPath}/devis/save" method="POST">
                
                <div class="form-group" style="display: flex; gap: 20px;">
                    <div style="flex: 1;">
                        <label style="font-weight:bold;">1. Sélectionner une Demande</label>
                        <select name="demande.id" id="demandeSelect" required onchange="fetchDemandeInfo()" style="padding: 10px; font-size: 16px; width: 100%;">
                            <option value="" disabled selected>-- Choisir la Demande concernée --</option>
                            <c:forEach items="${demandes}" var="d">
                                <option value="${d.id}">Demande N° ${d.id}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div style="flex: 1;">
                        <label style="font-weight:bold;">Type de Devis</label>
                        <select name="typeDevis.id" required style="padding: 10px; font-size: 16px; width: 100%;">
                            <option value="" disabled selected>-- Choisir le Type de Devis --</option>
                            <c:forEach items="${typesDevis}" var="type">
                                <option value="${type.id}">${type.libelle}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div id="demandeInfoBox" class="info-box">
                    <!-- Informations chargées via AJAX -->
                </div>

                <hr style="margin: 30px 0; border: 1px solid #eee;">
                
                <h3 style="margin-bottom: 15px;">2. Ajouter les Détails du Devis</h3>
                <div id="detailsContainer">
                    <!-- Les lignes dynamiques apparaîtront ici -->
                </div>
                
                <button type="button" class="btn" style="background:#5bc0de; color:white; margin-bottom: 25px;" onclick="addDetailRow()">
                    + Ajouter une ligne (Libellé / Montant)
                </button>

                <hr style="margin: 30px 0; border: 1px solid #eee;">

                <button type="submit" class="btn btn-primary" style="width: 100%; font-size: 18px; padding: 15px;">
                    Enregistrer le Devis complet
                </button>
            </form>
        </div>
    </div>

    <script>
        function fetchDemandeInfo() {
            const demandeId = document.getElementById('demandeSelect').value;
            if (!demandeId) return;

            fetch('${pageContext.request.contextPath}/api/demande/' + demandeId)
                .then(response => {
                    if (!response.ok) throw new Error("Erreur réseau");
                    return response.json();
                })
                .then(data => {
                    const infoBox = document.getElementById('demandeInfoBox');
                    infoBox.style.display = 'block';
                    infoBox.innerHTML = `
                        <strong>Client :</strong> \${data.clientNom} (Contact: \${data.clientContact}) <br>
                        <strong>Localisation :</strong> \${data.lieu} - \${data.district} <br>
                        <strong>Date d'émission :</strong> \${data.dateDemande}
                    `;
                })
                .catch(err => {
                    console.error("Erreur AJAX:", err);
                    const infoBox = document.getElementById('demandeInfoBox');
                    infoBox.style.display = 'block';
                    infoBox.innerHTML = `<span style="color:red;">Erreur lors du chargement des informations de la demande.</span>`;
                });
        }

        let detailIndex = 0;
        function addDetailRow() {
            const container = document.getElementById('detailsContainer');
            const row = document.createElement('div');
            row.className = 'detail-row';
            
            row.innerHTML = `
                <input type="text" name="detailsLibelles" placeholder="Ex: Forage 100m" required style="flex: 2; padding: 10px;">
                <input type="number" step="0.01" name="detailsMontants" placeholder="Montant (ex: 500000)" required style="flex: 1; padding: 10px;">
                <button type="button" class="btn btn-danger" onclick="this.parentElement.remove()" style="margin:0; padding: 10px 15px;">X</button>
            `;
            
            container.appendChild(row);
            detailIndex++;
        }
        
        // Ajouter une première ligne par défaut
        window.onload = function() {
            addDetailRow();
        };
    </script>
</body>
</html>
