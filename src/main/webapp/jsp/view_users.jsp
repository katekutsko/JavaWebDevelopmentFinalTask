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
        <c:set var="size" value="10"/>
        <c:set var="from" value="${param.from}"/>
        <c:set var="to" value="${from + size - 1}"/>

        <div id="content">
            <div id="centerbar_container">
                <div class="centerbar_top"></div>
                <div class="centerbar">
                    <div class="centerbar_item">

                        <table style="font-size: 8pt; margin: 0 0 0 0;" cellpadding="5px" width="85%" border="1px"
                               cellspacing="0">
                            <tr>
                                <td width="5%">ID</td>
                                <td width="30%"><fmt:message key="full_name"/></td>
                                <td width="20%"><fmt:message key="login"/></td>
                                <td width="20%"><fmt:message key="role"/></td>
                                <td width="10%"><fmt:message key="blocking"/></td>
                                <td width="15%"></td>
                            </tr>
                            <c:if test="${empty users}">
                                <p><fmt:message key="no_results"/>
                                    <fmt:message key="${error}"/></p>
                            </c:if>
                            <form action="Hospital" method="POST">
                                <!--input type="hidden" name="command" value="delete_user"/-->

                                <c:forEach items="${users}" var="current_user" begin="${from}" end="${to}">
                                    <tr>
                                        <td>${current_user.id} </td>
                                        <td>${current_user.surname} ${current_user.name} ${current_user.patronymic} </td>
                                        <td>${current_user.login} </td>
                                        <td><fmt:message key="${roles[current_user.role.ordinal]}"/></td>
                                        <td>

                                            <c:if test="${current_user.role != 'ADMINISTRATOR'}">
                                                <a href="${requestScope['javax.servlet.forward.context_path']}${requestScope['javax.servlet.forward.servlet_path']}?${requestScope['javax.servlet.forward.query_string']}&next_command=block_user&id=${current_user.id}#darkening">
                                                    <c:if test="${current_user.blocked == false}">
                                                        <!--fmt:message key="block"/-->
                                                        <img class="lang" src="style/unblock.png" />
                                                    </c:if>
                                                    <c:if test="${current_user.blocked == true}">
                                                        <!--fmt:message key="unblock"/-->
                                                        <img class="lang" src="style/block.png"/>
                                                    </c:if>
                                                </a>
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:if test="${(current_user.role == 'PATIENT') || (current_user.id != user.id && current_user.role == 'ADMINISTRATOR')}">
                                                <a href="${requestScope['javax.servlet.forward.context_path']}${requestScope['javax.servlet.forward.servlet_path']}?${requestScope['javax.servlet.forward.query_string']}&next_command=delete_user&id=${current_user.id}#darkening"><fmt:message
                                                        key="delete"/></a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <div id="darkening">
                                    <div id="window">
                                        <fmt:message key="confirm_delete"/><br>
                                        <button name="user_id" value="${param.id}"
                                                style="font-size: 9pt"><fmt:message
                                                key="yes"/></button>
                                        <input type="hidden" name="command" value="${param.next_command}"/>
                                        <a href="#" class="close"><fmt:message key="cancel"/></a>
                                    </div>
                                </div>
                            </form>


                        </table>

                        <ctg:changePage from="${from}" size="${size}" amount="${amount}" command="${param.command}"/>
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

