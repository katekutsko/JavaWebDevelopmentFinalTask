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
                <h2 align="center"><c:out value="${patient_name}"/></h2>
                <c:if test="${not empty cases}">
                    <form method="POST" action="Hospital">
                        <input type="hidden" name="command" value="edit_case"/>
                        <c:forEach var="cur" items="${cases}">

                            <div class="centerbar_top"></div>
                            <div class="centerbar">
                                <div class="centerbar_item">


                                    <h3 align="center"><fmt:message key="case"/>#${cur.id}
                                        <c:if test="${sessionScope.user.role == 'DOCTOR' && user.id == cur.doctorId}">
                                            <button type="submit" formmethod="get" name="command" value="edit_case">
                                                <fmt:message
                                                        key="edit"/></button>
                                            <input type="hidden" name="case_id" value="${cur.id}"/>
                                            <input type="hidden" name="name" value="${patient_name}"/>
                                        </c:if></h3>
                                    <table>
                                        <tr>
                                            <td><fmt:message key="admission_date"/></td>
                                            <td> ${cur.admissionDate} </td>
                                        </tr>
                                        <tr>
                                            <td><fmt:message key="discharge_date"/></td>
                                            <td> ${cur.dischargeDate} </td>
                                        </tr>
                                        <tr>
                                            <td><fmt:message key="diagnosis"/></td>
                                            <td><c:if
                                                    test="${cur.finalDiagnosis != 'UNDEFINED'}"> ${cur.finalDiagnosis} </c:if></td>
                                        </tr>
                                        <tr>
                                            <td><fmt:message key="complaints"/></td>
                                            <td> ${cur.complaints} </td>
                                        </tr>
                                        <tr>
                                            <c:set var="doctor" value="${cur.doctorId}"/>
                                            <td><fmt:message key="doctor"/></td>
                                            <td> ${doctor_names[doctor]}</td>
                                        </tr>
                                    </table>

                                    <c:set var="case_id" value="${cur.id}"/>
                                    <c:forEach var="pres" items="${prescriptions_by_case_id[(case_id).intValue()]}">
                                    <table border="1px" cellspacing="0" width="350px" align="center">
                                        <tr>
                                            <td colspan="2"><h3 align="center"><fmt:message key="prescription"/></h3></td>
                                        </tr>
                                        <c:if test="${not empty pres}">
                                            <tr>
                                                <td width="40%"><fmt:message key="date"/></td>
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
                                        </c:forEach>
                                    </table>

                                </div>
                            </div>
                            <div class="centerbar_base">
                            </div>
                        </c:forEach>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>

