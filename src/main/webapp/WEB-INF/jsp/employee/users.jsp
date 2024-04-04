<%@page import="utility.UserType"%>
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
<title>Users</title>
<link rel="stylesheet"
  href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <% request.setAttribute("activePath", "users"); %>
  <% 
    UserType userType=(UserType) session.getAttribute("userType");
    int selectedUserType=(int) request.getAttribute("userType");
  %>
  
  <% if(userType==UserType.ADMIN){ %>
    <%@include file="../addOns/adminHeader.jsp" %>
  <%}else{ %>
    <%@include file="../addOns/employeeHeader.jsp" %>
  <%}%>
    
    <div class="search">
      <form id="form" action="" method="get">
        <% if(userType==UserType.ADMIN){ %>
          <label for="userType">User Type:</label>
          <select name="userType"
          class="selection"
            id="userType"
            onchange="document.getElementById('form').submit()">
            <option value="2" <%= (2==selectedUserType)?"selected":"" %>>Employee</option>
            <option value="0" <%= (0==selectedUserType)?"selected":"" %>>Customer</option>
          </select> 
        <%}else{ %>
            <label for="status">Customer Status:</label>
            <select name="status"
              class="selection"
              id="status"
              onchange="document.getElementById('form').submit()">
              <option value="1" <%= (1==(int)request.getAttribute("status"))?"selected":"" %>>ACTIVE</option>
              <option value="0" <%= (0==(int)request.getAttribute("status"))?"selected":"" %>>INACTIVE</option>
            </select> 
        <%}%>
      </form>
      <form action="<%=request.getContextPath()%>/controller/<%=userType.name().toLowerCase() %>/modifyUser" method="get">
        <label for="userId"><%=userType==UserType.ADMIN?"User ID:":"Customer ID:" %></label>
        <input type="hidden" name="userType" value="<%=selectedUserType%>"/>
        <input
          type="number"
          id="userId"
          name="userId"
          placeholder="User ID"
        />
        <button class="btn-search"></button>
      </form>
    </div>
    <table class="border-table">
      <tr style="position: relative">
        <th colspan="12"
          style="text-align: center; background-color: var(--blue); color: white;position:reletive;height:3rem">
         <%=userType==UserType.ADMIN && selectedUserType!=0?"Employees":"Customers" %>
            <button onclick="window.location.href='<%=request.getContextPath() %>/controller/<%=userType.name().toLowerCase()%>/addUser'"
              class="add-button"><img src="<%=request.getContextPath() %>/static/images/add-contact.png" style="width:2rem"> Add <%=userType==UserType.ADMIN?"User":"Customer" %></button>
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
            if(selectedUserType==0){ 
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
            if(selectedUserType==0){
              Map<Integer,Customer> customers=(Map<Integer,Customer>) request.getAttribute("customers");
              for(Map.Entry<Integer,Customer> e:customers.entrySet()){
                int userId=e.getKey();
                Customer customer=e.getValue();
      %>
        <tr onclick="window.location.href='<%=request.getContextPath()%>/controller/<%=userType.name().toLowerCase() %>/modifyUser?userId=<%=userId%>&userType=<%=customer.getType().ordinal()%>'">
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
      <tr onclick="window.location.href='<%=request.getContextPath()%>/controller/<%=userType.name().toLowerCase() %>/modifyUser?userId=<%=userId%>&userType=<%=employee.getType().ordinal()%>'">
        <td><%=userId %></td>
          <td><%=employee.getName() %></td>
          <td><%=Utils.millisToLocalDate( employee.getDob(), ZoneId.systemDefault()) %></td>
          <td><%=employee.getNumber()%></td>
          <td><%=employee.getEmail()%></td>
          <td><%=employee.getGender()%></td>
          <td><%=employee.getLocation()%></td>
          <td><%=employee.getCity()%></td>
          <td><%=employee.getState()%></td>
          <td><%=employee.getType()%></td>
          <td><%=employee.getBranchId()%></td>
          <td><%=employee.getStatus()%></td>
      </tr>
      <%
          }
        }
      %>
    </table>
  <%@ include file="../addOns/pagination.jsp"%>
  
</body>
</html>
