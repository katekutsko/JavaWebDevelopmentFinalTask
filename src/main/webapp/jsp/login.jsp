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
            <form action="Hospital" method="POST">
                <input type="hidden" name="command" value="login"/>
                <div align="center">
                    <fmt:message key="login"/> </br>
                    <input type="text" name="login" value="${param.login}"/></br>
                    <p style="color: #F00"> ${loginError} </p>
                    <fmt:message key="password"/> </br> <input type="password" name="password"/>
                    <p style="color: #F00"> ${passwordError} </p>

                    <button type="submit"/>  <fmt:message key="log_in"/> </button> </br> </br>

                    <p><a href="Hospital?command=register"><fmt:message key="not_registered"/></a></p></div>
            </form>
        </div>
    </div>

    <%@include file='jspf/footer.jspf' %>

</div>
</body>

</html>