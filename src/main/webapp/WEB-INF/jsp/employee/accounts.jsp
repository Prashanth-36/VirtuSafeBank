<%@page import="utility.UserType"%>
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
<title>Accounts</title>
<link rel="stylesheet"
  href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <% request.setAttribute("activePath", "accounts"); %>
  <% UserType userType=(UserType) session.getAttribute("userType"); %>
  
  <% if(userType==UserType.ADMIN){ %>
    <%@include file="../addOns/adminHeader.jsp" %>
  <% }else{ %>
    <%@include file="../addOns/employeeHeader.jsp" %>
  <% } %>
  
  <div class="search">
    <form id="form" action="" method="get">
      <% if(userType==UserType.ADMIN){ %>
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
      <%} %>
      </form>
      <form method="get" action="<%=request.getContextPath()%>/controller/<%=userType==UserType.ADMIN?"admin":"employee" %>/manageAccount">
        <label for="accountNo">Account No:</label>
        <input
          name="accountNo"
          type="number"
          id="accountNo"
          placeholder="Account No"
        />
        <button class="btn-search"></button>
      </form>
    </div>
    <table class="border-table">
      <tr style="position: relative">
        <th colspan="7"
          style="text-align: center; background-color: var(--blue); color: white;position:reletive;height:3rem">
          Accounts
           <button onclick="window.location.href='<%=request.getContextPath() %>/controller/<%=userType.name().toLowerCase() %>/addAccount'"
              class="add-button"><img src="<%=request.getContextPath() %>/static/images/credit-card.png" style="width:2rem"> Add Account</button>
        </th>
      </tr>
      <tr>
        <th>Account Number</th>
        <th>Customer ID</th>
        <th>Current Balance</th>
        <th>Is Primary Account</th>
        <th>Account Open Date</th>
        <% if(userType==UserType.ADMIN){ %>
          <th>Branch ID</th>
        <%} %>
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
      <tr onclick="window.location.href='<%=request.getContextPath()%>/controller/<%=userType==UserType.ADMIN?"admin":"employee" %>/manageAccount?accountNo=<%=id%>'">
        <td><%=id %></td>
        <td><%=account.getCustomerId() %></td>
        <td><%=account.getCurrentBalance() %></td>
        <td><%=account.getIsPrimaryAccount()?"YES":"NO"%></td>
        <td><%=Utils.millisToLocalDate((account.getOpenDate()),ZoneId.systemDefault()) %></td>
        <% if(userType==UserType.ADMIN){ %>
          <td><%=account.getBranchId() %></td>
        <%} %>
        <td><%=account.getStatus() %></td>
      </tr>
      <%
          	}
          }
          %>
    </table>
  <%@ include file="../addOns/pagination.jsp"%>
  
</body>
</html>
