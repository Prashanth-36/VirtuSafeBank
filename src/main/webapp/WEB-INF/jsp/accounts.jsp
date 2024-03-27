<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="model.Branch"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="logicallayer.AdminHandler"%>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Home</title>
<link rel="stylesheet"
  href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
  <% request.setAttribute("activePath", "accounts"); %>
  <%@include file="adminHeader.jsp" %>
  <main class="main">
    <form id="form" action="" method="get" class="search">
      <label for="branchId">Branch ID:</label> 
      <select name="branchId"
      class="selection"
        id="branchId"
        onchange="document.getElementById('form').submit()">
        <option value="">select</option>
        <% 
          Map<Integer,Branch> branches=(Map<Integer,Branch>)request.getAttribute("branches");
          if(branches!=null){
    		  for(int branchId:branches.keySet()){
    			  Object id=request.getAttribute("branchId");
        %>
        <option value="<%=branchId%>" <%= (id!=null && (int)id==branchId)?"selected":"" %>><%=branchId%></option>
        <%
    		  }
          }
        %>
      </select>
      <label for="branchStatus">Branch Status:</label>
      <select name="branchStatus"
      class="selection"
        id="branchStatus"
        onchange="document.getElementById('form').submit()">
        <option value="1" <%= (1==(int)request.getAttribute("status"))?"selected":"" %>>ACTIVE</option>
        <option value="0" <%= (0==(int)request.getAttribute("status"))?"selected":"" %>>INACTIVE</option>
      </select> 
        <label for="accountNo">Account No:</label>
        <input
          type="search"
          id="accountNo"
          placeholder="Account No"
        />
        <img src="<%=request.getContextPath() %>/static/images/search.png" alt="" width="50rem" onclick="redirect()"/>
      </form>
    <table class="border-table">
      <tr style="position: relative">
        <th colspan="7"
          style="text-align: center; background-color: var(--blue); color: white;position:reletive;height:3rem">
          Accounts
            <button onclick="window.location.href='<%=request.getContextPath() %>/controller/addAccount'"
              style="position: absolute; font-size: larger; right: 1rem;top:.7rem; height:2rem;width: 2rem;">
              +</button>
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
      <%@page import="model.Account"%>
      <%@page import="java.util.Map"%>
      <% 
          Object acc=request.getAttribute("accounts");
          if(acc!=null){
          	Map<Integer,Account> accounts=(Map<Integer,Account>)acc;
          	for(Map.Entry<Integer,Account> e:accounts.entrySet()){
          		int id=e.getKey();
          		Account account=e.getValue();
          %>
      <tr onclick="window.location.href='<%=request.getContextPath()%>/controller/manageAccount?accountNo=<%=id%>'">
        <td><%=id %></td>
        <td><%=account.getCustomerId() %></td>
        <td><%=account.getCurrentBalance() %></td>
        <td><%=account.isPrimaryAccout()?"YES":"NO"%></td>
        <td><%=Utils.millisToLocalDate((account.getOpenDate()),ZoneId.systemDefault()) %></td>
        <td><%=account.getBranchId() %></td>
        <td><%=account.getStatus() %></td>
      </tr>
      <%
          	}
          }
          %>
    </table>
  </main>
  <%@ include file="pagination.jsp"%>
  
  <script>
  function redirect(){
  	window.location.href='<%=request.getContextPath()%>/controller/manageAccount?accountNo='+document.getElementById('accountNo').value;
		  }
  </script>
</body>
</html>
