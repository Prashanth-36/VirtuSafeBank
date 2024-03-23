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
  <%@ include file="customerHeader.jsp"%>
  <%@include file="customerSideNav.jsp"%>
  <main class="main">
    <form action="<%=request.getContextPath()%>/controller/<%=path%>"
      method="post" class="form-container">
      <label for="accountNo">Select Account Number <select
        name="accountNo" id="accountNo" class="account-selection">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
      </select>
      </label> <input type="text" name="amount" id="amount" placeholder="Amount" />
      <input type="password" name="mpin" id="mpin" placeholder="MPIN" />
      <button><%=path.equals("withdrawl") ? "Withdraw" : "Deposit"%></button>
    </form>
  </main>
</body>
</html>
