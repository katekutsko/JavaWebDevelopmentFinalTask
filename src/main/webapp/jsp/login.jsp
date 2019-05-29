<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file='jspf/head.jspf' %>

<body>
<div id="main">

    <%@include file='jspf/header.jspf' %>


    <div id="content_header"></div>

    <div id="site_content">
        <div id="banner"></div>

        <%@include file='jspf/side_content.jspf' %>

        <div id="darkening">
            <div id="window">
                <p><fmt:message key="${blocked}"/></p>
                <a href="#" class="close">OK</a>
            </div>
        </div>

        <div id="content">
            <form action="Hospital" method="POST">
                <input type="hidden" name="command" value="login"/>
                <div align="center">
                    <fmt:message key="login"/> </br>
                    <input type="text" name="login" value="${param.login}"/></br></br>
                   <c:if test="${not empty loginError}"><p style="color: #F00"> <fmt:message key="${loginError}"/></p></c:if>
                    <fmt:message key="password"/> </br> <input type="password" name="password"/></br>
                    <c:if test="${not empty passwordError}"><p style="color: #F00"> <fmt:message key="${passwordError}"/></p></c:if>

                    <button type="submit">  <fmt:message key="log_in"/> </button> </br> </br>

                    <c:if test="${not empty error}"><p style="color: #F00"> <fmt:message key="${error}"/></p></c:if>

                    <p><a href="Hospital?command=register"><fmt:message key="not_registered"/></a></p></div>
            </form>
        </div>

    </div>

    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>