<%@page import="model.Branch"%>
<%@page import="utility.UserType"%>
<%@page import="model.Employee"%>
<%@page import="java.time.ZoneId"%>
<%@page import="utility.Utils"%>
<%@page import="model.Customer"%>
<%@page import="model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Profile</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/home.css"/>
</head>
<body>
<%    
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache"); 
      response.setHeader("Expires", "0");
%>
  <% request.setAttribute("activePath", ""); %>
  <% UserType userType=(UserType) session.getAttribute("userType"); %>
  <%if(userType==UserType.ADMIN){ %>
  <%@include file="../addOns/adminHeader.jsp" %>
  <%}else if(userType==UserType.EMPLOYEE){ %>
  <%@include file="../addOns/employeeHeader.jsp" %>
  <%}else{ %>
    <%@include file="../addOns/customerHeader.jsp" %>
  <%} %>
  <% 
    User profile=(User) request.getAttribute("profile"); 
  %>
      <div class="card profile-container">
          <h3 style="text-align:center;background-color:var(--blue);color:white;padding:1rem">My Profile</h3>
          <div class="profile">
            <div class="column">
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/id.png" alt=""> User ID: </div>
                  <div><%=profile.getUserId() %></div>
              </div>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/user.png" alt=""> Name: </div>
                  <div><%=profile.getName() %></div>
              </div>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/calendar.png" alt=""> DOB: </div>
                  <div><%=Utils.millisToLocalDate(profile.getDob(), ZoneId.systemDefault()) %></div>
              </div>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/telephone-call.png" alt=""> Number:</div>
                  <div><%=profile.getNumber() %></div>
              </div>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/email.png" alt=""> Email:</div>
                  <div><%=profile.getEmail() %></div>
              </div>
           </div>
           <div class="column">
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/gender.png" alt=""> Gender:</div>
                  <div><%=profile.getGender() %></div>
              </div>
           <% if(userType==UserType.USER){ 
              Customer customer=(Customer)profile;
           %>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/digital.png" alt=""> Aadhaar No:</div>
                  <div><%=customer.getAadhaarNo() %></div>
              </div>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/business-card.png" alt=""> PAN:</div>
                  <div><%=customer.getPanNo() %></div>
              </div>
            <%}else{ 
              Branch branch=(Branch) request.getAttribute("branch");
            %>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/user-type.png" alt=""> User Type:</div>
                  <div><%=profile.getType() %></div>
              </div>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/branches.png" alt=""> Branch:</div>
                  <div><%=branch.getLocation()+", "+branch.getCity() %></div>
              </div>
            <%} %>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/location.png" alt=""> Location:</div>
                  <div><%=profile.getLocation() %>, <%=profile.getCity() %>, <%=profile.getState() %></div>
              </div>
            </div>
          </div>
          <button style="cursor:pointer" onclick="toggle()"><img src="<%=request.getContextPath()%>/static/images/password-white.png" style="width:1.8rem;"/> Change Password</button>
      </div>
      <%String errorMessage=request.getParameter("error"); %>
       <div id="back-drop" class="back-drop" style="<%=errorMessage!=null?"display:flex":"" %>" onclick="toggle()">
      <form
        method="post"
        onclick="event.stopPropagation()"
        action="<%=request.getContextPath() %>/controller/<%=userType.name().toLowerCase() %>/changePassword"
        id="form"
        onsubmit="return validate()"
        style="width: 40%; height: fit-content"
        class="form-container"
      >
        <h3>Change Password</h3>
        <input
          type="hidden"
          name="customerId"
          id="customerId"
          value="<%=profile.getUserId() %>"
        />
        <label for="currentPassword">Current Password</label>
        <input
          type="password"
          name="currentPassword"
          id="currentPassword"
          placeholder="Current Password"
          required
        />
        <label for="newPassword">New Password</label>
        <input
          type="password"
          name="newPassword"
          id="newPassword"
          placeholder="New Password"
          required
        />
        <label for="confirmPassword">Confirm Password</label>
        <input
          type="password"
          name="confirmPassword"
          id="confirmPassword"
          placeholder="Confirm Password"
          required
        />
        <p id="error" style="color:red"><%=errorMessage!=null?errorMessage:"" %></p>
        <button>Update</button>
      </form>
    </div>
  
  <script>
  function toggle() {
      const backDrop = document.getElementById("back-drop");
      if (backDrop.style.display === "flex") {
        backDrop.style.display = "none";
      } else {
        backDrop.style.display = "flex";
      }
    }
  function validate(){
	  console.log(document.getElementById("confirmPassword").value);
	  console.log(document.getElementById("newPassword").value);
	  if(document.getElementById("confirmPassword").value===document.getElementById("newPassword").value){
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