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
       <div class="centerbar_top"></div>
        <div class="centerbar">
         <div class="centerbar_item">
            <h2 align="center"><fmt:message key="adding_case"/></h2>
             <form name="add" method="POST" action="Hospital">
			   <input type="hidden" name="command" value="add_record"/>
				<table> <td valign="top"> 
				<p><fmt:message key="patient"/></p>
				<p><fmt:message key="card_id"/></p>
				<p><fmt:message key="doctor"/></p>
					<fmt:message key="complaints"/> </br></br></br>
					<fmt:message key="admission_date"/>
				</td> 
				<td> <input type="text" value="${param.name}" readonly="readonly" style="width: 250px"/> </br>
				<input type="text" name="card_id" value="${param.card_id}" readonly="readonly" style="width: 50px"/></br>
				<input type="hidden" name="doctor_id" value="${user.id}"/></br>
				 ${user.name} ${user.surname} ${user.patronymic} </br></br>
				 <input type="text" name="complaints" style="width: 350px" rows="3" maxlength="135".></br></br>
				 <input type="date" name="admission_date" value="${admission_date}" readonly="readonly" style="width: 150px"/></br>
				<input type="submit" value="Submit" style="margin-left: 100px"/>
				</td>
			   </table>
			   
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
