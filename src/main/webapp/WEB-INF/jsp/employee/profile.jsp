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
  <% String viewer=(String) request.getAttribute("viewer"); %>
  <%if(viewer.equals("admin")){ %>
  <%@include file="../addOns/adminHeader.jsp" %>
  <%}else{ %>
  <%@include file="../addOns/employeeHeader.jsp" %>
  <%} %>
  <% Employee profile=(Employee) request.getAttribute("profile"); %>
  <main class="main">
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
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/gender.png" alt=""> Gender:</div>
                  <div><%=profile.getGender() %></div>
              </div>
           </div>
           <div class="column">
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/user-type.png" alt=""> User Type:</div>
                  <div><%=profile.getType() %></div>
              </div>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/branches.png" alt=""> Branch ID:</div>
                  <div><%=profile.getBranchId() %></div>
              </div>
              <div class="row">
                  <div><img src="<%=request.getContextPath() %>/static/images/location.png" alt=""> Location:</div>
                  <div><%=profile.getLocation() %></div>
              </div>
              <div class="row">
                  <div style="margin-left:2.5rem;">City:</div>
                  <div><%=profile.getCity() %></div>
              </div>
              <div class="row">
                  <div style="margin-left:2.5rem;">State:</div>
                  <div><%=profile.getState() %></div>
              </div>
            </div>
          </div>
          <div style="display:flex;align-items:center;gap:1rem;justify-content:center" onclick="toggle()"><img src="<%=request.getContextPath()%>/static/images/password.png" style="width:2rem"/> Change Password</div>
      </div>
      
       <div id="back-drop" class="back-drop" onclick="toggle()">
      <form
        method="post"
        onclick="event.stopPropagation()"
        action="<%=request.getContextPath() %>/controller/<%=viewer.equals("admin")?"admin":"employee" %>/changePassword"
        id="form"
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
        <input
          type="password"
          name="currentPassword"
          id="currentPassword"
          placeholder="Current Password"
        />
        <input
          type="password"
          name="newPassword"
          id="newPassword"
          placeholder="New Password"
        />
        <input
          type="password"
          name="confirmPassword"
          id="comfirmPassword"
          placeholder="Confirm Password"
        />
        <button>Update</button>
      </form>
    </div>
  </main>
  
  <script>
  function toggle() {
      const backDrop = document.getElementById("back-drop");
      if (backDrop.style.display === "flex") {
        backDrop.style.display = "none";
      } else {
        backDrop.style.display = "flex";
      }
    }
  </script>
</body>
</html>