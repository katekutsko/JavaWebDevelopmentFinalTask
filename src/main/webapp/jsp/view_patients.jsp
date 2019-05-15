<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file='jspf/head.jspf' %>

<body>
<div id="main">

    <%@include file='jspf/header.jspf' %>

    <div id="content_header"></div>

    <div id="site_content">
        <div id="banner"></div>

        <%@include file='jspf/side_content.jspf' %>

        <c:set var="amount" value="${fn:length(patients)}"/>
        <c:set var="size" value="4"/>
        <c:set var="from" value="${param.from}"/>
        <c:set var="to" value="${from + size - 1}"/>


        <div id="content">
            <div id="centerbar_container">
                <c:forEach var="i" begin="${from}" end="${to}">
                    <c:if test="${not empty patients[i]}">
                    <div class="centerbar_top"></div>
                    <div class="centerbar">
                        <div class="centerbar_item">
                            <table cellpadding="10">
                                <tr>
                                    <td colspan="2" align="center">
                                        <c:set var="j" value="${i}"/>
                                        <h3 align="center">   ${ patients[j].name } ${ patients[j].surname } ${ patients[j].patronymic } </h3>
                                    </td>
                                </tr>
                                <tr>
                                    <td> <fmt:message key="birth_date"/>  </td> <td> ${ cards[j].dateOfBirth } </td>
                                </tr>
                                <tr> <td>
                                    <fmt:message key="sex"/>
                                </td> <td>
                                    <c:set var="s" value="${ cards[j].sex }"/>
                                    <c:choose>
                                        <c:when test="${ s  == 0}">
                                            <fmt:message key="female"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="male"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td> </tr>
                                <tr><td>
                                    <fmt:message key="card_id"/>
                                </td><td>
                                        ${ cards[j].id }
                                </td>
                                </tr> <tr>
                                    <td colspan="2" align="center">
                                        <form method="GET" action="Hospital">
                                            <input formmethod="GET" type="hidden" name="command" value="select_patient"/>
                                            <button type="submit" name="card_id" value="${cards[j].id}"> <fmt:message key="select"/></button>
                                        </form>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="centerbar_base"></div>
                    </c:if>
                </c:forEach>
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
    </div>

    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>

