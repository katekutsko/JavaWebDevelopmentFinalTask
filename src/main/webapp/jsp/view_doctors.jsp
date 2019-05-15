<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file='jspf/head.jspf' %>

<body>
<div id="main">

    <%@include file='jspf/header.jspf' %>

    <div id="content_header"></div>

    <div id="site_content">
        <div id="banner"></div>

        <%@include file='jspf/side_content.jspf' %>

        <c:set var="amount" value="${amount}"/>
        <c:set var="size" value="4"/>
        <c:set var="from" value="${param.from}"/>
        <c:set var="to" value="${from + size - 1}"/>

        <div id="content">
            <div id="centerbar_container">
                <c:forEach var="i" begin="${from}" end="${to}">
                    <c:if test="${not empty doctors[i]}">
                        <div class="centerbar_top"></div>
                        <div class="centerbar">
                            <div class="centerbar_item">
                                <table cellpadding="10px" width="85%" border="1px" cellspacing="0">
                                    <tr>
                                        <td colspan="2" style="text-align : center">
                                            <c:set var="j" value="${i}"/>
                                            <p>${ doctors[j].name } ${ doctors[j].surname } ${ doctors[j].patronymic } </p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="50%">
                                            <p><fmt:message key="email"/></p>
                                        </td>
                                        <td align="center">
                                            <p> ${ doctors[j].login }</p>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="centerbar_base"></div>
                    </c:if>
                </c:forEach>

                <c:if test="${(from + size) < amount}">
                    <a style="float: right; margin-top: 20px; margin-right: 70px;"
                       href="<c:url value="${requestScope['javax.servlet.forward.servlet_path']}">
                    <c:param name='from' value="${from + size}"/>
                       <c:param name="command" value="${param.command}"/> </c:url>">
                        <fmt:message key="next"/> </a>
                </c:if>
                <c:if test="${(from - size) ge 0}">
                    <a style="float: left; margin-top: 20px;"
                       href="<c:url value="${requestScope['javax.servlet.forward.servlet_path']}">
                    <c:param name='from' value="${from - size}"/>
                       <c:param name="command" value="${param.command}"/> </c:url>">
                        <fmt:message key="previous"/> </a>
                </c:if>
            </div>
        </div>
    </div>

    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>

