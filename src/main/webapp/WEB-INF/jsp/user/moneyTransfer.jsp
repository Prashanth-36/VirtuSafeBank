<%@page import="java.util.Map"%>
<%@page import="model.Account"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Money Transfer</title>
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
    <%@ include file="../addOns/customerSideNav.jsp"%>
    <div class="main-container">
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
        style="display: none;margin:3rem;margin-bottom:0;">
        <label for="senderAccountNo">Select From Account Number</label>
          <select name="senderAccountNo" id="senderAccountNo" required>
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
        <label for="beneficiaryAccNo">Beneficiary Account No</label>
        <input type="number" name="beneficiaryAccNo"
          id="beneficiaryAccNo" placeholder="Beneficiary Account No" required/>
        <label id="ifsc" for="beneficiaryIfsc">Beneficiary IFSC Code</label>
        <input type="text" name="beneficiaryIfsc" id="beneficiaryIfsc"
          placeholder="Beneficiary IFSC code" /> 
        <label for="amount">Amount</label>
          <input type="number" name="amount" min="0.01" id="amount" placeholder="Amount" step="0.01" required/> 
        <label for="description">Description</label>
          <input type="text"name="description" id="description" maxlength="50" placeholder="Description" /> 
        <label for="mpin">MPIN</label>
          <input type="password" name="mpin" id="mpin" placeholder="MPIN" required/>
        <button>Send</button>
      </form>
      </div>
    </main>
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
          beneficiaryIfsc=document.getElementById("beneficiaryIfsc")
          beneficiaryIfsc.style.display = "none";
          beneficiaryIfsc.removeAttribute("required");
          beneficiaryIfscLabel=document.getElementById("ifsc")
          beneficiaryIfscLabel.style.display = "none";
        }
      });
      otherBank.addEventListener("click", () => {
        const classes = otherBank.classList;
        if (!classes.contains("selected")) {
          otherBank.classList.add("selected");
          withinBank.classList.remove("selected");
          form.style.display = "flex";
          const beneficiaryIfsc=document.getElementById("beneficiaryIfsc")
          beneficiaryIfsc.style.display = "block";
          document.getElementById("ifsc").style.display="block";
          beneficiaryIfsc.setAttribute("required","required");
        }
      });
    </script>
</body>
</html>
