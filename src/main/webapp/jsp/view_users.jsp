<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file='jspf/head.jspf' %>

<body>
<div id="main">

    <%@include file='jspf/header.jspf' %>

    <div id="content_header"></div>

    <div id="site_content">
        <div id="banner"></div>

        <%@include file='jspf/side_content.jspf' %>

        <c:set var="amount" value="${fn:length(users)}"/>
        <c:set var="size" value="4"/>
        <c:set var="from" value="${param.from}"/>
        <c:set var="to" value="${from + size - 1}"/>

        <div id="content">
            <div id="centerbar_container">
                <div class="centerbar_top"></div>
                <div class="centerbar">
                    <div class="centerbar_item">
                        <form action="Hospital" method="POST">
                            <input type="hidden" name="command" value="delete_user"/>
                            <table style="font-size: 8pt; margin: 0 0 0 0;" cellpadding="5px" width="85%" border="1px"
                                   cellspacing="0">
                                <tr>
                                    <td width="10%">ID</td>
                                    <td width="35%"><fmt:message key="full_name"/></td>
                                    <td width="20%"><fmt:message key="login"/></td>
                                    <td width="20%"><fmt:message key="role"/></td>
                                    <td width="15%"></td>
                                </tr>
                                <c:if test="${empty users}">
                                    <p><fmt:message key="${error}"/> </p>
                                </c:if>
                                <c:forEach items="${users}" var="current_user" begin="${from}" end="${to}">
                                    <tr>
                                        <td>${current_user.id} </td>
                                        <td>${current_user.surname} ${current_user.name} ${current_user.patronymic} </td>
                                        <td>${current_user.login} </td>
                                        <td><fmt:message key="${roles[current_user.role.ordinal]}"/></td>
                                        <td>
                                            <c:if test="${current_user.role != 'ADMINISTRATOR'}">
                                                <button name="user_id" value="${current_user.id}"
                                                        style="font-size: 9pt"><fmt:message
                                                        key="delete"/></button>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </form>
                        <c:if test="${(from + size) < amount}">
                            <a style="float: right; margin-top: 20px; margin-right: 70px;" href="<c:url value="${requestScope['javax.servlet.forward.servlet_path']}">
                    <c:param name='from' value="${from + size}"/>

                       <c:param name="command" value="${param.command}"/> </c:url>">
                                <fmt:message key="next"/> </a>
                        </c:if>
                        <c:if test="${(from - size) ge 0}">
                            <a style="float: left; margin-top: 20px;" href="<c:url value="${requestScope['javax.servlet.forward.servlet_path']}">
                    <c:param name='from' value="${from - size}"/>

                       <c:param name="command" value="${param.command}"/> </c:url>">
                                <fmt:message key="previous"/> </a>
                        </c:if>
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

