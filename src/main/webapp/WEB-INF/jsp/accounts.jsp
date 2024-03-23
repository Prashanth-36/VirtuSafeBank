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
  <nav>
    <ul class="top-nav">
      <li><img src="<%=request.getContextPath() %>/static/images/logo.png"
        alt="" id="logo" /></li>
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
    <form id="form" action="" method="get" class="search">
      <label for="branchId">Branch ID</label> <select name="branchId"
        id="branchId"
        onchange="document.getElementById('form').submit()">
        <option value="">select</option>
        <option value="1">1</option>
        <option value="2">2</option>
        <option value="3">3</option>
      </select> <img src="<%=request.getContextPath() %>/static/images/search.png"
        alt="" width="50rem" />
    </form>
    <table class="border-table">
      <tr style="position: relative">
        <th colspan="7"
          style="text-align: center; background-color: var(--blue); color: white;">
          Account Details
          <form action="/account" method="post">
            <button
              style="position: absolute; font-size: larger; right: 1rem; width: 2rem;">
              +</button>
          </form>
        </th>
      </tr>
      <tr>
        <th>Account Number</th>
        <th>Customer ID</th>
        <th>Current Balance</th>
        <th>Is Primary Account</th>
        <th>Account Open Date</th>
        <th>Branch ID</th>
        <th>Account Status</th>
      </tr>
      <%@page import="model.Account"%>
      <%@page import="java.util.Map"%>
      <% 
          Object acc=request.getAttribute("accounts");
          if(acc!=null){
          	Map<Integer,Account> accounts=(Map<Integer,Account>)acc;
          	for(Map.Entry<Integer,Account> e:accounts.entrySet()){
          		int id=e.getKey();
          		Account account=e.getValue();
          %>
      <tr onclick="window.location.href='?id=<%=id%>'">
        <td><%=id %></td>
        <td><%=account.getCustomerId() %></td>
        <td><%=account.getCurrentBalance() %></td>
        <td><%=account.isPrimaryAccout()?"YES":"NO"%></td>
        <td><%=account.getOpenDate() %></td>
        <td><%=account.getBranchId() %></td>
        <td><%=account.getStatus() %></td>
      </tr>
      <%
          	}
          }
          %>
    </table>
  </main>

  <%@ include file="pagination.jsp"%>
</body>
</html>
