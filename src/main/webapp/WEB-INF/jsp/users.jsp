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
  <%@include file="adminHeader.jsp" %>
  <main class="main">
    <form id="form" action="" method="get" class="search">

      <label for="userType">User Type:</label>
      <select name="userType"
      class="selection"
        id="userType"
        onchange="document.getElementById('form').submit()">
        <% 
          int userType=(int)request.getAttribute("userType");
        %>
        <option value="2" <%= (2==userType)?"selected":"" %>>Employee</option>
        <option value="0" <%= (0==userType)?"selected":"" %>>Customer</option>
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
          Account Details
            <button onclick="window.location.href='<%=request.getContextPath() %>/controller/addAccount'"
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
        <% 
            if(userType==0){ 
        %>
            <th>Aadhaar No</th>
            <th>PAN</th>
        <%
          }else{
        %>
            <th>User Type</th>
            <th>Branch Id</th>
        <%
          }
        %>
            <th>Status</th>
      </tr>
      <%
            if(userType==0){
              Map<Integer,Customer> customers=(Map<Integer,Customer>) request.getAttribute("customers");
              for(Map.Entry<Integer,Customer> e:customers.entrySet()){
                int userId=e.getKey();
                Customer customer=e.getValue();
      %>
        <tr onclick="window.location.href='<%=request.getContextPath()%>/controller/manageUser?userId=<%=userId%>'">
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
        }else{
        	Map<Integer,Employee> employees=(Map<Integer,Employee>) request.getAttribute("employees");
            for(Map.Entry<Integer,Employee> e:employees.entrySet()){
              int userId=e.getKey();
              Employee employee=e.getValue();
      %>
      <tr onclick="window.location.href='<%=request.getContextPath()%>/controller/manageUser?userId=<%=userId%>'">
        <td><%=userId %></td>
          <td><%=employee.getName() %></td>
          <td><%=Utils.millisToLocalDate( employee.getDob(), ZoneId.systemDefault()) %></td>
          <td><%=employee.getNumber()%></td>
          <td><%=employee.getEmail()%></td>
          <td><%=employee.getGender()%></td>
          <td><%=employee.getType()%></td>
          <td><%=employee.getLocation()%></td>
          <td><%=employee.getCity()%></td>
          <td><%=employee.getState()%></td>
          <td><%=employee.getBranchId()%></td>
          <td><%=employee.getStatus()%></td>
      </tr>
      <%
          }
        }
      %>
    </table>
  </main>
  <%@ include file="pagination.jsp"%>
  
  <script>
  function redirect(){
  	window.location.href='<%=request.getContextPath()%>/controller/user?userId='+document.getElementById('userId').value;
		  }
  </script>
</body>
</html>
