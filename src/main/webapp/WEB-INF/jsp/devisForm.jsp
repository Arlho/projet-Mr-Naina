<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BackOffice - Créer un Devis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .step-number {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 28px;
            height: 28px;
            background: var(--accent-grey);
            color: #fff;
            border-radius: 50%;
            font-size: 0.8rem;
            font-weight: 700;
            margin-right: 10px;
        }
        .step-title {
            display: flex;
            align-items: center;
            font-weight: 600;
            font-size: 1rem;
            color: var(--text-primary);
            margin-bottom: 16px;
        }
        .divider {
            border: none;
            border-top: 1px solid var(--border-color);
            margin: 28px 0;
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
                <h1>Création d'un Nouveau Devis</h1>
                <p>Renseignez les détails du devis</p>
            </div>
        </header>

        <div class="card">
            <form action="${pageContext.request.contextPath}/devis/save" method="POST">

                <div class="step-title">
                    <span class="step-number">1</span>
                    Sélectionner une Demande et un Type
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                    <div class="form-group">
                        <label>Demande concernée</label>
                        <select name="demande.id" id="demandeSelect" required onchange="fetchDemandeInfo()">
                            <option value="" disabled selected>-- Choisir la Demande --</option>
                            <c:forEach items="${demandes}" var="d">
                                <option value="${d.id}">Demande N° ${d.id}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Type de Devis</label>
                        <select name="typeDevis.id" required>
                            <option value="" disabled selected>-- Choisir le Type --</option>
                            <c:forEach items="${typesDevis}" var="type">
                                <option value="${type.id}">${type.libelle}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div id="demandeInfoBox" class="info-box">
                    <!-- Informations chargées via AJAX -->
                </div>

                <hr class="divider">

                <div class="step-title">
                    <span class="step-number">2</span>
                    Ajouter les Détails du Devis
                </div>

                <div id="detailsContainer">
                    <!-- Les lignes dynamiques apparaîtront ici -->
                </div>

                <button type="button" class="btn" style="background: var(--bg-input); color: var(--text-primary); border-color: var(--border-light); margin-bottom: 24px;" onclick="addDetailRow()">
                    + Ajouter une ligne
                </button>

                <hr class="divider">

                <button type="submit" class="btn btn-primary" style="width: 100%; font-size: 1rem; padding: 14px;">
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
                    infoBox.innerHTML = `<span style="color: var(--text-muted);">Erreur lors du chargement des informations de la demande.</span>`;
                });
        }

        let detailIndex = 0;
        function addDetailRow() {
            const container = document.getElementById('detailsContainer');
            const row = document.createElement('div');
            row.className = 'detail-row';

            row.innerHTML = `
                <input type="text" name="detailsLibelles" placeholder="Ex: Forage 100m" required style="flex: 2;">
                <input type="number" step="0.01" name="detailspu" placeholder="Montant (ex: 50000)" required style="flex: 1;">
                <input type="number" step="0.01" name="detailsquantite" placeholder="combien" required style="flex: 1;">
                <button type="button" class="btn btn-danger" onclick="this.parentElement.remove()" style="margin:0; padding: 10px 15px; flex: 0;">X</button>
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
