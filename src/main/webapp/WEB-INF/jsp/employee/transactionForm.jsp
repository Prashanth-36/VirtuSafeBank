<%@page import="java.util.Set"%>
<%@page import="model.Account"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Fund Transfer</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
    <% request.setAttribute("activePath", "fundTransfer"); %>
    <%@include file="../addOns/employeeHeader.jsp" %>
    <form action="<%=request.getContextPath()%>/controller/employee/fundTransfer"
      method="post" class="form-container">
      <h3>Deposit Money</h3>
      <label for="accountNo">Account Number </label> 
      <input type="number" name="accountNo" placeholder="Account Number" list="accounts" required/>
      <datalist id="accounts">
          <% 
          Set<Integer> accounts=(Set<Integer>)request.getAttribute("accounts");
            for(int accountNo:accounts){
          %>
            <option value="<%=accountNo %>">
          <%
            }
          %>
      </datalist>
      <label for="amount">Amount</label> 
      <input type="number" name="amount" min="0.01" step="0.01" id="amount" placeholder="Amount" required/>
      <label for="description">Description</label> 
      <input type="text" name="description" maxlength="50" id="description" placeholder="Description" />
      <button>Deposit</button>
    </form>
</body>
</html>