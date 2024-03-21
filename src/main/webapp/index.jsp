<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/style.css" />
  </head>
  <body>
    <form action="<%=request.getContextPath() %>/controller/login" id="form-container" method="post">
      <img src="<%=request.getContextPath() %>/images/logo.png" alt="VirtuSafe Bank" id="logo" />
      <input
        type="text"
        name="userId"
        id="userId"
        placeholder="Enter User ID"
      />
      <input
        type="password"
        name="password"
        id="password"
        placeholder="Enter Password"
      />
      <button>Login</button>
    </form>
  </body>
</html>
