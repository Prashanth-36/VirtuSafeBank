<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home</title>
    <link rel="stylesheet" href="<%=request.getContextPath() %>/css/home.css" />
  </head>
  <body>
    <nav>
      <ul class="top-nav">
        <li>
          <img
            src="<%=request.getContextPath() %>/images/logo.png"
            alt=""
            id="logo"
          />
        </li>
        <li>
          <a href="accounts.html" class="active">Accounts Management</a>
        </li>
        <li><a href="branches.html">Branch Management</a></li>
        <li><a href="">Customer Management</a></li>
        <li><a href="">Employee Management</a></li>
        <li><a href="">Fund Transfer</a></li>
      </ul>
      <a href="" class="top-nav" id="logout">Logout</a>
    </nav>

    <div class="main-container">
      <main class="main">
        <form id="form" action="" method="get" class="search">
          <label for="branchId">Branch ID</label>
          <select name="branchId" id="branchId" onchange="document.getElementById('form').submit()">
            <option value="">select</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
          </select>
          <img
            src="<%=request.getContextPath() %>/images/search.png"
            alt=""
            width="50rem"
          />
        </form>
        <table class="border-table">
          <tr style="position: relative">
            <th
              colspan="7"
              style="
                text-align: center;
                background-color: var(--blue);
                color: white;
              "
            >
              Account Details
              <form action="/account" method="post">
                <button
                  style="
                    position: absolute;
                    font-size: larger;
                    right: 1rem;
                    width: 2rem;
                  "
                >
                  +
                </button>
              </form>
            </th>
          </tr>
          <tr>
            <th>Account Number</th>
            <th>Customer ID</th>
            <th>Current Balance</th>
            <th>Is Primary Account</th>
            <th>Account Open Date</th>
            <th>Branch ID</th>
            <th>Account Status</th>
          </tr>
          <%@page import="model.Account" %>
          <%@page import="java.util.Map" %>
          <% 
          Object acc=request.getAttribute("accounts");
          if(acc!=null){
          	Map<Integer,Account> accounts=(Map<Integer,Account>)acc;
          	for(Map.Entry<Integer,Account> e:accounts.entrySet()){
          		int id=e.getKey();
          		Account account=e.getValue();
          %>
	          <tr onclick="window.location.href='?id=<%=id%>'">
	            <td><%=id %></td>
	            <td><%=account.getCustomerId() %></td>
	            <td><%=account.getCurrentBalance() %></td>
	            <td><%=account.isPrimaryAccout()?"YES":"NO"%></td>
	            <td><%=account.getOpenDate() %></td>
	            <td><%=account.getBranchId() %></td>
	            <td><%=account.getStatus() %></td>
	          </tr>
          <%
          	}
          }
          %>
        </table>
      </main>
    </div>
    <div class="pages" id="pages">
      <a id="previousPage" onclick="previousPage()"
        ><img
          src="<%=request.getContextPath() %>/images/left.png"
          alt="previous"
          style="width: 1rem"
      /></a>
      <a href="?page=1" class="page activePage">1</a>
      <a href="?page=2" class="page">2</a>
      <a href="?page=3" class="page">3</a>
      <a href="?page=4" class="page">4</a>
      <a href="?page=5" class="page">5</a>
      <a id="nextPage" onclick="nextPage()"
        ><img
          src="<%=request.getContextPath() %>/images/right.png"
          alt="next"
          style="width: 1rem"
      /></a>
    </div>

    <script src="<%=request.getContextPath() %>/script/page.js"></script>
  </body>
</html>
