<%@page import="model.Branch"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Branches</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/static/css/home.css" />
</head>
<body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
    <% request.setAttribute("activePath", "branches"); %>
    <%@include file="../addOns/adminHeader.jsp" %>
     <form id="form" action="<%=request.getContextPath() %>/controller/admin/branches" method="get" class="search">
      <label for="branchStatus">Branch Status:</label>
      <select name="branchStatus"
        class="selection"
        id="branchStatus"
        onchange="document.getElementById('form').submit()">
        <option value="1" <%= (1==(int)request.getAttribute("status"))?"selected":"" %>>ACTIVE</option>
        <option value="0" <%= (0==(int)request.getAttribute("status"))?"selected":"" %>>INACTIVE</option>
      </select> 
      </form>
	<main class="main">
		<table id="table" class="border-table">
			<tr>
				<th colspan="6"
					style="text-align: center; position: relative; background-color: var(--blue); color: white;">
					Branch Details
					<button onclick="window.location.href='<%=request.getContextPath() %>/controller/admin/addBranch'"
						style="position: absolute; font-size: larger; right: 1rem; width: 2rem;">
						+</button>
				</th>
			</tr>
			<tr>
				<th>Branch ID</th>
				<th>IFSC</th>
				<th>Location</th>
				<th>City</th>
				<th>State</th>
				<th>Branch Status</th>
			</tr>
            <%
              Map<Integer,Branch> branches=( Map<Integer,Branch>)request.getAttribute("branches");
              if(branches!=null){
                for(Map.Entry<Integer,Branch> e:branches.entrySet()){
                    int id=e.getKey();                
                    Branch branch=e.getValue();
            %>
			<tr onclick="window.location.href='<%=request.getContextPath() %>/controller/admin/branch?id=<%=id%>'">
				<td><%=id %></td>
				<td><%=branch.getIfsc() %></td>
				<td><%=branch.getLocation()%></td>
				<td><%=branch.getCity()%></td>
				<td><%=branch.getState()%></td>
				<td><%=branch.getStatus()%></td>
			</tr>
            <%
                }
              }
            %>
		</table>
	</main>
</body>
</html>
