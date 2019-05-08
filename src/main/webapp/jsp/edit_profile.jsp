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
                        <h2 align="center"><fmt:message key="edit_profile"/></h2>
                        <form method="POST" action="Hospital">
                            <input type="hidden" name="command" value="edit_profile"/>
                            <table>
                                <tr>
                                    <td>
                                        <fmt:message key="id"/>
                                    </td>
                                    <td>
                                        <input type="text" name="id" value="${user.id}" readonly="readonly"
                                               style="width: 50px"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="surname"/>
                                    </td>
                                    <td>
                                        <input type="text" name="surname" value="${user.surname}" style="width: 200px"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="name"/>
                                    </td>
                                    <td>
                                        <input type="text" name="name" value="${user.name}" style="width: 200px"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="patronymic"/>
                                    </td>
                                    <td>
                                        <input type="text" name="patronymic" value="${user.patronymic}"
                                               style="width: 200px"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="login"/>
                                    </td>
                                    <td>
                                        <input type="text" name="login" value="${user.login}" style="width: 200px"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="old_password"/>
                                    </td>
                                    <td>
                                        <input type="password" name="old_password" style="width: 200px"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="new_password"/>
                                    </td>
                                    <td>
                                        <input type="password" name="new_password" style="width: 200px"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="repeat_password"/>
                                    </td>
                                    <td>
                                        <input type="password" name="repeat_new_password" style="width: 200px"/>
                                    </td>
                                </tr>

                                <c:if test="${user.role == 'PATIENT'}">
                                    <tr>
                                        <td>
                                            <fmt:message key="birth_date"/>
                                        </td>
                                        <td>
                                            <input type="date" name="birth_date" value="${medical_card.dateOfBirth}"
                                                   style="width: 200px"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <fmt:message key="sex"/>
                                        </td>
                                        <td>
                                            <c:if test="${medical_card.sex == 0}">
                                                <input id="0" type="radio" name="sex" checked="checked" value="0"/>
                                                <label for="0"><fmt:message key="female"/></label> <br/>
                                                <input id="1" type="radio" name="sex" value="1"/> <label for="1">
                                                <fmt:message key="male"/></label>
                                            </c:if>
                                            <c:if test="${medical_card.sex == 1}">
                                                <input type="radio" name="sex" value="0"/> <fmt:message key="female"/> </br> </br>
                                                <input type="radio" name="sex" value="1" checked="checked"/><fmt:message key="male"/>
                                            </c:if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <label><fmt:message key="card_id"/>
                                                <input type="text" name="card_id" value="${medical_card.id}"
                                                       readonly="readonly" style="width: 50px"/>
                                            </label>
                                        </td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td colspan="2">
                                        <div align="center"><input type="submit" value="Save changes"/></div>
                                    </td>
                                </tr>
                            </table>
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

