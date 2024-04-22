<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="model.Token"%>
<%@page import="utility.UserType"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Accounts</title>
<link rel="stylesheet"
  href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <% request.setAttribute("activePath", "api"); %>
  <% UserType userType=(UserType) session.getAttribute("userType"); %>
  
  <% if(userType==UserType.ADMIN){ %>
    <%@include file="../addOns/adminHeader.jsp" %>
  <% }else{ %>
    <%@include file="../addOns/employeeHeader.jsp" %>
  <% } %>
  
  <form method="post" class="token-form" action="<%=request.getContextPath() %>/controller/<%=userType.name().toLowerCase() %>/api" >
  <h3 style="text-align:center">Generate API Token</h3>
  <div>
    <input type="radio" name="accessLevel" id="readOnly" value="0" required/> 
    <label for="readOnly">ReadOnly</label>
  </div>
  <div>
    <input type="radio" name="accessLevel" id="write" value="1" required/> 
    <label for="write">Write</label>
  </div>
      <button class="btn">Generate</button>
  </form>
  
  <%
    Token token=(Token)request.getAttribute("token");
    if(token !=null){
  %>
  
  <table class="border-table">
      <tr style="position: relative">
        <th colspan="4" style="text-align: center; background-color: var(--blue); color: white;position:reletive;height:3rem"> Available Token </th>
      </tr>
      <tr>
        <th>Token</th>
        <th>Access Level</th>
        <th>Valid Till</th>
      </tr>
      <tr>
        <td><%=token.getToken() %></td>
        <% int accressLevel=token.getAccessLevel(); %>
        <td><%=accressLevel==0?"ReadOnly":"Write" %></td>
        <td><%=Utils.formatLocalDateTime(Utils.millisToLocalDateTime(token.getValidTill() , ZoneId.systemDefault())) %></td>
      </tr>
    </table>
  <%
     }
  %>
  
</body>
</html>
