<nav>
<% String activePath=(String)request.getAttribute("activePath"); %>
  <ul class="top-nav">
    <li><img src="<%=request.getContextPath() %>/static/images/logo.png"
      alt="VirtuSafeBank" id="logo" /></li>
    <li><a href="<%=request.getContextPath() %>/controller/home" class=<%=activePath.equals("accounts")?"active":"" %>>Accounts</a></li>
    <li><a href="<%=request.getContextPath() %>/controller/users" class=<%=activePath.equals("users")?"active":"" %>>Customers</a></li>
    <li><a href="<%=request.getContextPath() %>/controller/fundTransfer" class=<%=activePath.equals("fundTransfer")?"active":"" %>>Fund Transfer</a></li>
  </ul>
  <a href="<%=request.getContextPath()%>/controller/logout" class="top-nav" id="logout">Logout</a>
</nav>