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
                    <p><fmt:message key="no_results"/></p>
                </c:if>
                <c:forEach var="i" begin="${from}" end="${to}">
                    <c:if test="${not empty doctors[i]}">
                        <div class="centerbar_top"></div>
                        <div class="centerbar">
                            <div class="centerbar_item">
                                <table cellpadding="10px" width="85%" cellspacing="0">
                                    <tr>
                                        <td colspan="2" style="text-align : center">
                                            <c:set var="j" value="${i}"/>
                                            <h3>${ doctors[j].surname } ${ doctors[j].name } ${ doctors[j].patronymic }</h3>
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
                                            <fmt:message key="phone_number"/>
                                        </td>
                                        <td align="center">
                                                ${ doctors[j].phoneNumber }
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
                                <c:if test="${not empty user}">
                                    <input type="hidden" name="command" value="make_appointment">
                                    <div style="margin-left: 200px; margin-top: 20px;">
                                        <c:if test="${doctors[j].role == 'DOCTOR'}">
                                            <a class="close" style="text-decoration: none;"
                                               href="Hospital?command=view_doctors&doctor_id=${doctors[j].id}#darkening">
                                                <fmt:message key="make_appointment"/>
                                            </a>
                                        </c:if>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div class="centerbar_base"></div>
                    </c:if>
                </c:forEach>

                <div id="darkening">
                    <div id="date">
                        <form action="Hospital" method="post">
                            <h3><fmt:message key="choose_date"/></h3>
                            <input type="date" name="date" value="${today}"/> <br>
                            <button name="doctor_id" value="${param.doctor_id}" style="font-size: 9pt">ОК</button>
                            <input type="hidden" name="command" value="make_appointment"/>
                            <a href="#" class="close" style="text_decoration: none"><fmt:message key="cancel"/></a>
                        </form>
                    </div>
                </div>

                <ctg:changePage from="${from}" size="${size}" amount="${amount}" command="${param.command}"/>
            </div>
        </div>
    </div>

    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>

