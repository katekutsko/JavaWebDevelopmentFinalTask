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
                        <h2 align="center"><fmt:message key="editing_case"/></h2>

                        <form method="POST" action="Hospital">
                            <input type="hidden" name="command" value="edit_case"/>
                            <input type="hidden" name="case_id" value="${current_case.id}"/>

                            <table>
                                <tr>
                                    <td>
                                        <fmt:message key="patient"/></td>
                                    <td>
                                        <input type="text" id="1" name="name" value="${patient_name}"
                                               readonly="readonly"
                                               style="width: 250px"/></td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="card_id"/></td>
                                    <td>
                                        <input id="2" type="text" name="card_id" value="${current_case.medicalCardId}"
                                               readonly="readonly"
                                               style="width: 50px"/></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="doctor"/></td>
                                    <td> ${user.name} ${user.surname} ${user.patronymic}
                                        <input id="3" type="hidden" name="doctor_id" value="${current_case.doctorId}">
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="admission_date"/></td>
                                    <td><input id="5" type="date" name="admission_date"
                                                value="${current_case.admissionDate}"
                                                style="width: 150px"/></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="complaints"/></td>
                                    <td><input id="4" type="text" name="complaints" value="${current_case.complaints}"
                                               style="width: 350px"
                                               maxlength="100"/></td>
                                </tr>
                                <c:if test="${current_case.active == 0}">
                                    <tr>
                                        <td><fmt:message key="discharge_date"/></td>
                                        <td><input id="6" type="date" name="discharge_date"
                                                   value="${current_case.dischargeDate}"
                                                   style="width: 150px"/></td>
                                    </tr>
                                    <tr>
                                        <td><fmt:message key="diagnosis"/></td>
                                        <td><ctg:diagnoses ordinal="${current_case.finalDiagnosis.ordinal}"/></td>
                                    </tr>
                                </c:if>
                            </table>

                            <button type="submit" name="command" value="edit_case">
                                <fmt:message
                                        key="save"/></button>
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
