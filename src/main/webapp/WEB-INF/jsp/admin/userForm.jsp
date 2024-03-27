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
    String type=(String)request.getAttribute("type");
    User user=(User)request.getAttribute("user");
  %>
  <%@include file="../addOns/adminHeader.jsp" %>
    <main class="main">
      <form action="<%=request.getContextPath() %>/controller/admin/<%=user==null?"addUser":"modifyUser" %>" method="post" class="user-form-container">
        <h3><%=user!=null? "Modify User":"Add User" %></h3>
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
              <input type="tel" name="number" value="<%=user!=null?user.getNumber():""%>"  id="number" placeholder="Number" required>
            </div>
            <div class="form-row">
              <label for="email">Email</label>
              <input type="email" name="email" value="<%=user!=null?user.getEmail():""%>"  id="email" placeholder="Email" required>
            </div>
            <div class="form-row" style="display:<%=user==null?"flex":"none"%>">
              <label for="password">Password</label>
              <input type="password" name="password"  id="password" placeholder="password" <%=user==null?"required":"" %>>
            </div>
          </div>
          <div class="form-column">
            <div class="form-row">
                <label for="userType">User Type</label>
                <select name="userType" id="userType" style="margin:1rem" class="selection" onchange="toggleInputs()" required >
                  <option value="">Select</option>
                  <option value="0" <%=user!=null&& user.getType()==UserType.USER?"selected":""%>>Customer</option>
                  <option value="2" <%=user!=null&& user.getType()==UserType.EMPLOYEE?"selected":""%>>Employee</option>
                  <option value="3" <%=user!=null&& user.getType()==UserType.ADMIN?"selected":""%>>Admin</option>
                </select>
            </div>
            <% 
              Employee employee=null;
              if(user!=null && user instanceof Employee){
            	   employee=(Employee) user;
              }
            %>
            <div class="form-row" style="display:<%=user!=null && (user.getType()==UserType.ADMIN||user.getType()==UserType.EMPLOYEE)?"flex":"none"%>" id="employee">
              <label for="branchId">Branch ID *</label>
                <select name="branchId" id="branchId" style="margin:1rem" class="selection">
                  <option value="">Select</option>
                  <% 
                  Map<Integer,Branch> branches=(Map<Integer,Branch>)request.getAttribute("branches");
                    if(branches!=null){
                      for(int branchId:branches.keySet()){
                  %>
                        <option value="<%=branchId%>" <%=employee!=null&& employee.getBranchId()==branchId?"selected":""%>><%=branchId%></option>
                  <%
                      }
                    }
                  %>
                </select>
            </div>
            <% 
              Customer customer=null;
              if(user!=null && user instanceof Customer){
                 customer=(Customer) user;
              }
            %>
            <div style="width:100%;display:<%=user!=null && user.getType()==UserType.USER?"block":"none"%>" id="customer">
              <div class="form-row">
                <label for="aadhaarNo">Aadhaar No</label> 
                <input type="text" name="aadhaarNo"  value="<%=customer!=null?customer.getAadhaarNo():""%>"  id="aadhaarNo" placeholder="Aadhaar No">
              </div>
              <div class="form-row">
                <label for="panNo">PAN</label> 
                <input type="text" name="panNo" value="<%=customer!=null?customer.getPanNo():""%>" id="panNo" placeholder="PAN" >
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
          <button><%=user!=null? "Update":"Create" %></button>
        </div>
      </form>
    </main>

    <script>
      const customer=document.getElementById("customer");
      const employee=document.getElementById("employee");
      function toggleInputs(){
        const type=document.getElementById("userType").value;
        if(type===""){
          customer.style.display="none";
          employee.style.display="none";
        }else if(type==="0"){
          customer.style.display="block";
          employee.style.display="none";
        }else{
          customer.style.display="none";
          employee.style.display="flex";
        }
      }
    </script>
  </body>
</html>