<nav>
  <ul class="top-nav">
    <li><img
      src="<%=request.getContextPath()%>/static/images/logo.png" alt=""
      id="logo" /></li>
    <li><a href="<%=request.getContextPath()%>/controller/home"
      class=<%=request.getRequestURI().contains("customer.jsp") ? "active" : ""%>>My Accounts</a></li>
    <li><a
      href="<%=request.getContextPath()%>/controller/moneyTransfer"
      class=<%=request.getRequestURI().contains("moneyTransfer.jsp") ? "active" : ""%>>Money
        Transfer</a></li>
  </ul>
  <img
    src="<%=request.getContextPath()%>/static/images/profile-user.png"
    alt="" style="width: 2.6rem" /> <a
    href="<%=request.getContextPath()%>/" class="top-nav" id="logout">Logout</a>
</nav>
