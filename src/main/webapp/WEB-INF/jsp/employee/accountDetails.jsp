<%@page import="utility.TransactionType"%>
<%@page import="model.Transaction"%>
<%@page import="model.Customer"%>
<%@page import="utility.ActiveStatus"%>
<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="model.Account"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Account Details</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <% 
    request.setAttribute("activePath", "accounts"); 
    String viewer=(String) request.getAttribute("viewer");
  %>
  
  <% if(viewer!=null && viewer.equals("admin")){ %>
    <%@include file="../addOns/adminHeader.jsp" %>
  <%}else if(viewer!=null && viewer.equals("employee")){ %>
    <%@include file="../addOns/employeeHeader.jsp" %>
  <%} %>
  
  <main class="main">
    
    <table class="border-table" style="margin-bottom:1rem">
      <tr style="position: relative">
        <th colspan="6"
          style="text-align: center; background-color: var(--blue); color: white;position:reletive;height:3rem">
          Account Details
        </th>
      </tr>
      <tr>
        <th>Account Number</th>
        <th>Customer ID</th>
        <th>Current Balance</th>
        <th>Is Primary Account</th>
        <th>Account Open Date</th>
        <th>Account Status</th>
      </tr>
      <%@page import="model.Account"%>
      <%@page import="java.util.Map"%>
      <% 
          Account account=(Account)request.getAttribute("account");
       %>
      <tr onclick="window.location.href='<%=request.getContextPath()%>/controller/<%=viewer.equals("admin")?"admin":"employee"%>/manageAccount?accountNo=<%=account.getAccountNo()%>'">
        <td><%=account.getAccountNo() %></td>
        <td><%=account.getCustomerId() %></td>
        <td><%=account.getCurrentBalance() %></td>
        <td><%=account.getIsPrimaryAccount()?"YES":"NO"%></td>
        <td><%=Utils.millisToLocalDate((account.getOpenDate()),ZoneId.systemDefault()) %></td>
        <td><%=account.getStatus() %></td>
      </tr>
    </table>
    <% if(account.getStatus()==ActiveStatus.INACTIVE){ %>
    <form method="post" action="<%=request.getContextPath() %>/controller/<%=viewer.equals("admin")?"admin":"employee"%>/manageAccount">
      <input type="hidden" name="activate" value="1" />
      <input type="hidden" name="accountNo" value="<%=account.getAccountNo() %>" />
      <button
            style="
              font-size: large;
              padding: 0.5rem;
              margin: auto;
              display: block;
              background-color: #009e60;
              color: white;
            "
          >
            Activate
          </button> 
    </form>
        <%}else{ %>
      <form method="post" action="<%=request.getContextPath() %>/controller/<%=viewer.equals("admin")?"admin":"employee"%>/manageAccount">
      <input type="hidden" name="activate" value="0" />
      <input type="hidden" name="accountNo" value="<%=account.getAccountNo() %>" />
      <button
        style="font-size: large; padding: 0.5rem; margin: auto; display: block; background-color: #e34234; color: white;">
        Deactivate</button>
      </form>
     <%} %>
     
          <div style="display:flex;justify-content:flex-end;gap:1rem;align-items:center;width:90%;margin:auto">
            <label for="months">Select days:</label>
            <select name="months" id="months" class="selection" onchange="selectMonth()" required>
            <% int months=(int) request.getAttribute("months"); %>
              <option value="">Select</option>
              <option value="1" <%=months==1?"selected":"" %>>30</option>
              <option value="2" <%=months==2?"selected":"" %>>60</option>
              <option value="3" <%=months==3?"selected":"" %>>90</option>
              <option value="4" <%=months==4?"selected":"" %>>120</option>
              <option value="5" <%=months==5?"selected":"" %>>150</option>
              <option value="6" <%=months==6?"selected":"" %>>180</option>
            </select>
          </div>

        <table class="border-table" style="margin-top:1rem">
          <tr>
            <th
              colspan="8"
              style="
                text-align: center;
                background-color: var(--blue);
                color: white;
              "
            >
              Transaction Statements
            </th>
          </tr>
          <tr>
            <th>Transaction ID</th>
            <th>Time</th>
            <th>Transaction type</th>
            <th>Amount</th>
            <th>Primary Account</th>
            <th>Transactional Account</th>
            <th>Description</th>
            <th>Balance</th>
          </tr>
         <%
            List<Transaction> transactions=(List<Transaction>)request.getAttribute("transactions");
            for(Transaction transaction:transactions){
              TransactionType type=transaction.getType();
              String description=transaction.getDescription();
         %>
          <tr>
            <td><%=transaction.getId()%></td>
            <td><%=Utils.formatLocalDateTime(Utils.millisToLocalDateTime(transaction.getTimestamp(), ZoneId.systemDefault()))%></td>
            <td><%=type%></td>
            <td class="<%=type.name().toLowerCase()%>"><%=(type==TransactionType.DEBIT?"-":"+")+transaction.getAmount()%></td>
            <td><%=transaction.getPrimaryAccount()%></td>
            <td><%=transaction.getTransactionalAccount()%></td>
            <td><%=(description==null || description.isEmpty())? "-" : description%></td>
            <td><%=transaction.getBalance()%></td>
          </tr>
          <% } %>
        </table>        
        
        <%@include file="../addOns/pagination.jsp" %>
  </main>
  <script>
  function selectMonth(){
	  const params=new URLSearchParams(location.search);
    const months=document.getElementById("months").value;
    params.set("months",months);
    window.location.search=params;
  }
  </script>
</body>
</html>