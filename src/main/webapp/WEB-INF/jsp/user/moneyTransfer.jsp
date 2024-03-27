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
<% request.setAttribute("selected","moneyTransfer"); %>
  <%@ include file="../addOns/customerHeader.jsp"%>
  <%@ include file="../addOns/customerSideNav.jsp"%>
  <div class="main-container">
    <main class="main">
      <div class="transfer-options">
        <div class="card" id="withinBank">
          <img
            src="<%=request.getContextPath()%>/static/images/banking.png"
            width="100rem" alt="" />
          <p>Within Bank Transaction</p>
        </div>
        <div class="card" id="otherBank">
          <img
            src="<%=request.getContextPath()%>/static/images/bank-to-bank.png"
            alt="" width="100rem" />
          <p>Other Bank Transaction</p>
        </div>
      </div>
      <form id="form"
        action="<%=request.getContextPath()%>/controller/user/moneyTransfer"
        method="post" class="form-container"
        style="display: none; height: fit-content">
        <label for="senderAccountNo">Select From Account Number
          <select name="senderAccountNo" id="senderAccountNo"
          class="account-selection" required>
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
        </label> <input type="text" name="beneficiaryAccNo"
          id="beneficiaryAccNo" placeholder="Beneficiary Account No" />
        <input type="text" name="beneficiaryIfsc" id="beneficiaryIfsc"
          placeholder="Beneficiary IFSC code" /> 
          <input type="text"
          name="amount" id="amount" placeholder="Amount" /> 
          <input type="text"
          name="description" id="description" placeholder="Description" /> 
          <input
          type="password" name="mpin" id="mpin" placeholder="MPIN" />
        <button>Send</button>
      </form>
    </main>
  </div>
  <script>
      const form = document.getElementById("form");
      const withinBank = document.getElementById("withinBank");
      const otherBank = document.getElementById("otherBank");
      withinBank.addEventListener("click", () => {
        const classes = withinBank.classList;
        if (!classes.contains("selected")) {
          withinBank.classList.add("selected");
          otherBank.classList.remove("selected");
          form.style.display = "flex";
          document.getElementById("beneficiaryIfsc").style.display = "none";
        }
      });
      otherBank.addEventListener("click", () => {
        const classes = otherBank.classList;
        if (!classes.contains("selected")) {
          otherBank.classList.add("selected");
          withinBank.classList.remove("selected");
          form.style.display = "flex";
          document.getElementById("beneficiaryIfsc").style.display = "block";
        }
      });
    </script>
</body>
</html>
