<nav>
<% String activePath=(String)request.getAttribute("activePath"); %>
  <ul class="top-nav">
    <li><img src="<%=request.getContextPath() %>/static/images/logo.png"
      alt="VirtuSafeBank" id="logo" /></li>
    <li><a href="<%=request.getContextPath() %>/controller/employee/home" class=<%=activePath.equals("accounts")?"active":"" %>>Accounts</a></li>
    <li><a href="<%=request.getContextPath() %>/controller/employee/users" class=<%=activePath.equals("users")?"active":"" %>>Customers</a></li>
    <li><a href="<%=request.getContextPath() %>/controller/employee/fundTransfer" class=<%=activePath.equals("fundTransfer")?"active":"" %>>Fund Transfer</a></li>
  </ul>
  <div id="userName"><%=session.getAttribute("userName") %></div>
  <img src="<%=request.getContextPath()%>/static/images/profile-user.png" onclick="window.location.href='<%=request.getContextPath()%>/controller/employee/profile'"
  alt="profile" style="width: 2.6rem" /> 
  <a href="<%=request.getContextPath()%>/controller/logout" class="top-nav" id="logout">Logout</a>
</nav>