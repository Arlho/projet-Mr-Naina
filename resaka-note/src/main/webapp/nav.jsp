<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="fixed-header">
    <h1 class="etu">ETU3558</h1>

    <nav class="nav">
        <a href="${pageContext.request.contextPath}/simulation"
            class="${requestScope['javax.servlet.forward.servlet_path'] == '/index.jsp' || requestScope['javax.servlet.forward.servlet_path'] == '/result.jsp' ? 'active' : ''}">
            Simulation</a>
        <a href="${pageContext.request.contextPath}/candidats"
            class="${requestScope['javax.servlet.forward.servlet_path'] == '/candidats.jsp' ? 'active' : ''}">
            Candidats</a>
        <a href="${pageContext.request.contextPath}/correcteurs"
            class="${requestScope['javax.servlet.forward.servlet_path'] == '/correcteurs.jsp' ? 'active' : ''}">
            Correcteurs</a>
        <a href="${pageContext.request.contextPath}/matieres"
            class="${requestScope['javax.servlet.forward.servlet_path'] == '/matieres.jsp' ? 'active' : ''}">
            Matières</a>
        <a href="${pageContext.request.contextPath}/operateurs"
            class="${requestScope['javax.servlet.forward.servlet_path'] == '/operateurs.jsp' ? 'active' : ''}">
            Opérateurs</a>
        <a href="${pageContext.request.contextPath}/resolutions"
            class="${requestScope['javax.servlet.forward.servlet_path'] == '/resolutions.jsp' ? 'active' : ''}">
            Résolutions</a>
        <a href="${pageContext.request.contextPath}/parametres"
            class="${requestScope['javax.servlet.forward.servlet_path'] == '/parametres.jsp' ? 'active' : ''}">
            Paramètres</a>
        <a href="${pageContext.request.contextPath}/notes"
            class="${requestScope['javax.servlet.forward.servlet_path'] == '/notes.jsp' ? 'active' : ''}">Notes</a>
        <a href="${pageContext.request.contextPath}/resultats"
            class="${requestScope['javax.servlet.forward.servlet_path'] == '/resultats.jsp' ? 'active' : ''}"> Résultats</a>
    </nav>
</div>