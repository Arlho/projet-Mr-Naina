<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Détails du Devis N° ${devis.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .info-label {
            display: block;
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 0.8px;
            color: var(--text-muted);
            margin-bottom: 4px;
            font-weight: 600;
        }
        .info-value {
            font-size: 1rem;
            color: var(--text-primary);
            font-weight: 500;
        }
        .info-item {
            margin-bottom: 16px;
        }
        .total-row td {
            background: var(--bg-input);
            font-size: 1.1em;
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
                <h1>Devis N° ${devis.id}</h1>
                <p>Résumé complet et lignes du devis</p>
            </div>
        </header>

        <div class="grid-layout">
            <div class="card">
                <h3 style="border-bottom: 2px solid var(--border-light); padding-bottom: 10px; margin-bottom: 18px;">Informations Client</h3>
                <div class="info-item">
                    <span class="info-label">Nom</span>
                    <span class="info-value">${devis.demande.client.nom}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Contact</span>
                    <span class="info-value">${devis.demande.client.contact}</span>
                </div>
            </div>
            <div class="card">
                <h3 style="border-bottom: 2px solid var(--border-light); padding-bottom: 10px; margin-bottom: 18px;">Détails de la Demande</h3>
                <div class="info-item">
                    <span class="info-label">Demande N°</span>
                    <span class="info-value">${devis.demande.id}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Localisation</span>
                    <span class="info-value">${devis.demande.lieu} (${devis.demande.district})</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Date demandée</span>
                    <span class="info-value">${devis.demande.dateDemande}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Lignes de Détails du Devis</h2>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Libellé / Désignation</th>
                            <th style="text-align: right;">Montant (Ar)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${detailsList}" var="detail">
                            <tr>
                                <td>${detail.libelle}</td>
                                <td style="text-align: right; font-weight: 500;">
                                    <fmt:formatNumber value="${detail.montant}" type="number" pattern="#,##0.00"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty detailsList}">
                            <tr>
                                <td colspan="2" style="text-align: center; color: var(--text-muted); padding: 28px;">
                                    Aucun détail n'a été spécifié pour ce devis.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                    <tfoot>
                        <tr class="total-row">
                            <td style="text-align: right; font-weight: 700; padding: 16px;">TOTAL GENERAL</td>
                            <td style="text-align: right; font-weight: 700; color: var(--text-primary); font-size: 1.15em; padding: 16px;">
                                <fmt:formatNumber value="${montantTotal}" type="number" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
