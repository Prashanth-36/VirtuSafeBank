<%@page import="utility.TransactionType"%>
<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="java.util.List"%>
<%@page import="model.Transaction"%>
<%@page import="logicallayer.CustomerHandler"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>My Account</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/home.css" />
  </head>
  <body>
  <%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <% request.setAttribute("activePath","myAccounts"); %>
	<%@ include file="../addOns/customerHeader.jsp" %>
          <div class="search">
            <% boolean isPrimary=(boolean) request.getAttribute("isPrimary");
            if(!isPrimary){ %>
            <button class="btn-option" onclick="document.getElementById('setPrimary').submit()"><img src="<%=request.getContextPath()%>/static/images/financial.png" style="width:2rem">Set as Primary Account</button>
            <%} %>
            <button class="btn-option"  onclick="toggle()"><img src="<%=request.getContextPath()%>/static/images/password.png" style="width:2rem">Change MPIN</button>
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
        <table class="border-table">
          <tr>
            <th
              colspan="8"
              style="
                text-align: center;
                background-color: var(--blue);
                color: white;
                border:none;
              "
            >
            <span style="text-align: center;background-color: var(--blue);color: white;border:none;">Account No: <%=request.getAttribute("accountNo") %> </span>
              - Transaction Statements
            </th>
          </tr>
          <tr>
            <th>Transaction ID</th>
            <th>Time</th>
            <th>Transaction type</th>
            <th>Amount</th>
            <th>Transactional Account</th>
            <th>Description</th>
            <th>Balance</th>
          </tr>
          <%
          	List<Transaction> transactions=(List<Transaction>)request.getAttribute("transactions");
          for(Transaction transaction:transactions){
        	  String description=transaction.getDescription();
        	  TransactionType type= transaction.getType();
        	  int transactionalAccount=transaction.getTransactionalAccount();
          %>
          
          <tr>
            <td><%=transaction.getId()%></td>
            <td><%=Utils.formatLocalDateTime(Utils.millisToLocalDateTime(transaction.getTimestamp(), ZoneId.systemDefault()))%></td>
            <td><%=type%></td>
            <td class="<%=type.name().toLowerCase()%>"><%=(type==TransactionType.DEBIT?"-":"+")+transaction.getAmount()%></td>
            <td><%=transactionalAccount==0?"Self" :transactionalAccount%></td>
            <td><%=(description==null || description.isEmpty())? "-" : description%></td>
            <td><%=transaction.getBalance()%></td>
          </tr>
          
          <% } %>
        </table>

<%@ include file="../addOns/pagination.jsp" %>
     <%String errorMessage=request.getParameter("error"); %>
    <div id="back-drop" class="back-drop" style="<%=errorMessage!=null?"display:flex":"" %>" onclick="toggle()">
      <form
        method="post"
        onsubmit="return validate()"
        onclick="event.stopPropagation()"
        action="<%=request.getContextPath() %>/controller/user/changeMpin"
        id="form"
        style="width: 40%; height: fit-content"
        class="form-container"
      >
        <h3>Change MPIN</h3>
        <input
          type="hidden"
          name="accountNo"
          id="accountNo"
          value="<%=request.getParameter("accountNo") %>"
        />
        <label for="currentMpin">Current MPIN</label>
        <input
          type="password"
          name="currentMpin"
          id="currentMpin"
          placeholder="Current Mpin"
          required
        />
        <label for="newMpin">New MPIN</label>
        <input
          type="password"
          name="newMpin"
          id="newMpin"
          placeholder="New Mpin"
          required
        />
        <label for="confirmMpin">Confirm MPIN</label>
        <input
          type="password"
          name="confirmMpin"
          id="confirmMpin"
          placeholder="Confirm Mpin"
          required
        />
        <p id="error" style="color:red"><%=errorMessage!=null?errorMessage:"" %></p>
        <button>Update</button>
      </form>
    </div>
    <form action="<%=request.getContextPath() %>/controller/user/setPrimary" method="post"  id="setPrimary">
      <input
            type="hidden"
            name="accountNo"
            id="accountNo"
            value="<%=request.getParameter("accountNo") %>"
          />
    </form>
    <script>
      function toggle() {
        const backDrop = document.getElementById("back-drop");
        if (backDrop.style.display === "flex") {
          backDrop.style.display = "none";
        } else {
          backDrop.style.display = "flex";
        }
      }
      
      function selectMonth(){
    	  const params=new URLSearchParams(location.search);
        const months=document.getElementById("months").value;
        params.set("months",months);
        window.location.search=params;
      }
      
      function validate(){
    	  if(document.getElementById("confirmMpin").value===document.getElementById("newMpin").value){
    	  console.log("true");
    		  return true;
    	  }
    	  console.log("false");
    	  document.getElementById("error").innerText="Please re-enter new passwords correctly!";
    	  return false;
      }
    </script>
  </body>
</html>
