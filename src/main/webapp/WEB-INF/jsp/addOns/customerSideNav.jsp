<%
String path = (String) request.getAttribute("path");
%>
<aside>
  <ul class="side-nav">
    <li><a
      href="<%=request.getContextPath()%>/controller/user/moneyTransfer"
      class=<%=request.getRequestURI().contains("moneyTransfer.jsp") ? "active" : ""%>>Money
        Transfer</a></li>
    <li><a
      href="<%=request.getContextPath()%>/controller/user/withdrawl"
      class=<%=path != null && path.equals("withdrawl") ? "active" : ""%>>Withdrawl</a></li>
    <li><a href="<%=request.getContextPath()%>/controller/user/deposit"
      class=<%=path != null && path.equals("deposit") ? "active" : ""%>>Deposit</a></li>
  </ul>
</aside>