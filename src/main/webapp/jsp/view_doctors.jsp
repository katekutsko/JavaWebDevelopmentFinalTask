<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                <c:forEach var="i" begin="0" end="${amount - 1}">
                    <div class="centerbar_top"></div>
                    <div class="centerbar">
                        <div class="centerbar_item">
                            <table cellpadding="10px" width = "85%" border="1px"  cellspacing="0">
                                <tr>
                                    <td colspan="2" style="text-align : center">
                                        <c:set var="j" value="${i}"/>
                                       <p>${ doctors[j].name }  ${ doctors[j].surname } ${ doctors[j].patronymic } </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="50%">
                                        <p> <fmt:message key="email"/></p>
                                    </td>
                                    <td align="center">
                                        <p>  ${ doctors[j].login }</p>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="centerbar_base"></div>
                </c:forEach>
            </div>
        </div>
    </div>

    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>

