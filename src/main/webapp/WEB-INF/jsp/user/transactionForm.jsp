<%@page import="java.util.Map"%>
<%@page import="model.Account"%>
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
<% request.setAttribute("selected","moneyTransfer"); %>
  <%@ include file="../addOns/customerHeader.jsp"%>
  <%@include file="../addOns/customerSideNav.jsp"%>
  <main class="main">
    <form action="<%=request.getContextPath()%>/controller/user/<%=path%>"
      method="post" class="form-container">
      <label for="accountNo">Select Account Number <select
        name="accountNo" id="accountNo" class="account-selection" required>
           <option value="">Select</option>
          <% 
          Map<Integer,Account> accounts=(Map<Integer,Account>)request.getAttribute("accounts");
            for(int accountNo:accounts.keySet()){
          %>
            <option value="<%=accountNo %>"><%=accountNo %></option>
          <%
            }
          %>
      </select>
      </label> <input type="number" name="amount" id="amount" placeholder="Amount" />
      <input type="password" name="mpin" id="mpin" placeholder="MPIN" />
      <input type="text" name="description" id="description" placeholder="Description" />
      <button><%=path.equals("withdrawl") ? "Withdraw" : "Deposit"%></button>
    </form>
  </main>
</body>
</html>
