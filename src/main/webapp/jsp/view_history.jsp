<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file='jspf/head.jspf' %>

<body>
<div id="main">

    <%@include file='jspf/header.jspf' %>

    <script language="JavaScript">
        <%@include file='../js/hideElement.js' %>
    </script>

    <div id="content_header"></div>

    <div id="site_content">
        <div id="banner"></div>

        <%@include file='jspf/side_content.jspf' %>

        <c:set var="amount" value="${fn:length(cases)}"/>
        <c:set var="size" value="3"/>
        <c:set var="from" value="${param.from}"/>
        <c:set var="to" value="${from + size - 1}"/>


        <div id="content">
            <div id="centerbar_container">
                <c:if test="${user.role != 'PATIENT'}">
                    <h3 align="center"><a href="Hospital?command=select_patient&card_id=${card_id}"><c:out
                            value="${patient_name}"/></a></h3>
                </c:if>
                <c:if test="${user.role == 'PATIENT'}">
                    <h2 style="margin-left: 250px;"><fmt:message key="history"/></h2>
                </c:if>
                <c:choose>
                    <c:when test="${not empty cases}">
                        <c:forEach var="cur" items="${cases}" begin="${from}" end="${to}">

                            <div class="centerbar_top"></div>
                            <div class="centerbar">
                                <div class="centerbar_item">

                                    <h3 align="center"><fmt:message key="case"/>#${cur.id}

                                    </h3>
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
                                            <td><c:if test="${not empty cur.finalDiagnosis}">
                                                ${ cur.finalDiagnosis} </c:if>
                                            </td>
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


                                    <form method="POST" action="Hospital">
                                        <input type="hidden" name="name" value="${patient_name}"/>
                                        <input type="hidden" name="case_id" value="${cur.id}"/>
                                        <c:if test="${user.id == cur.doctorId}">
                                            <button type="submit" formmethod="get" name="command" value="edit_case">
                                                <fmt:message key="edit"/></button>

                                            <a class="close"
                                               style="text-decoration: none; font: normal 100% 'trebuchet ms', sans-serif;"
                                               href="${requestScope['javax.servlet.forward.context_path']}${requestScope['javax.servlet.forward.servlet_path']}?${requestScope['javax.servlet.forward.query_string']}&case_id=${cur.id}#zatemnenie">
                                                <fmt:message key="delete"/></a>

                                            <div id="darkening">
                                                <div id="window">
                                                    <p><fmt:message key="confirm_delete"/></p>
                                                    <a class="close"
                                                       href="${requestScope['javax.servlet.forward.context_path']}${requestScope['javax.servlet.forward.servlet_path']}?command=delete_case&case_id=${param.case_id}">
                                                        <fmt:message key="delete"/></a>
                                                    <a href="#" class="close"><fmt:message key="cancel"/></a>
                                                </div>
                                            </div>

                                        </c:if>
                                        <button type="button" onclick="javascript:hide(${cur.id})">
                                            <fmt:message
                                                    key="view_prescriptions"/></button>
                                    </form>

                                    <c:set var="prescriptions"
                                           value="${prescriptions_by_case_id[(cur.id).intValue()]}"/>
                                    <c:if test="${not empty prescriptions}">
                                        <table style="margin-top: 20px" id="${cur.id}" hidden="hidden">
                                            <tr>
                                                <fmt:message key="view_prescriptions"/>
                                            </tr>
                                            <c:set var="i" value="0" scope="page"/>
                                            <c:forEach var="pres" items="${prescriptions}">
                                                <c:if test="${not empty pres}">
                                                    <tr>
                                                        <td>
                                                           ${i = i+1}. <fmt:message key="date"/>: ${pres.date}, <fmt:message
                                                                key="type"/>: <fmt:message key="${pres.type}"/> "${pres.details}"
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td><fmt:message key="doctor"/>: ${doctor_names[pres.doctorId]} </td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                        </table>
                                    </c:if>
                                </div>
                            </div>
                            <div class="centerbar_base">
                            </div>
                        </c:forEach>

                        <ctg:changePage from="${from}" size="${size}" amount="${amount}" command="${param.command}"
                                        cardId="${param.card_id}" name="${param.name}"/>

                    </c:when>
                    <c:otherwise>
                        <div class="centerbar_top"></div>
                        <div class="centerbar">
                            <div class="centerbar_item">
                                <fmt:message key="${message}"/>
                            </div>
                        </div>
                        <div class="centerbar_base">
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </div>
    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>

