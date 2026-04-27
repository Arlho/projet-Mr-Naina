<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Statistiques - Système Forage</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .stat-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .stat-card {
            background: var(--bg-card);
            backdrop-filter: blur(10px);
            padding: 24px;
            border-radius: var(--radius-md);
            border: 1px solid var(--border-color);
            text-align: center;
            box-shadow: var(--shadow-sm);
            transition: var(--transition);
        }
        .stat-card:hover {
            box-shadow: var(--shadow-md);
            transform: translateY(-2px);
        }
        /* Carte KPI cliquable */
        .stat-card-link {
            text-decoration: none;
            color: inherit;
            display: block;
            cursor: pointer;
        }
        .stat-card-link .stat-card {
            transition: all 0.22s ease;
            border: 1px solid var(--border-color);
        }
        .stat-card-link:hover .stat-card {
            border-color: var(--accent-primary);
            box-shadow: 0 6px 24px rgba(37,99,235,0.13);
            transform: translateY(-3px);
        }
        .stat-card-link:hover .stat-value {
            color: var(--accent-primary);
        }
        .stat-card-link .stat-card::after {
            content: '↓ voir la liste';
            display: block;
            font-size: 0.72rem;
            color: var(--accent-primary);
            margin-top: 6px;
            opacity: 0;
            transition: opacity 0.2s;
            letter-spacing: 0.5px;
            text-transform: uppercase;
            font-weight: 600;
        }
        .stat-card-link:hover .stat-card::after {
            opacity: 1;
        }
        .stat-value {
            font-size: 2.2rem;
            font-weight: 700;
            color: var(--accent-primary);
            margin: 10px 0;
            line-height: 1.2;
        }
        .stat-value.money {
            color: var(--accent-danger);
            font-size: 1.8rem;
        }
        .stat-label {
            font-size: 0.85rem;
            color: var(--text-secondary);
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .h-section {
            border-bottom: 2px solid var(--border-color);
            padding-bottom: 10px;
            margin: 40px 0 20px 0;
            color: var(--text-primary);
        }
        .mini-badge {
            font-size: 0.70rem;
            padding: 2px 8px;
            border-radius: 10px;
            background: #e2e8f0;
            color: #475569;
            margin-left: 5px;
        }
        .badge-success { background: #dcfce7; color: #166534; }
        .badge-danger { background: #fee2e2; color: #991b1b; }
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
                <h1>Tableau de Bord - Statistiques</h1>
                <p>Aperçu global de l'activité de forage</p>
            </div>
        </header>

        <!-- KPI GLOBALES -->
        <div class="stat-grid">
            <div class="stat-card">
                <div class="stat-label">Chiffre d'Affaires Global</div>
                <div class="stat-value money">
                    <c:choose>
                        <c:when test="${not empty chiffreAffaire}">
                            <fmt:formatNumber value="${chiffreAffaire}" type="number" pattern="#,##0.00"/> Ar
                        </c:when>
                        <c:otherwise>0.00 Ar</c:otherwise>
                    </c:choose>
                </div>
            </div>
            <a href="#section-clients" class="stat-card-link">
                <div class="stat-card">
                    <div class="stat-label">Nombre de Clients</div>
                    <div class="stat-value">${nombreClient}</div>
                </div>
            </a>
            <a href="#section-demandes" class="stat-card-link">
                <div class="stat-card">
                    <div class="stat-label">Total des Demandes</div>
                    <div class="stat-value">${listeDemande.size()}</div>
                </div>
            </a>
            <a href="#section-statuts" class="stat-card-link">
                <div class="stat-card">
                    <div class="stat-label">Types de Statuts</div>
                    <div class="stat-value">${nombreStatus}</div>
                </div>
            </a>
        </div>

        <h2 class="h-section">Synthèse des Étapes</h2>
        <div class="stat-grid" style="grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));">
            <div class="stat-card">
                <div class="stat-label">Demandes Traitées (Création)</div>
                <div class="stat-value">${nbdemandeCree}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Examinations</div>
                <div class="stat-value">
                    ${nbexaminationCree} 
                    <span style="font-size: 1rem; color: #64748b; font-weight: normal;">créées</span>
                </div>
                <div style="display: flex; justify-content: center; gap: 10px; margin-top: 5px;">
                    <span class="mini-badge badge-success">${nbexaminationAccepte} Acceptées</span>
                    <span class="mini-badge badge-danger">${nbexaminationRefuse} Refusées</span>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Forages</div>
                <div class="stat-value">
                    ${nbforageCree} 
                    <span style="font-size: 1rem; color: #64748b; font-weight: normal;">créés</span>
                </div>
                <div style="display: flex; justify-content: center; gap: 10px; margin-top: 5px;">
                    <span class="mini-badge badge-success">${nbforageAccepte} Acceptés</span>
                    <span class="mini-badge badge-danger">${nbforageRefuse} Refusés</span>
                </div>
            </div>
        </div>

        <!-- LISTES DETAILLEES -->
        <h2 class="h-section" style="scroll-margin-top: 80px;" id="section-clients">Liste des Clients</h2>
        <div class="card">
            <h2>
                <span>Clients enregistrés</span>
                <span class="badge">${nombreClient} enregistrements</span>
            </h2>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Nom</th>
                            <th>Contact</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${clients}" var="client">
                            <tr>
                                <td><span class="badge">#${client.id}</span></td>
                                <td style="font-weight: 600;">${client.nom}</td>
                                <td>${client.contact}</td>
                                <td class="action-btns">
                                    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Voir sur Dashboard</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty clients}">
                            <tr><td colspan="4" style="text-align: center;">Aucun client</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <h2 class="h-section" style="scroll-margin-top: 80px;" id="section-demandes">Liste des Demandes</h2>
        <div class="card">
            <h2>
                <span>Toutes les Demandes</span>
                <span class="badge">${listeDemande.size()} enregistrements</span>
            </h2>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Demande N°</th>
                            <th>Date</th>
                            <th>Client</th>
                            <th>Localisation</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listeDemande}" var="demande">
                            <tr>
                                <td><span class="badge">#${demande.id}</span></td>
                                <td>${demande.dateDemande}</td>
                                <td style="font-weight: 600;">${demande.client.nom}</td>
                                <td>${demande.lieu} (${demande.district})</td>
                                <td class="action-btns">
                                    <a href="${pageContext.request.contextPath}/demande/details/${demande.id}" class="btn" style="background: var(--accent-primary); color: white; border: none;">Détails</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listeDemande}">
                            <tr><td colspan="5" style="text-align: center;">Aucune donnée</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <h2 class="h-section" style="scroll-margin-top: 80px;" id="section-statuts">Types de Statuts</h2>
        <div class="card">
            <h2>
                <span>Tous les Statuts</span>
                <span class="badge">${nombreStatus} types</span>
            </h2>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Libellé</th>
                            <th>Demandes concernées</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${allStatus}" var="s">
                            <tr>
                                <td><span class="badge">#${s.id}</span></td>
                                <td><span class="status-badge">${s.libelle}</span></td>
                                <td style="color: var(--text-muted); font-size: 0.85rem;">—</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty allStatus}">
                            <tr><td colspan="3" style="text-align: center;">Aucun statut</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="grid-layout">
            <!-- Historique Demande Créée -->
            <div class="card">
                <h2>Historique : Demandes Créées</h2>
                <div style="overflow-x: auto;">
                    <table>
                        <thead>
                            <tr>
                                <th>Demande N°</th>
                                <th>Date Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${demandeCree}" var="hist">
                                <tr>
                                    <td><span class="badge">#${hist.demande.id}</span></td>
                                    <td>${hist.dateStatus}</td>
                                    <td class="action-btns">
                                        <a href="${pageContext.request.contextPath}/demande/details/${hist.demande.id}" class="btn btn-primary">Détails</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty demandeCree}">
                                <tr><td colspan="3" style="text-align: center;">Aucune donnée</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Historique Examinations Créées -->
            <div class="card">
                <h2>Historique : Examinations Créées</h2>
                <div style="overflow-x: auto;">
                    <table>
                        <thead>
                            <tr>
                                <th>Demande N°</th>
                                <th>Date Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${examinationCree}" var="hist">
                                <tr>
                                    <td><span class="badge">#${hist.demande.id}</span></td>
                                    <td>${hist.dateStatus}</td>
                                    <td class="action-btns">
                                        <a href="${pageContext.request.contextPath}/demande/details/${hist.demande.id}" class="btn btn-primary">Détails</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty examinationCree}">
                                <tr><td colspan="3" style="text-align: center;">Aucune donnée</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <!-- Historique Forages Créés -->
            <div class="card">
                <h2>Historique : Forages Créés</h2>
                <div style="overflow-x: auto;">
                    <table>
                        <thead>
                            <tr>
                                <th>Demande N°</th>
                                <th>Date Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${forageCree}" var="hist">
                                <tr>
                                    <td><span class="badge">#${hist.demande.id}</span></td>
                                    <td>${hist.dateStatus}</td>
                                    <td class="action-btns">
                                        <a href="${pageContext.request.contextPath}/demande/details/${hist.demande.id}" class="btn btn-primary">Détails</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty forageCree}">
                                <tr><td colspan="3" style="text-align: center;">Aucune donnée</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <!-- (Optional) Other lists like forageAccepte, examinationRefuse can go here -->
            <div class="card">
                <h2>Historique : Forages/Examinations Acceptés</h2>
                <div style="overflow-x: auto;">
                    <table>
                        <thead>
                            <tr>
                                <th>Demande N°</th>
                                <th>Type</th>
                                <th>Date Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${forageAccepte}" var="hist">
                                <tr>
                                    <td><span class="badge">#${hist.demande.id}</span></td>
                                    <td>Forage</td>
                                    <td>${hist.dateStatus}</td>
                                    <td class="action-btns">
                                        <a href="${pageContext.request.contextPath}/demande/details/${hist.demande.id}" class="btn btn-primary">Détails</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:forEach items="${examinationAccepte}" var="hist">
                                <tr>
                                    <td><span class="badge">#${hist.demande.id}</span></td>
                                    <td>Exam</td>
                                    <td>${hist.dateStatus}</td>
                                    <td class="action-btns">
                                        <a href="${pageContext.request.contextPath}/demande/details/${hist.demande.id}" class="btn btn-primary">Détails</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            
        </div>
    </div>
</body>
</html>
