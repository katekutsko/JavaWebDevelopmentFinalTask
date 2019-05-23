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
                        <h2 align="center"><fmt:message key="profile"/></h2>
                        <form method="GET" action="Hospital">
                            <input type="hidden" name="command" value="edit_profile"/>
                            <table>
                                <tr>
                                    <td>
                                        <fmt:message key="id"/>
                                    </td>
                                    <td>
                                        ${user.id}
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="surname"/>
                                    </td>
                                    <td>
                                        ${user.surname}
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="name"/>
                                    </td>
                                    <td>
                                        ${user.name}
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="patronymic"/>
                                    </td>
                                    <td>
                                        ${user.patronymic}
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="role"/>
                                    </td>
                                    <td>
                                        <fmt:message key="${user.role}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="login"/>
                                    </td>
                                    <td>
                                        ${user.login}
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:if test="${user.role == 'PATIENT'}">
                                        <fmt:message key="birth_date"/>
                                    </td>
                                    <td>
                                            ${medical_card.dateOfBirth}
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="sex"/>
                                    </td>
                                    <td>
                                        <c:if test="${medical_card.sex == 0}">
                                            <fmt:message key="female"/>
                                        </c:if>
                                        <c:if test="${medical_card.sex == 1}">
                                            <fmt:message key="male"/>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="card_id"/>
                                    </td>
                                    <td>
                                            ${medical_card.id}
                                    </td>
                                </tr>
                                </c:if>
                            </table>
                            <div align="center">
                                <button type="submit"><fmt:message key="edit_profile"/></button>
                            </div>
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

