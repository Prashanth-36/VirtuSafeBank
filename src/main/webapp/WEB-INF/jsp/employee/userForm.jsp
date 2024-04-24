<%@page import="java.time.LocalDate"%>
<%@page import="model.Account"%>
<%@page import="utility.ActiveStatus"%>
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
  <% 
    request.setAttribute("activePath", "users"); 
    User user=(User)request.getAttribute("user");
  %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><%=user==null?"Create User":"User Details" %></title>
    <link rel="stylesheet" href="<%=request.getContextPath() %>/static/css/home.css" />
  </head>
  <body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <% 
    UserType userType=(UserType) session.getAttribute("userType");
    if(userType==UserType.ADMIN){ 
  %>
  <%@include file="../addOns/adminHeader.jsp" %>
  <%}else{ %>
  <%@include file="../addOns/employeeHeader.jsp" %>
  <%} %>
    <div class="user-form-container">
      <form id="form" action="<%=request.getContextPath() %>/controller/<%=userType==UserType.ADMIN?"admin":"employee" %>/<%=user==null?"addUser":"modifyUser" %>" method="post" style="position:relative;">
        <h3 style="position:relative"><%=user!=null? userType==UserType.ADMIN? "User Details":"Customer Details": userType==UserType.ADMIN? "Add User":"Add Customer" %> 
        <%if(user!=null){ %>
          <%if(user.getType()==UserType.USER){%>
            <img src="<%=request.getContextPath()%>/static/images/accounts.png" title="view all accounts" style="width:2rem;position:absolute;right:5rem;top:.6rem;" onclick="getAllAccounts()" alt="all Accounts">
          <%} %>
          <img src="<%=request.getContextPath()%>/static/images/edit-white.png" id="edit" title="edit" style="width:1.8rem;position:absolute;right:1rem;top:.7rem;" onclick="enableEdit()" alt="edit">
        <%} %>
        </h3>
         <%if(user!=null){%>
          <input type="hidden" name="userId" value="<%=user.getUserId()%>"/>
        <%} %>
        <div class="user-form">
          <div class="form-column">
            <div class="form-row">
              <label for="name">Name</label>
              <input type="text" name="name" maxlength=30 value="<%=user!=null?user.getName():""%>" id="name" placeholder="Name" required>
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
              <input type="tel" name="number" pattern="^[1-9][0-9]{9}$" title="10-digit mobile no" value="<%=user!=null?user.getNumber():""%>"  id="number" placeholder="Number" required>
            </div>
            <div class="form-row">
              <label for="email">Email</label>
              <input type="email" name="email" value="<%=user!=null?user.getEmail():""%>"  id="email" placeholder="Email" required>
            </div>
            <% if(user==null){%> 
              <div class="form-row">
                <label for="password">Password</label>
                <input type="password" name="password"  id="password" placeholder="password" <%=user==null?"required":"" %>>
              </div>
            <%} %>
          </div>
          <div class="form-column">
            <div class="form-row">
                <label for="userType">User Type</label>
                <select name="userType" id="userType" style="margin:1rem" class="selection" onchange="toggleInputs()" required >
                  <% if(userType==UserType.ADMIN){%>
                    <option value="">Select</option>
                    <option value="2" <%=user!=null && user.getType()==UserType.EMPLOYEE?"selected":""%>>Employee</option>
                    <option value="1" <%=user!=null && user.getType()==UserType.ADMIN?"selected":""%>>Admin</option>
                  <%} %>
                  <option value="0" <%=user!=null && user.getType()==UserType.USER?"selected":""%>>Customer</option>
                </select>
            </div>
            <% 
              Employee employee=null;
              if(user!=null && user instanceof Employee){
            	   employee=(Employee) user;
              }
            %>
            <%if((user==null&& userType==UserType.ADMIN)|| (user!=null && user.getType()!=UserType.USER)){ %>
            <div class="form-row" id="branchField">
              <label for="branchId" id="branchIdLabel">Branch ID *</label>
                <select name="branchId" id="branchId" style="margin:1rem" class="selection" required>
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
            <%} %>
            <% 
              Customer customer=null;
              if(user!=null && user instanceof Customer){
                 customer=(Customer) user;
              }
            %>
            <div style="width:100%;display:<%=user!=null && user.getType()==UserType.USER?"block":"none"%>" id="customer">
              <div class="form-row">
                <label for="aadhaarNo">Aadhaar No</label> 
                <input type="text" name="aadhaarNo"  pattern="^[1-9][0-9]{11}$" title="12-digit Aadhaar no"  value="<%=customer!=null?customer.getAadhaarNo():""%>"  id="aadhaarNo" placeholder="Aadhaar No">
              </div>
              <div class="form-row">
                <label for="panNo">PAN</label> 
                <input type="text" name="panNo" pattern="^[A-Z]{5}[0-9]{4}[A-Z]$" title="ABCDE1234F-PAN no" value="<%=customer!=null?customer.getPanNo():""%>" id="panNo" placeholder="PAN" >
              </div>
            </div>
            <div class="form-row">
              <label for="location">Location</label> 
              <input type="text" name="location" maxlength=50 value="<%=user!=null?user.getLocation():""%>"  id="location" placeholder="Location" required>
            </div>
            <div class="form-row">
              <label for="city">City</label>
              <input type="text" name="city" maxlength=30 value="<%=user!=null?user.getCity():""%>"  id="city" placeholder="City" required>
            </div>
            <div class="form-row">
              <label for="state">State</label>
              <input type="text" name="state" maxlength=30 value="<%=user!=null?user.getState():""%>"  id="state" placeholder="State" required>
            </div>
          </div>
          <button id="submit" <%=user!=null?"disabled":"" %> style='<%=user!=null?"display:none":""%>'><%=user!=null? "Update":"Create" %></button>
          <button type="reset" id="reset" onclick="cancelEdit()" class="btn-danger" style="display:none;position:absolute;bottom:1rem;right:1rem">Discard</button>
        </div>
      </form>
          <%if(user!=null){ %>
            <form method="post" id="activateForm" action="<%=request.getContextPath() %>/controller/<%=userType==UserType.ADMIN?"admin":"employee" %>/manageUser">
              <input type="hidden" name="userId" value="<%=user.getUserId() %>" />
              <input type="hidden" name="userType" value="<%=user.getType() %>" />
              <% if(user.getStatus()==ActiveStatus.INACTIVE){ %>
                <button id="activate" name="activate" class="btn-success" value="1">Activate</button>
              <%}else{ %>
                <button id="activate" name="activate" class="btn-danger" value="0">Deactivate</button>
              <%} %>
            </form>
          <%} %>
      </div>
    
   <% if(request.getAttribute("accounts")!=null){ %>
      <table class="border-table">
        <tr>
          <th colspan="7"
            style="text-align: center; background-color: var(--blue); color: white;">
            Accounts</th>
        </tr>
        <tr>
          <th>Account Number</th>
          <th>Current Balance</th>
          <th>Is Primary Account</th>
          <th>Account Open Date</th>
          <th>Branch ID</th>
          <th>Account Status</th>
        </tr>
  
        <%
        Map<Integer, Account> accounts = (Map<Integer, Account>) request.getAttribute("accounts");
        for (Map.Entry<Integer, Account> e : accounts.entrySet()) {
          int id = e.getKey();
          Account account = e.getValue();
        %>
        <tr
          onclick="window.location.href='<%=request.getContextPath()%>/controller/<%=userType==UserType.ADMIN?"admin":"employee" %>/manageAccount?accountNo=<%=id%>'">
          <td><%=id%></td>
          <td><%=account.getCurrentBalance()%></td>
          <td><%=account.getIsPrimaryAccount() ? "YES" : "NO"%></td>
          <td><%=Utils.millisToLocalDate((account.getOpenDate()),ZoneId.systemDefault()) %></td>
          <td><%=account.getBranchId()%></td>
          <td><%=account.getStatus()%></td>
        </tr>
        <%
        }
        %>
      </table>
     <%} %>
  

 <%if(user==null || userType==UserType.ADMIN){ %>
    <script>
      const customer=document.getElementById("customer");
      const aadhaarNo=document.getElementById("aadhaarNo");
      const panNo=document.getElementById("panNo");
      const branchLabel= document.getElementById("branchIdLabel");
      function toggleInputs(){
        const type=document.getElementById("userType").value;
        if(type==="0"){
          customer.style.display="block";
          aadhaarNo.setAttribute("required","required");
          //panNo.setAttribute("required","required");
          aadhaarNo.removeAttribute("disabled");
          panNo.removeAttribute("disabled");
          if(branchLabel){branchLabel.innerText="Account's Branch ID";}
        }else{
          customer.style.display="none";
          aadhaarNo.removeAttribute("required");
          //panNo.removeAttribute("required");
          aadhaarNo.setAttribute("disabled","disabled");
          panNo.setAttribute("disabled","disabled");
          if(branchLabel){ branchLabel.innerText="Branch ID";}
        }
      }
      window.onload=toggleInputs();
    </script>
<%} %>
    <%if(user!=null){ %>
    <script>
      const fields = document.querySelectorAll("#form input");
      const selections = document.querySelectorAll("#form select");
      const submit=document.getElementById("submit");
      const reset=document.getElementById("reset");
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
            reset.style.display="none";
          }else{
            submit.removeAttribute("disabled");
          }
        });
      }
      for(field of selections){
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
      function enableEdit(){
		  for(e of selections){
    		  e.removeAttribute("disabled");
    	  }
    	  for(e of fields){
    		  e.removeAttribute("disabled");
    	  }
    	  document.getElementById("activate").style.display="none";
    	  submit.style.display="block";
    	  reset.style.display="block";
      }
      function cancelEdit(){
		  for(e of selections){
    		  e.setAttribute("disabled","disabled");
    	  }
    	  for(e of fields){
    		  e.setAttribute("disabled","disabled");
    	  }
    	  submit.style.display="none";
    	  reset.style.display="none";
    	  document.getElementById("activate").style.display="block";
      }
      window.onload=()=>{
    	  for(e of selections){
    		  e.setAttribute("disabled","disabled");
    	  }
    	  for(e of fields){
    		  e.setAttribute("disabled","disabled");
    	  }
      };
      <%if(user.getType()==UserType.USER){%>
     	function getAllAccounts(){
     		const params=new URLSearchParams(window.location.search);
     		params.set("getAllAccounts","1");
     		params.delete("message");
     		window.location.search=params;
     	}
      <%}%>
    </script>
    <%} %>
  </body>
</html>