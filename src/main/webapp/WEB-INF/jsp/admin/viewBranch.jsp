<%@page import="utility.ActiveStatus"%>
<%@page import="model.Branch"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Branch Details</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <% request.setAttribute("activePath", "branches"); %>
  <%@include file="../addOns/adminHeader.jsp" %>
  <main class="main">
    <table class="table" style="margin-top:5rem;">
      <tr style="position: relative">
        <th colspan="2"
          style="text-align: center; background-color: var(--blue); color: white;">
          Branch Details 
        </th>
      </tr>
      <% 
        Branch branch=(Branch)request.getAttribute("branch");
        if(branch!=null){
      %>
      <tr>
        <th>Branch ID</th>
        <td><%=branch.getId() %></td>
      </tr>
      <tr>
        <th>IFSC</th>
        <td><%=branch.getIfsc() %></td>
      </tr>
      <tr>
        <th>Location</th>
        <td><%=branch.getLocation() %></td>
      </tr>
      <tr>
        <th>City</th>
        <td><%=branch.getCity() %></td>
      </tr>
      <tr>
        <th>State</th>
        <td><%=branch.getState() %></td>
      </tr>
      <tr>
        <th>Status</th>
        <td><%=branch.getStatus() %></td>
      </tr>
    </table>
       <% if(branch.getStatus()==ActiveStatus.ACTIVE){ %>
      <form method="post" action="<%=request.getContextPath() %>/controller/admin/branch">
      <input type="hidden" name="activate" value="0" />
      <input type="hidden" name="branchId" value="<%=branch.getId() %>" />
      <button
        style="font-size: large; padding: 0.5rem; margin: auto; display: block; background-color: #e34234; color: white;">
        Remove Branch</button>
      </form>
      <%
          }
        }
      %>
  </main>
</body>
</html>
