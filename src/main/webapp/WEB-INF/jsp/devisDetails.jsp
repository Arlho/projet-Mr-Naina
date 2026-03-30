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
        .grid-layout { display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; }
        @media (max-width: 900px) { .grid-layout { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
    <jsp:include page="nav.jsp" />
    <div class="container">
        <header>
            <div>
                <h1>Détails du Devis N° ${devis.id}</h1>
                <p style="color: #666">Résumé complet et Lignes du Devis</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/devis/liste" class="btn">Retour à la liste</a>
            </div>
        </header>

        <div class="grid-layout" style="margin-top: 20px;">
            <div class="card">
                <h3 style="border-bottom: 2px solid #5bc0de; padding-bottom: 5px;">Informations Client</h3>
                <p><strong>Nom :</strong> ${devis.demande.client.nom}</p>
                <p><strong>Contact :</strong> ${devis.demande.client.contact}</p>
            </div>
            <div class="card">
                <h3 style="border-bottom: 2px solid #5bc0de; padding-bottom: 5px;">Détails de la Demande</h3>
                <p><strong>Demande N° :</strong> ${devis.demande.id}</p>
                <p><strong>Lieu :</strong> ${devis.demande.lieu} (${devis.demande.district})</p>
                <p><strong>Date demandée :</strong> ${devis.demande.dateDemande}</p>
            </div>
        </div>

        <div class="card" style="margin-top: 20px;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <h2>Lignes de Détails du Devis</h2>
            </div>
            
            <div style="overflow-x: auto; margin-top: 15px;">
                <table>
                    <thead>
                        <tr style="background:#f4f4f4;">
                            <th>Libellé / Désignation</th>
                            <th style="text-align:right;">Montant (Ar)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${detailsList}" var="detail">
                            <tr>
                                <td>${detail.libelle}</td>
                                <td style="text-align:right; font-weight: 500;">
                                    <fmt:formatNumber value="${detail.montant}" type="number" pattern="#,##0.00"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty detailsList}">
                            <tr>
                                <td colspan="2" style="text-align: center; color: #999; padding: 20px;">
                                    Aucun détail n'a été spécifié pour ce devis.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                    <tfoot>
                        <tr style="background:#eef;">
                            <td style="text-align:right; font-weight:bold; padding: 15px;">TOTAL GENERAL</td>
                            <td style="text-align:right; font-weight:bold; color:#d9534f; font-size:1.2em; padding: 15px;">
                                <fmt:formatNumber value="${devis.montantTotal}" type="number" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
