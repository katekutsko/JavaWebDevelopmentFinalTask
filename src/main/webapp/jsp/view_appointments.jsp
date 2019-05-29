<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file='jspf/head.jspf' %>

<body>
<div id="main">

    <%@include file='jspf/header.jspf' %>

    <div id="content_header"></div>

    <div id="site_content">
        <div id="banner"></div>

        <%@include file='jspf/side_content.jspf' %>

        <c:set var="amount" value="${fn:length(cases)}"/>
        <c:set var="size" value="10"/>
        <c:set var="from" value="${param.from}"/>
        <c:set var="to" value="${from + size - 1}"/>

        <div id="content">
            <div id="centerbar_container">
                <h3 align="center">
                    <fmt:message key="appointments"/>
                </h3>
                <c:choose>
                    <c:when test="${not empty appointments}">
                        <c:forEach var="cur" items="${appointments}" begin="${from}" end="${to}">
                            <c:if test="${cur.active}">
                                <div class="centerbar_top"></div>
                                <div class="centerbar">
                                    <div class="centerbar_item">
                                        <table>
                                            <tr>
                                                <td style="width: 150px"><fmt:message key="date"/>:</td>
                                                <td> ${cur.date} </td>
                                            </tr>
                                            <tr>
                                                <td><fmt:message key="status"/>:</td>
                                                <td>
                                                    <fmt:message key="awaiting"/>
                                                </td>
                                            </tr>
                                            <c:if test="${user.role == 'PATIENT'}">
                                                <tr>
                                                    <td><fmt:message key="doctor"/>:</td>
                                                    <td> ${names[cur.doctorId]}</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2">
                                                        <br>
                                                        <a class="close"
                                                           style="margin-left: 250px; text-decoration: none;"
                                                           href="Hospital?command=cancel_appointment&from=${from}&id=${cur.id}#darkening">
                                                            <fmt:message key="to_cancel"/>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:if>
                                            <c:if test="${user.role == 'DOCTOR'}">
                                                <tr>
                                                    <td><fmt:message key="patient"/>:</td>
                                                    <td> ${names[cur.patientId]}</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2">
                                                        <br>
                                                        <c:set var="last_case" value="${cases[cur.patientId]}"/>
                                                        <c:if test="${last_case.active == 1}">
                                                            <a class="close"
                                                               style="text-decoration: none; margin-left: 30px;"
                                                               href="Hospital?command=make_prescription&card_id=${cur.patientId}&last_case_id=${last_case.id}">
                                                                <fmt:message key="prescription"/>
                                                            </a>
                                                            <c:if test="${last_case.doctorId == user.id}">
                                                                <a class="close"
                                                                   style="text-decoration: none; margin-left: 30px;"
                                                                   href="Hospital?command=discharge&card_id=${cur.patientId}&name=${names[cur.patientId]}&discharge_case_id=${last_case.id}">
                                                                    <fmt:message key="discharge"/>
                                                                </a>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${last_case.active == 0 || empty cases}">
                                                            <a class="close"
                                                               style="text-decoration: none; margin-left: 30px;"
                                                               href="Hospital?command=add_record&card_id=${param.card_id}&name=${names[cur.patientId]}">
                                                                <fmt:message key="add_case"/>
                                                            </a>
                                                        </c:if>
                                                        <a class="close"
                                                           style="text-decoration: none; margin-left: 30px;"
                                                           href="Hospital?command=decline_appointment&from=${from}&id=${cur.id}&#darkening">
                                                            <fmt:message key="decline"/>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </table>
                                    </div>
                                </div>
                                <div class="centerbar_base"></div>
                            </c:if>
                        </c:forEach>

                        <ctg:changePage from="${from}" size="${size}" amount="${amount}" command="${param.command}"/>

                        <div id="darkening">
                            <div id="window">
                                <form action="Hospital" method="post">
                                    <fmt:message key="confirm_cancel_appointment"/>
                                    <button name="appointment_id" value="${param.id}" style="font-size: 9pt">
                                        <fmt:message key="yes"/>
                                    </button>
                                    <input type="hidden" name="command" value="cancel_appointment"/>
                                    <a href="#" class="close" style="text-decoration: none;"><fmt:message
                                            key="cancel"/></a>
                                </form>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="centerbar_top"></div>
                        <div class="centerbar">
                            <div class="centerbar_item">
                                <fmt:message key="no_results"/>
                                <c:if test="${user.role == 'PATIENT'}">
                                    <a class="close" style="text-decoration: none; margin-left: 100px"
                                       href="Hospital?command=view_doctors">
                                        <fmt:message key="make_appointment"/>
                                    </a>
                                </c:if>
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

