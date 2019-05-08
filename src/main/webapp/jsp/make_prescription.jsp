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
                        <h3><fmt:message key="prescription"/></h3>

                        <form method="POST" action="Hospital">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" name="command" value="make_prescription"/>
                                        <input type="hidden" name="last_case_id" value="${param.discharge_case_id}"/>

                                        <fmt:message key="type"/>
                                    </td>
                                    <td>
                                        <select name="type">

                                            <c:set var="cur_user" value="${sessionScope.user}"/>
                                            <c:set var="access_level" value="${cur_user.role.ordinal}"/>

                                            <c:forEach items="${types}" var="type">

                                                <c:if test="${type.access >= access_level}">

                                                    <option value="${type}"><fmt:message key="${type}"/></option>
                                                </c:if>

                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="details"/></td>
                                    <td><input type="text" name="details" autocomplete="off"
                                               required="yes"/></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="card_id"/></td>
                                    <td><input type="text" name="card_id" value="${param.card_id}"
                                               readonly="readonly" style="width: 50px"/></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="date"/></td>
                                    <td><input type="date" name="date" value="${date}"
                                               required="yes"/></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="${user.role}"/>:</td>
                                    <td>${user.name} ${user.surname} ${user.patronymic}</td>
                                </tr>
                                <input type="hidden" name="doctor_id" value="${user.id}"/>

                            </table>
                            <button type="submit"><fmt:message key="add"/></button>
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
