<%@page import="java.util.Map"%>
<%@page import="model.Account"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title><%=request.getAttribute("path").equals("withdrawl") ? "Withdrawl" : "Deposit"%></title>
<link rel="stylesheet"
  href="<%=request.getContextPath()%>/static/css/home.css" />
</head>
<body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
<% request.setAttribute("activePath","moneyTransfer"); %>
  <%@ include file="../addOns/customerHeader.jsp"%>
  <main class="main">
  <%@include file="../addOns/customerSideNav.jsp"%>
    <form action="<%=request.getContextPath()%>/controller/user/<%=path%>"
      method="post" class="form-container" style="height:fit-content;">
      <label for="accountNo">Select Account Number</label> 
      <select name="accountNo" id="accountNo" required>
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
      <label for="amount">Amount</label>
      <input type="number" name="amount" min="0.01" id="amount" step="0.01" placeholder="Amount" required/>
      <label for="mpin">MPIN</label>
      <input type="password" name="mpin" id="mpin" placeholder="MPIN" required/>
      <label for="description">Description</label>
      <input type="text" name="description" maxlength="50" id="description" placeholder="Description" />
      <button><%=path.equals("withdrawl") ? "Withdraw" : "Deposit"%></button>
    </form>
  </main>
</body>
</html>
