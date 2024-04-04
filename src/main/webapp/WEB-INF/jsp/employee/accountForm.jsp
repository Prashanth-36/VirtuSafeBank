<%@page import="utility.UserType"%>
<%@page import="model.Branch"%>
<%@page import="java.util.Map"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Create Account</title>
    <link rel="stylesheet" href="<%=request.getContextPath() %>/static/css/home.css" />
  </head>
  <body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <% request.setAttribute("activePath", "accounts"); %>
  <% UserType userType=(UserType) session.getAttribute("userType");%>
  <%if(userType==UserType.ADMIN){ %>
  <%@include file="../addOns/adminHeader.jsp" %>
  <%}else{ %>
  <%@include file="../addOns/employeeHeader.jsp" %>
  <%} %>
      <form action="<%=request.getContextPath() %>/controller/<%=userType==UserType.ADMIN?"admin":"employee" %>/addAccount" method="post" class="form-container">
        <h3>Create Account</h3>
        <label for="customerId">Customer ID *</label>
        <input
          type="text"
          name="customerId"
          id="customerId"
          placeholder="Customer ID"
          required
        />
        <% if(userType==UserType.ADMIN){ %>
          <label for="branchId">Branch ID *</label>
          <select name="branchId" id="branchId" class="selection" required>
                <option value="">Select</option>
          <% 
            Map<Integer,Branch> branches=(Map<Integer,Branch>)request.getAttribute("branches");
            if(branches!=null){
              for(int branchId:branches.keySet()){
          %>
                <option value="<%=branchId%>"><%=branchId%></option>
          <%
              }
            }
          %>
          </select>
        <%} %>
        <button>Create</button>
      </form>
  </body>
</html>
