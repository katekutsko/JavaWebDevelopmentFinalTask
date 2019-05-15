<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file='jspf/head.jspf' %>

<body>
<div id="main">

    <%@include file='jspf/header.jspf' %>

    <script language="JavaScript">
        function hide(id) {
            var table = document.getElementById(id);
            var hidden = "hidden";
            if (table.getAttribute(hidden) == null)
                table.setAttribute(hidden, "true");
            else table.removeAttribute(hidden);
        }
    </script>

    <div id="content_header"></div>

    <div id="site_content">
        <div id="banner"></div>

        <%@include file='jspf/side_content.jspf' %>

        <c:set var="amount" value="${fn:length(cases)}"/>
        <c:set var="size" value="3"/>
        <c:set var="from" value="${param.from}"/>
        <c:set var="to" value="${from + size - 1}"/>


        <div id="content">
            <div id="centerbar_container">
                <h2 align="center"><c:out value="${patient_name}"/></h2>
                <c:choose>
                    <c:when test="${not empty cases}">
                        <form method="POST" action="Hospital">
                            <input type="hidden" name="command" value="edit_case"/>

                            <c:forEach var="cur" items="${cases}" begin="${from}" end="${to}">

                                <div class="centerbar_top"></div>
                                <div class="centerbar">
                                    <div class="centerbar_item">


                                        <h3 align="center"><fmt:message key="case"/>#${cur.id}
                                            <c:if test="${user.id == cur.doctorId}">
                                                <button type="submit" formmethod="get" name="command" value="edit_case">
                                                    <fmt:message
                                                            key="edit"/></button>
                                                <input type="hidden" name="case_id" value="${cur.id}"/>
                                                <input type="hidden" name="name" value="${patient_name}"/>
                                            </c:if>
                                            <button type="button" onclick="javascript:hide(${cur.id})">
                                                <fmt:message
                                                        key="view_prescriptions"/></button>
                                        </h3>
                                        <table>
                                            <tr>
                                                <td><fmt:message key="admission_date"/></td>
                                                <td> ${cur.admissionDate} </td>
                                            </tr>
                                            <tr>
                                                <td><fmt:message key="discharge_date"/></td>
                                                <td> ${cur.dischargeDate} </td>
                                            </tr>
                                            <tr>
                                                <td><fmt:message key="diagnosis"/></td>
                                                <td><c:if
                                                        test="${cur.finalDiagnosis != 'UNDEFINED'}"> ${cur.finalDiagnosis} </c:if></td>
                                            </tr>
                                            <tr>
                                                <td><fmt:message key="complaints"/></td>
                                                <td> ${cur.complaints} </td>
                                            </tr>
                                            <tr>
                                                <c:set var="doctor" value="${cur.doctorId}"/>
                                                <td><fmt:message key="doctor"/></td>
                                                <td> ${doctor_names[doctor]}</td>
                                            </tr>
                                        </table>

                                        <c:set var="case_id" value="${cur.id}"/>
                                        <table border="1px" cellspacing="0" width="350px" align="center" id="${cur.id}"
                                               hidden="true">
                                            <c:forEach var="pres"
                                                       items="${prescriptions_by_case_id[(case_id).intValue()]}">
                                                <c:if test="${not empty pres}">
                                                    <tr>
                                                        <td colspan="2"><h3 align="center"><fmt:message
                                                                key="prescription"/></h3></td>
                                                    </tr>

                                                    <tr>
                                                        <td width="40%"><fmt:message key="date"/></td>
                                                        <td> ${pres.date} </td>
                                                    </tr>
                                                    <tr>
                                                        <td><fmt:message key="type"/></td>
                                                        <td> ${pres.type}</td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <fmt:message key="details"/></td>
                                                        <td> ${pres.details} </td>
                                                    </tr>
                                                    <tr>
                                                        <td><fmt:message key="doctor"/></td>
                                                        <td> ${pres.doctorId} </td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                        </table>

                                    </div>
                                </div>
                                <div class="centerbar_base">
                                </div>
                            </c:forEach>
                        </form>
                        <c:if test="${(from + size) < amount}">
                            <a style="float: right; margin-top: 20px; margin-right: 70px;"
                               href="<c:url value="${requestScope['javax.servlet.forward.servlet_path']}">
                    <c:param name='from' value="${from + size}"/>
                    <c:param name='card_id' value="${param.card_id}"/>
                    <c:param name='name' value="${param.name}"/>
                       <c:param name="command" value="${param.command}"/> </c:url>">
                                <fmt:message key="next"/> </a>
                        </c:if>
                        <c:if test="${(from - size) > 0}">
                            <a style="float: left; margin-top: 20px;"
                               href="<c:url value="${requestScope['javax.servlet.forward.servlet_path']}">
                    <c:param name='from' value="${from - size}"/>
                    <c:param name='card_id' value="${param.card_id}"/>
                    <c:param name='name' value="${param.name}"/>
                       <c:param name="command" value="${param.command}"/> </c:url>">
                                <fmt:message key="previous"/> </a>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <div class="centerbar_top"></div>
                        <div class="centerbar">
                            <div class="centerbar_item">
                                <fmt:message key="${message}"/>
                            </div>
                        </div>
                        <div class="centerbar_base">
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>

