<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file='jspf/head.jspf' %>

<body>
<div id="main">

    <%@include file='jspf/header.jspf' %>

    <div id="content_header"></div>

    <div id="site_content">
        <div id="banner"></div>

        <%@include file='jspf/side_content.jspf' %>

        <div id="content">
            <div id="centerbar_container">
                <h2 align="center"><fmt:message key="prescriprions"/></h2>

                <div class="centerbar_top"></div>
                <div class="centerbar">
                    <div class="centerbar_item">
                        <fmt:message key="${message}"/>
                        <c:forEach var="pres" items="${prescriptions}">
                            <table border="1px" cellspacing="0" width="350px" align="center"
                                   style="margin-bottom: 10px;">
                                <tr>
                                    <td colspan="2"><p align="center"><fmt:message key="prescription"/>#${pres.id}</p>
                                    </td>
                                </tr>
                                <c:if test="${not empty pres}">
                                    <tr>
                                        <td><fmt:message key="date"/></td>
                                        <td> ${pres.date} </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message key="type"/></td>
                                        <td> ${pres.type}</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <fmt:message key="details"/></td>
                                        <td> ${pres.details} </td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message key="doctor"/></td>
                                        <td> ${pres.doctorId} </td>
                                    </tr>
                                </c:if>
                            </table>
                        </c:forEach>
                    </div>
                </div>
                <div class="centerbar_base">
                </div>
            </div>
        </div>
    </div>
    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>

