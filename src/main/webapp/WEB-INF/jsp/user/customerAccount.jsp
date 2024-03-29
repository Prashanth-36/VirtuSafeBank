<%@page import="utility.TransactionType"%>
<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="java.util.List"%>
<%@page import="model.Transaction"%>
<%@page import="logicallayer.CustomerHandler"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="../error.jsp"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/home.css" />
  </head>
  <body>
  <% request.setAttribute("selected","myAccounts"); %>
	<%@ include file="../addOns/customerHeader.jsp" %>
      <main class="main">
        <div class="options" style="margin-top: 3rem">
          <div
            class="card"
            onclick="document.getElementById('setPrimary').submit()"
          >
            <img src="<%=request.getContextPath()%>/static/images/financial.png" alt="" width="50rem" />
            <p>Set as Primary Account</p>
          </div>
          <div class="card" onclick="toggle()">
            <img src="<%=request.getContextPath()%>/static/images/password.png" alt="" width="50rem" />Change MPIN
          </div>
          <div class="card">
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
            <th>Primary Account</th>
            <th>Transactional Account</th>
            <th>Description</th>
            <th>Balance</th>
          </tr>
          <%
          	List<Transaction> transactions=(List<Transaction>)request.getAttribute("transactions");
          for(Transaction transaction:transactions){
        	  String description=transaction.getDescription();
        	  TransactionType type= transaction.getType();
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

<%@ include file="../addOns/pagination.jsp" %>
      </main>

    <div id="back-drop" class="back-drop" onclick="toggle()">
      <form
        method="post"
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
        <input
          type="password"
          name="currentMpin"
          id="currentMpin"
          placeholder="Current MPIN"
        />
        <input
          type="password"
          name="newMpin"
          id="newMpin"
          placeholder="New MPIN"
        />
        <input
          type="password"
          name="confirmMpin"
          id="comfirmMpin"
          placeholder="Confirm MPIN"
        />
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
    </script>
  </body>
</html>
