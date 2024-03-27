<%@page import="model.Employee"%>
<%@page import="model.Customer"%>
<%@page import="java.util.Map"%>
<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="model.Branch"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="logicallayer.AdminHandler"%>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Home</title>
<link rel="stylesheet"
  href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
  <% request.setAttribute("activePath", "users"); %>
  <%@include file="../addOns/employeeHeader.jsp" %>
  <main class="main">
    <form id="form" action="" method="get" class="search">
    <label for="status">User Status:</label>
        <select name="status"
          class="selection"
          id="status"
          onchange="document.getElementById('form').submit()">
          <option value="1" <%= (1==(int)request.getAttribute("status"))?"selected":"" %>>ACTIVE</option>
          <option value="0" <%= (0==(int)request.getAttribute("status"))?"selected":"" %>>INACTIVE</option>
        </select> 
         
        <label for="userId">User ID:</label>
        <input
          type="search"
          id="userId"
          placeholder="User ID"
        />
        <img src="<%=request.getContextPath() %>/static/images/search.png" alt="" width="50rem" onclick="redirect()"/>
      </form>
      
    <table class="border-table">
      <tr style="position: relative">
        <th colspan="12"
          style="text-align: center; background-color: var(--blue); color: white;position:reletive;height:3rem">
          Customer Details
            <button onclick="window.location.href='<%=request.getContextPath() %>/controller/employee/addUser'"
              style="position: absolute; font-size: larger; right: 1rem;top:.7rem; height:2rem;width: 2rem;">
              +</button>
        </th>
      </tr>
      <tr>
        <th>User ID</th>
        <th>Name</th>
        <th>DOB</th>
        <th>Contact No</th>
        <th>Email</th>
        <th>Gender</th>
        <th>Location</th>
        <th>City</th>
        <th>State</th>
        <th>Aadhaar No</th>
        <th>PAN</th>
        <th>Status</th>
      </tr>
      <%
          Map<Integer,Customer> customers=(Map<Integer,Customer>) request.getAttribute("customers");
          for(Map.Entry<Integer,Customer> e:customers.entrySet()){
            int userId=e.getKey();
            Customer customer=e.getValue();
      %>
        <tr onclick="window.location.href='<%=request.getContextPath()%>/controller/employee/manageUser?userId=<%=userId%>'">
          <td><%=userId %></td>
          <td><%=customer.getName() %></td>
          <td><%=Utils.millisToLocalDate(customer.getDob(), ZoneId.systemDefault())  %></td>
          <td><%=customer.getNumber()%></td>
          <td><%=customer.getEmail()%></td>
          <td><%=customer.getGender()%></td>
          <td><%=customer.getLocation()%></td>
          <td><%=customer.getCity()%></td>
          <td><%=customer.getState()%></td>
          <td><%=customer.getAadhaarNo()%></td>
          <td><%=customer.getPanNo()%></td>
          <td><%=customer.getStatus()%></td>
        </tr>
      <%
          }
      %>
    </table>
  </main>
  <%@ include file="../addOns/pagination.jsp"%>
  
  <script>
  function redirect(){
  	window.location.href='<%=request.getContextPath()%>/controller/employee/user?userId='+document.getElementById('userId').value;
		  }
  </script>
</body>
</html>
