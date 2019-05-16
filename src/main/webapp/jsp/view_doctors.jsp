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
                <c:if test="${empty doctors}">
                    <p> <fmt:message key="no_results"/> </p>
                </c:if>
                <c:forEach var="i" begin="${from}" end="${to}">
                    <c:if test="${not empty doctors[i]}">
                        <div class="centerbar_top"></div>
                        <div class="centerbar">
                            <div class="centerbar_item">
                                <table cellpadding="10px" width="85%" border="1px" cellspacing="0">
                                    <tr>
                                        <td colspan="2" style="text-align : center">
                                            <c:set var="j" value="${i}"/>
                                            ${ doctors[j].name } ${ doctors[j].surname } ${ doctors[j].patronymic }
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="50%">
                                            <fmt:message key="email"/>
                                        </td>
                                        <td align="center">
                                            ${ doctors[j].login }
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="50%">
                                           <fmt:message key="category"/>
                                        </td>
                                        <td align="center">
                                           <fmt:message key="${ doctors[j].role }"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="centerbar_base"></div>
                    </c:if>
                </c:forEach>
                <ctg:changePage from="${from}" size="${size}" amount="${amount}" command="${param.command}"/>
            </div>
        </div>
    </div>

    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>

