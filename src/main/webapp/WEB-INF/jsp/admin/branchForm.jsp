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
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <%request.setAttribute("activePath", "branches");%>
  <%@include file="../addOns/adminHeader.jsp"%>
    <form action="<%=request.getContextPath()%>/controller/admin/addBranch" method="post" class="form-container">
      <h3>Create Branch</h3>
      <label for="location">Location *</label> 
      <input type="text" name="location" id="location" placeholder="Location" maxlength=50 required />
      <label for="city">City *</label> 
      <input type="text" name="city" id="city" placeholder="City" maxlength=30 required /> 
      <label for="city">State*</label> 
      <input type="text" name="state" id="state" placeholder="State" maxlength=30 required />
      <button>Create</button>
    </form>
</body>
</html>
