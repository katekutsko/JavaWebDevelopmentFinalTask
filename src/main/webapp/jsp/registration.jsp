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
                        <h3><fmt:message key="registration"/></h3>
                        <form method="POST" action="Hospital">
                            <div align="center">
                                <input type="hidden" name="command" value="register"/>
                                <c:if test="${not empty error}"> <fmt:message key="${error}"/> </c:if> </br>
                                <fmt:message key="surname"/><p><input type="text" name="surname" maxlength="20"
                                                                       autocomplete="off"/></p>
                                <fmt:message key="name"/> <p><input type="text" name="name" maxlength="20"
                                                                    autocomplete="off"/></p>

                                <fmt:message key="patronymic"/><p><input type="text" name="patronymic"
                                                                         autocomplete="off" maxlength="20"/></p>
                                <c:if test="${empty user}">
                                    <fmt:message key="sex"/>
                                    <p>
                                        <label> <input type="radio" name="sex" value="0"/><fmt:message key="female"/>
                                        </label></p>
                                    <p>
                                        <label> <input type="radio" name="sex" value="1"/> <fmt:message key="male"/>
                                        </label></p>
                                    <fmt:message key="birth_date"/> <p><input type="date" name="date" min="1920-01-01"
                                                                              max="2002-02-02"
                                                                              value="1970-01-01"/></p>
                                </c:if>
                                <c:if test="${user.role == 'ADMINISTRATOR'}">
                                    </br> <fmt:message key="role"/>
                                    <p><select name="role">
                                        <option value="${roles[0]}"><fmt:message key="${roles[0]}"/></option>
                                        <option value="${roles[1]}"><fmt:message key="${roles[1]}"/></option>
                                        <option value="${roles[3]}"><fmt:message key="${roles[3]}"/></option>
                                    </select>
                                    </p>
                                </c:if>
                                <fmt:message key="email"/>:
                                <p><input type="text" name="login" maxlength="20" autocomplete="off"/></p>
                                <fmt:message key="password"/>:
                                <p><input type="password" name="password" maxlength="10"/></p>
                                <fmt:message key="repeat_password"/>:
                                <p><input type="password" name="repeat_password" maxlength="10"/></p>
                                <p>
                                    <button type="submit"><fmt:message key="registration"/></button>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="centerbar_base" style="margin-top: 1px;"></div>
            </div>
        </div>
    </div>
    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>
