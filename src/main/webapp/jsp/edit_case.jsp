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
                        <h2 align="center"><fmt:message key="edit"/></h2>
                        <form method="POST" action="Hospital">
                            <input type="hidden" name="command" value="edit_case"/>
                            <input type="hidden" name="case_id" value="${case_id}"/>
                            <label for="1"><fmt:message key="patient"/>
                                <input type="text" id="1" name="name" value="${patient_name}" readonly="readonly"
                                       style="width: 250px"/>
                            </label></br>
                            <label for="2"><fmt:message key="card_id"/>
                                <input id="2" type="text" name="card_id" value="${card_id}" readonly="readonly"
                                       style="width: 50px"/></br>
                            </label></br>
                            <label for="3"><fmt:message key="doctor"/>
                                <input id="3" type="text" name="doctor_id" value="${user.id}" readonly="readonly"
                                       style="width: 50px"/></label>
                            ${user.name} ${user.surname} ${user.patronymic} </br> </br>
                            <label for="5"><fmt:message key="admission_date"/>
                                <input id="5" type="date" name="admission_date" value="${admission_date}"
                                       style="width: 150px"/>
                            </label> </br> </br>

                            <label for="6"><fmt:message key="discharge_date"/>
                                <input id="6" type="date" name="discharge_date" value="${dischargement_date}"
                                       style="width: 150px"/></label></br></br>
                            <label for="4"><fmt:message key="complaints"/>
                                <input id="4" type="text" name="complaints" value="${complaints}" style="width: 350px" maxlength="100"/>
                            </label></br>
                            <label><fmt:message key="diagnosis"/>
                                <ctg:diagnoses/>
                            </label>

                            <input type="submit" value="Submit" style="margin-left: 100px"/>
                            </td>
                            </table>

                        </form>

                    </div>
                </div>
                <div class="centerbar_base" style="margin-top: 50px;"></div>
            </div>
        </div>
    </div>

    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>
