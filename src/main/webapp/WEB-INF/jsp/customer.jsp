<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="logicallayer.CustomerHandler"%>
<%@page import="model.User"%>
<%@page import="model.Account"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Home</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/home.css" />
</head>
<body>
  <%@ include file="customerHeader.jsp"%>
  <main class="main">
    <div class="totalBalance">
      Total Available Balance: ₹<span> <%=request.getAttribute("totalBalance") %></span>
    </div>
    <table class="border-table">
      <tr>
        <th colspan="7"
          style="text-align: center; background-color: var(--blue); color: white;">
          Accounts</th>
      </tr>
      <tr>
        <th>Account Number</th>
        <th>Current Balance</th>
        <th>Is Primary Account</th>
        <th>Account Open Date</th>
        <th>Branch ID</th>
        <th>Account Status</th>
      </tr>

      <%
      Map<Integer, Account> accounts = (Map<Integer, Account>) request.getAttribute("accounts");
      for (Map.Entry<Integer, Account> e : accounts.entrySet()) {
      	int id = e.getKey();
      	Account account = e.getValue();
      %>
      <tr
        onclick="window.location.href='<%=request.getContextPath()%>/controller/account?accountNo=<%=id%>'">
        <td><%=id%></td>
        <td><%=account.getCurrentBalance()%></td>
        <td><%=account.isPrimaryAccout() ? "YES" : "NO"%></td>
        <td><%=account.getOpenDate()%></td>
        <td><%=account.getBranchId()%></td>
        <td><%=account.getStatus()%></td>
      </tr>
      <%
      }
      %>
    </table>
  </main>
</body>
</html>
