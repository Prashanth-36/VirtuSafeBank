<%@page import="model.Branch"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Home</title>
<link rel="stylesheet"
  href="<%=request.getContextPath()%>/static/css/home.css" />
</head>
<body>
  <%
  request.setAttribute("activePath", "branches");
  Object branchObj=request.getAttribute("branch");
  Branch branch=branchObj==null?null:(Branch)branchObj;
  %>
  <%@include file="../addOns/adminHeader.jsp"%>
  <main class="main">
    <form action="<%=request.getContextPath()%>/controller/admin/addBranch"
      method="post" class="form-container">
      <h3><%=branch==null?"Create Branch":"Edit Branch" %></h3>
      <%if(branch!=null){%>
        <label for="ifsc">IFSC *</label> <input type="text" name="ifsc"
        id="ifsc" placeholder="IFSC" value="<%=branch!=null?branch.getIfsc():"" %>" required />
        <%} %>
      <label for="location">Location *</label> <input type="text"
        name="location" id="location" placeholder="Location" value="<%=branch!=null?branch.getLocation():"" %>" required />
      <label for="city">City *</label> <input type="text" name="city"
        id="city" placeholder="City" value="<%=branch!=null?branch.getCity():"" %>" required /> <label for="city">State
        *</label> <input type="text" name="state" id="state" placeholder="State"
       value="<%=branch!=null?branch.getState():"" %>" required />
      <button><%=branch==null?"Create":"Update" %></button>
    </form>
  </main>
</body>
</html>
