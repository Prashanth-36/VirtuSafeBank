<%@page import="utility.ActiveStatus"%>
<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="model.Account"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Home</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
  <nav>
    <ul class="top-nav">
      <li><img src="<%=request.getContextPath() %>/static/images/logo.png" alt="" id="logo" /></li>
      <li><a href="accounts.html" class="active">Accounts
          Management</a></li>
      <li><a href="branches.html">Branch Management</a></li>
      <li><a href="">Customer Management</a></li>
      <li><a href="">Employee Management</a></li>
      <li><a href="">Fund Transfer</a></li>
    </ul>
    <a href="" class="top-nav" id="logout">Logout</a>
  </nav>

  <main class="main">
    <table class="table" style="margin-top: 5rem">
      <tr>
        <th colspan="2"
          style="text-align: center; background-color: var(--blue); color: white;">
          Account Details</th>
      </tr>
      <%
        Account account=(Account)request.getAttribute("account");
      %>
      <tr>
        <th>Account Number</th>
        <td><%=account.getAccountNo() %></td>
      </tr>
      <tr>
        <th>Customer ID</th>
        <td><%=account.getCustomerId() %></td>
      </tr>
      <tr>
        <th>Current Balance</th>
        <td><%=account.getCurrentBalance() %></td>
      </tr>
      <tr>
        <th>Is Primary Accout</th>
        <td><%=account.isPrimaryAccout()?"YES":"NO" %></td>
      </tr>
      <tr>
        <th>Account Open Date</th>
        <td><%=Utils.millisToLocalDate(account.getOpenDate(), ZoneId.systemDefault()) %></td>
      </tr>
      <tr>
        <th>Branch ID</th>
        <td><%=account.getBranchId() %></td>
      </tr>
      <tr>
        <th>Account Status</th>
        <td><%=account.getStatus() %></td>
      </tr>
    </table>
    <% if(account.getStatus()==ActiveStatus.INACTIVE){ %>
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
        <%}else{ %>
      <button
        style="font-size: large; padding: 0.5rem; margin: auto; display: block; background-color: #e34234; color: white;">
        Deactivate</button>
     <%} %>
  </main>
</body>
</html>
