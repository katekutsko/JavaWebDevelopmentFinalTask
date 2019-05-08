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
                        <h3><fmt:message key="dischargement"/></h3>
                        <form method="POST" action="Hospital">
                            <input type="hidden" name="command" value="discharge"/>
                            <table>
                                <tr>
                                    <td><fmt:message key="name"/></td>
                                    <td><input type="text" readonly="readonly"
                                               value="${param.name}"/></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="card_id"/></td>
                                    <td><input type="text" readonly="readonly" name="card_id"
                                               value="${param.card_id}"/></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="discharge_date"/></td>
                                    <td><input type="date" name="discharge_date"
                                               readonly="readonly"
                                               value="${discharge_date}"/></td>
                                </tr>
                                <tr>
                                    <td><c:set var="c" value="${param.discharge_case_id}"/>

                                        <fmt:message key="case_id"/></td>
                                    <td>
                                        <input type="text" name="last_case" readonly="readonly"
                                               value="${c}"/></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="diagnosis"/></td>
                                    <td><select name="final_diagnosis" value="none" required="yes">

                                        <c:forEach items="${diagnoses}" var="diagnosis">

                                            <option value="${diagnosis}">${diagnosis}</option>

                                        </c:forEach>
                                    </select></td>
                                </tr>

                                <input type="hidden" name="doctor_id" value="${user.id}" readonly="readonly"/></br>

                                <tr>
                                    <td><fmt:message key="discharged_by"/>:</td>
                                    <td>${user.name} ${user.surname} ${user.patronymic} </td>
                                </tr>
                            </table>
                            <button type="submit"><fmt:message key="dischargement"/></button>

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

