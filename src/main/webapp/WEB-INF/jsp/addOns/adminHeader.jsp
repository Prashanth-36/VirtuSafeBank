<nav>
<% String activePath=(String)request.getAttribute("activePath"); %>
  <ul class="top-nav">
    <li><img src="<%=request.getContextPath() %>/static/images/logo.png"
      alt="VirtuSafeBank" id="logo" /></li>
    <li><a href="<%=request.getContextPath() %>/controller/admin/home" class=<%=activePath.equals("accounts")?"active":"" %>>Accounts</a></li>
    <li><a href="<%=request.getContextPath() %>/controller/admin/branches" class=<%=activePath.equals("branches")?"active":"" %>>Branches</a></li>
    <li><a href="<%=request.getContextPath() %>/controller/admin/users" class=<%=activePath.equals("users")?"active":"" %>>User</a></li>
  </ul>
  <div id="userName"><%=session.getAttribute("userName") %></div>
  <img src="<%=request.getContextPath()%>/static/images/profile-user.png" onclick="window.location.href='<%=request.getContextPath()%>/controller/admin/profile'"
  alt="profile" style="width: 2.6rem" /> 
  <a href="<%=request.getContextPath()%>/controller/logout" class="top-nav" id="logout">Logout</a>
</nav>