<%@page import="model.Employee"%>
<%@page import="utility.UserType"%>
<%@page import="model.User"%>
<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="model.Customer"%>
<%@page import="utility.ActiveStatus"%>
<%@page import="model.Branch"%>
<%@ page language="java" contentType="text/html;
charset=UTF-8"
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
  request.setAttribute("activePath", "users");
  %>
  <%@include file="../addOns/adminHeader.jsp"%>
  <main class="main">
      <%
        int type = (int) request.getAttribute("userType");
      	User user = (User) request.getAttribute("user");
      %>
    <table class="table" style="margin-top: 5rem;width:50%">
      <tr style="position: relative">
        <th colspan="2"
          style="text-align: center; background-color: var(--blue); color: white;">
          User Details <img
          src="<%=request.getContextPath()%>/static/images/edit-white.png"
          onclick="window.location.href='<%=request.getContextPath()%>/controller/admin/modifyUser?userId=<%=user.getUserId()%>&userType=<%=user.getType() %>'" alt="edit"
          style="position: absolute; font-size: larger; right: 1rem; width: 2rem;" />
        </th>
      </tr>
      <tr>
        <th>User ID</th>
        <td><%=user.getUserId()%></td>
      </tr>
      <tr>
        <th>Name</th>
        <td><%=user.getName()%></td>
      </tr>
      <tr>
        <th>DOB</th>
        <td><%=Utils.millisToLocalDate(user.getDob(), ZoneId.systemDefault())%>
        </td>
      </tr>
      <tr>
        <th>Contact No</th>
        <td><%=user.getNumber()%></td>
      </tr>
      <tr>
        <th>Email</th>
        <td><%=user.getEmail()%></td>
      </tr>
      <tr>
        <th>Gender</th>
        <td><%=user.getGender()%></td>
      </tr>
      <tr>
        <th>Location</th>
        <td><%=user.getLocation()%></td>
      </tr>
      <tr>
        <th>City</th>
        <td><%=user.getCity()%></td>
      </tr>
      <tr>
        <th>State</th>
        <td><%=user.getState()%></td>
      </tr>
      <% 
        if(type==0){
          Customer customer=(Customer) user;
      %>
      <tr>
        <th>Aadhaar No</th>
        <td><%=customer.getAadhaarNo()%></td>
      </tr>
      <tr>
        <th>PAN</th>
        <td><%=customer.getPanNo()%></td>
      </tr>
      <%}
        else{
    	   Employee employee=(Employee) user;  
        %>
      <tr>
        <th>User Type</th>
        <td><%=employee.getType()%></td>
      </tr>
      <tr>
        <th>Branch ID</th>
        <td><%=employee.getBranchId()%></td>
      </tr>
      <% } %>
      <tr>
        <th>Status</th>
        <td><%=user.getStatus()%></td>
      </tr>
    </table>
    
       <% if(user.getStatus()==ActiveStatus.INACTIVE){ %>
    <form method="post" action="<%=request.getContextPath() %>/controller/admin/manageUser">
      <input type="hidden" name="activate" value="1" />
      <input type="hidden" name="userId" value="<%=user.getUserId() %>" />
      <input type="hidden" name="userType" value="<%=user.getType() %>" />
      <button
            style="
              font-size: large;
              padding: 0.5rem;
              margin: auto;
              display: block;
              background-color: #009e60;
              color: white;
            "
          >
            Activate
          </button> 
    </form>
        <%}else{ %>
      <form method="post" action="<%=request.getContextPath() %>/controller/admin/manageUser">
      <input type="hidden" name="deactivate" value="1" />
      <input type="hidden" name="userId" value="<%=user.getUserId() %>" />
      <input type="hidden" name="userType" value="<%=user.getType() %>" />
      <button
        style="font-size: large; padding: 0.5rem; margin: auto; display: block; background-color: #e34234; color: white;">
        Deactivate</button>
      </form>
     <%} %>
  </main>
</body>
</html>
