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
                        <table cellpadding="10">
                            <tr>
                                <td colspan="2" style="text-align : center">
                                    <p>   ${ patient.name } ${ patient.surname } ${ patient.patronymic } </p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <ul>
                                        <li> <fmt:message key="birth_date"/>: ${ card.dateOfBirth }
                                        <li> <fmt:message key="sex"/>:
                                            <c:set var="s" value="${ card.sex }"/>
                                            <c:choose>
                                            <c:when test="${ s  == 0}">
                                                <fmt:message key="female"/>
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:message key="male"/>
                                            </c:otherwise>
                                            </c:choose>
                                        <li> <fmt:message key="card_id"/>: ${ card.id }
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
                                </td>
                                <td>
                                    <form method="GET" action="Hospital">
                                        <input type="hidden" name="discharge_case_id" value="${last_case.id}"/>
                                        <input type="hidden" name="card_id" value="${card.id}"/>
                                        <input type="hidden" name="name"
                                               value="${patient.name} ${patient.surname} ${patient.patronymic}"/>
                                        <table>
                                            <tr>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${user.role == 'DOCTOR' && (empty last_case || last_case.active == 0)}">
                                                            <button type="submit" name="command" value="add_record"
                                                                    style="width: 120px; height: 50px;"><fmt:message key="add_case"/>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button disabled="disabled" type="submit" name="command"
                                                                    value="add_record"
                                                                    style="width: 120px; height: 50px;"><fmt:message key="add_case"/>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${last_case.active == 1 && last_case.doctorId == user.id}">
                                                            <button type="submit" name="command" value="discharge"
                                                                    style="width: 120px; height: 50px"><fmt:message key="discharge"/>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button disabled="disabled" type="submit" name="command"
                                                                    value="discharge"
                                                                    style="width: 120px; height: 50px"><fmt:message key="discharge"/>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${(user.role == 'DOCTOR' || user.role == 'NURSE') && last_case.active == 1}">
                                                            <button type="submit" name="command"
                                                                    value="make_prescription"
                                                                    style="width: 120px; height: 50px"><fmt:message
                                                                    key="make_prescription"/>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button disabled="disabled" type="submit" name="command"
                                                                    value="make_prescription"
                                                                    style="width: 120px; height: 50px"><fmt:message
                                                                    key="make_prescription"/>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty last_case}">
                                                            <button type="submit" name="command" value="view_history"
                                                                    style="width: 120px; height: 50px"> <fmt:message
                                                                    key="view_history"/>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button disabled="disabled" type="submit" name="command"
                                                                    value="view_history"
                                                                    style="width: 120px; height: 50px"> <fmt:message
                                                                    key="view_history"/>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </table>
                                    </form>

                                </td>
                            </tr>

                        </table>
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
