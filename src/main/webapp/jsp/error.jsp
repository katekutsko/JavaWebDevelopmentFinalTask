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
                        <p style="text-align: center; color: red">
                            <c:forEach items="${error}" var="item">
                        <p style="color: #F00; padding-right: 70px;"><fmt:message key="${item}"/></p>
                        </c:forEach>
                        <c:if test="${ not empty param.error}">
                            <fmt:message key="${param.error}"/>
                        </c:if>
                        <c:if test="${empty param.error && empty error}">
                            <fmt:message key="something_went_wrong"/>
                        </c:if>
                        </p>
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

