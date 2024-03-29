<%@page import="model.Branch"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home</title>
    <link rel="stylesheet" href="<%=request.getContextPath() %>/static/css/home.css" />
  </head>
  <body>
  <% request.setAttribute("activePath", "accounts"); %>
  <% String viewer=(String) request.getAttribute("viewer");%>
  <%@include file="../addOns/adminHeader.jsp" %>
    <main class="main">
      <form action="<%=request.getContextPath() %>/controller/<%=viewer.equals("admin")?"admin":"employee" %>/addAccount" method="post" class="form-container">
        <h3>Create Account</h3>
        <label for="customerId">Customer ID *</label>
        <input
          type="text"
          name="customerId"
          id="customerId"
          placeholder="Customer ID"
        />
        <% if(viewer!=null && viewer.equals("admin")){ %>
          <label for="branchId">Branch ID * 
            <select name="branchId" id="branchId" style="margin:1rem" class="selection" required>
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
          </label>
        <%} %>
        <button>Create</button>
      </form>
    </main>
  </body>
</html>
