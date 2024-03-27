<% String selected=(String)request.getAttribute("selected"); %>
<nav>
  <ul class="top-nav">
    <li><img
      src="<%=request.getContextPath()%>/static/images/logo.png" alt=""
      id="logo" /></li>
    <li><a href="<%=request.getContextPath()%>/controller/user/home"
      class=<%=selected.equals("myAccounts") ? "active" : ""%>>My Accounts</a></li>
    <li><a
      href="<%=request.getContextPath()%>/controller/user/moneyTransfer"
      class=<%=selected.equals("moneyTransfer") ? "active" : ""%>>Money
        Transfer</a></li>
  </ul>
  <img
    src="<%=request.getContextPath()%>/static/images/profile-user.png"
    alt="" style="width: 2.6rem" /> <a
    href="<%=request.getContextPath()%>/controller/logout" class="top-nav" id="logout">Logout</a>
</nav>