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
                        <p style="color: #F00; padding-right: 70px;"><fmt:message key="${error}"/></p>
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

