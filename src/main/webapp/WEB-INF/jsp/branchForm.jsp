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
  %>
  <%@include file="adminHeader.jsp"%>
  <main class="main">
    <form action="<%=request.getContextPath()%>/controller/addBranch"
      method="post" class="form-container">
      <h3>Create Branch</h3>
      <!-- <h3>Edit Branch</h3> -->
      <!-- <label for="ifsc">IFSC *</label> <input type="text" name="ifsc"
        id="ifsc" placeholder="IFSC" required /> -->
      <label for="location">location *</label> <input type="text"
        name="location" id="location" placeholder="Location" required />
      <label for="city">City *</label> <input type="text" name="city"
        id="city" placeholder="City" required /> <label for="city">State
        *</label> <input type="text" name="state" id="state" placeholder="State"
        required />
      <button>Create</button>
      <!-- <button>Update</button> -->
    </form>
  </main>
</body>
</html>
