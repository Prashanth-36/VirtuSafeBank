<%@page import="model.Customer"%>
<%@page import="model.Employee"%>
<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="utility.Gender"%>
<%@page import="utility.UserType"%>
<%@page import="model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="model.Branch"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home</title>
    <link rel="stylesheet" href="<%=request.getContextPath() %>/static/css/home.css" />
  </head>
  <body>
  <% 
    request.setAttribute("activePath", "users"); 
    Customer user=(Customer)request.getAttribute("user");
  %>
  <%@include file="../addOns/employeeHeader.jsp" %>
    <main class="main">
      <form id="form" action="<%=request.getContextPath() %>/controller/employee/<%=user==null?"addUser":"modifyUser" %>" method="post" class="user-form-container">
        <h3><%=user!=null? "Modify User":"Add Customer" %></h3>
        <%if(user!=null){%>
          <input type="hidden" name="userId" value="<%=user.getUserId()%>"/>
        <%} %>
        <div class="user-form">
          <div class="form-column">
            <div class="form-row">
              <label for="name">Name</label>
              <input type="text" name="name" value="<%=user!=null?user.getName():""%>" id="name" placeholder="Name" required>
            </div>
            <div class="form-row">
              <label for="dob">D.O.B</label>
              <input type="date" name="dob" value="<%=user!=null?Utils.millisToLocalDate(user.getDob(),ZoneId.systemDefault()):""%>"  id="dob" placeholder="D.O.B" required>
            </div>
            <div class="form-row">
              <label for="gender">Gender</label>
                <select name="gender" id="gender"  style="margin:1rem" class="selection" required>
                  <option value="">Select</option>  
                  <option value="1" <%=user!=null&&user.getGender()==Gender.MALE?"selected":""%> >Male</option>
                  <option value="0" <%=user!=null&&user.getGender()==Gender.FEMALE?"selected":""%>>Female</option>
                </select>
            </div>
            <div class="form-row">
              <label for="number">Number</label>
              <input type="tel" name="number" pattern="[0-9]{10}" title="10-digit Mobile no" value="<%=user!=null?user.getNumber():""%>"  id="number" placeholder="Number" required>
            </div>
            <div class="form-row">
              <label for="email">Email</label>
              <input type="email" name="email" value="<%=user!=null?user.getEmail():""%>"  id="email" placeholder="Email" required>
            </div>
            <div class="form-row">
                <label for="userType">User Type</label>
                <select name="userType" id="userType" style="margin:1rem" class="selection" onchange="toggleInputs()" required >
                  <option value="0" readonly>Customer</option>
                </select>
            </div>
          </div>
          <div class="form-column">
            <div class="form-row" style="display:<%=user==null?"flex":"none"%>">
              <label for="password">Password</label>
              <input type="password" name="password"  id="password" placeholder="password" <%=user==null?"required":"" %>>
            </div>
            <div style="width:100%;" id="customer">
              <div class="form-row">
                <label for="aadhaarNo">Aadhaar No</label> 
                <input type="text" name="aadhaarNo" pattern="[0-9]{12}" title="12-digit Aadhaar no" value="<%=user!=null?user.getAadhaarNo():""%>"  id="aadhaarNo" placeholder="Aadhaar No">
              </div>
              <div class="form-row">
                <label for="panNo">PAN</label> 
                <input type="text" name="panNo" pattern="^[A-Z]{5}[0-9]{4}[A-Z]$" title="ABCDE1234F - Pan no" value="<%=user!=null?user.getPanNo():""%>" id="panNo" placeholder="PAN" >
              </div>
            </div>
            <div class="form-row">
              <label for="location">Location</label> 
              <input type="text" name="location" value="<%=user!=null?user.getLocation():""%>"  id="location" placeholder="Location" required>
            </div>
            <div class="form-row">
              <label for="city">City</label>
              <input type="text" name="city" value="<%=user!=null?user.getCity():""%>"  id="city" placeholder="City" required>
            </div>
            <div class="form-row">
              <label for="state">State</label>
              <input type="text" name="state" value="<%=user!=null?user.getState():""%>"  id="state" placeholder="State" required>
            </div>
          </div>
          <button id="submit" <%=user!=null?"disabled":"" %>><%=user!=null? "Update":"Create" %></button>
        </div>
      </form>
    </main>
    <%if(user!=null){ %>
    <script>
    	const fields = document.querySelectorAll("#form input");
      const submit=document.getElementById("submit");
      let changedCount=0;
    	for(field of fields){
    		field.addEventListener("change",(e)=>{
    			console.log(e.target.value);
    			console.log(e.target.defaultValue);
    			if(e.target.value===e.target.defaultValue){
            changedCount--;
          }else{
            changedCount++;
          }
          if(changedCount==0){
            submit.setAttribute("disabled","disabled");
          }else{
            submit.removeAttribute("disabled");
          }
    		});
    	}
    </script>
    <%} %>
  </body>
</html>