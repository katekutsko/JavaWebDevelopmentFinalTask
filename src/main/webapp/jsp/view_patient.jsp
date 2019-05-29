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
                <div class="centerbar_top"></div>
                <div class="centerbar">
                    <div class="centerbar_item">
                        <h3>   ${ patient.name } ${ patient.surname } ${ patient.patronymic } </h3>

                        <ul style="font-size: 12pt">
                            <li> <fmt:message key="birth_date"/>: ${ medical_card.dateOfBirth }
                            <li> <fmt:message key="sex"/>:
                                <c:set var="s" value="${ medical_card.sex }"/>
                                <c:choose>
                                <c:when test="${ s  == 0}">
                                    <fmt:message key="female"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="male"/>
                                </c:otherwise>
                                </c:choose>
                            <li>
                                <fmt:message key="phone_number"/>: ${patient.phoneNumber}
                            </li>
                            <li>
                                <fmt:message key="email"/>: ${patient.login}
                            </li>
                            <li> <fmt:message key="card_id"/>: ${ medical_card.id }
                            <li> <fmt:message key="status"/>:
                                <c:choose>
                                <c:when test="${empty last_case}">
                                    <fmt:message key="no_records"/>
                                </c:when>

                                <c:when test="${ last_case.active == 1}">
                                    <fmt:message key="inpatient"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="discharged"/>
                                </c:otherwise>
                                </c:choose>
                        </ul>
                        <form method="GET" action="Hospital">
                            <input type="hidden" name="discharge_case_id" value="${last_case.id}"/>
                            <input type="hidden" name="card_id" value="${medical_card.id}"/>
                            <input type="hidden" name="name"
                                   value="${patient.name} ${patient.surname} ${patient.patronymic}"/>
                            <c:if test="${(user.role == 'DOCTOR' || user.role == 'NURSE') && last_case.active == 1}">
                                <button type="submit" name="command" value="make_prescription"
                                        style="width: 120px; height: 50px"><fmt:message key="make_prescription"/>
                                </button>
                            </c:if>
                            <c:if test="${not empty last_case}">
                                <button type="submit" name="command" value="view_history"
                                        style="width: 120px; height: 50px"><fmt:message key="view_history"/>
                                </button>
                            </c:if>
                        </form>
                    </div>
                </div>
                <div class="centerbar_base"></div>
            </div>
        </div>
    </div>

    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>
